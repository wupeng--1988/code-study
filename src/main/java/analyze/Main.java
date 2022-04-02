package analyze;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.Event;
import com.github.shyiko.mysql.binlog.event.EventType;
import com.github.shyiko.mysql.binlog.jmx.BinaryLogClientStatistics;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

	static BinaryLogClient client = null;
	private final static String HOST = "127.0.0.1", USERNAME = "root", PASSWORD = "root";
	private final static int POST = 3306;
	private final static int SERVER_PORT = 25010, RMI_PORT = 25014;
	public final static ReentrantLock waitLock = new ReentrantLock();
	public final static Condition waitCondition = waitLock.newCondition();
	private static JMXConnectorServer connectorServer = null;
	public static int stopTime = -1;
	private final static MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

	public static void main(String[] args){
		//����binlog
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					connectBinLog();
				} catch (Exception exception) {
					System.out.println("����ʧ��");
					exception.printStackTrace();
				}
			}
		}).start();

		try {
			registerJMX(SERVER_PORT, RMI_PORT);
			System.out.println("����jmx�ɹ�");
		} catch (Exception exception) {
			System.out.println("ע��jmxʧ��");
			exception.printStackTrace();
		}

		try {
			mbs.registerMBean(new Server(), new ObjectName("analyze:name=server"));
			mbs.registerMBean(new BinaryLogClientStatistics(), new ObjectName("analyze:name=log"));
			System.out.println("ע��mbs�ɹ�");
		} catch (Exception ex){
			System.out.println("ע��mbsʧ��");
			ex.printStackTrace();
		}

		//�̵߳ȴ�
		doWait();
		//�˳�����
		try {
			stopJMX();
			System.out.println("ֹͣjmx");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		try {
			client.disconnect();
			System.out.println("�Ͽ�����");
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	private static void doWait(){
		while (true){
			try {
				waitLock.lockInterruptibly();
			} catch (InterruptedException e) {
				break;
			}
			try {
				if(stopTime < 0) {
					waitCondition.await();
				}else{
					break;
				}
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	private static void registerJMX(int serverport, int rmiport) throws Exception{
		LocateRegistry.createRegistry(rmiport);
		JMXServiceURL url = new JMXServiceURL(String.format("service:jmx:rmi://localhost:%d/jndi/rmi://localhost:%d/jmxrmi",
				serverport, rmiport));
		connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
		connectorServer.start();
	}

	public static void stopJMX() throws Exception{
		if(connectorServer != null){
			connectorServer.stop();
		}
	}


	private static void connectBinLog() throws Exception{
		client = new BinaryLogClient(HOST, POST, USERNAME, PASSWORD);
		//���������ϴζ�ȡ�ļ���λ��
//		client.setBinlogFilename();
//		client.setBinlogPosition();
		client.registerLifecycleListener(new BinaryLogClient.LifecycleListener() {
			public void onConnect(BinaryLogClient binaryLogClient) {
				System.out.println(binaryLogClient.getServerId() + "���ӳɹ�, start position is " + binaryLogClient.getBinlogPosition());
			}
			public void onCommunicationFailure(BinaryLogClient binaryLogClient, Exception e) {
			}

			public void onEventDeserializationFailure(BinaryLogClient binaryLogClient, Exception e) {
			}
			public void onDisconnect(BinaryLogClient binaryLogClient) {
				System.out.println(binaryLogClient.getServerId() + "�Ͽ�����, end position is " + binaryLogClient.getBinlogPosition());
			}
		});
		client.registerEventListener(new BinaryLogClient.EventListener() {
			public void onEvent(Event event) {
				System.out.println(event);
			}
		});
		client.connect();
	}

}
