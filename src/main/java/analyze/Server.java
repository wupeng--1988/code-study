package analyze;

public class Server implements ServerMXBean{

	public void stopServer() {
		Main.stopTime = 1;
		try {
			Main.waitLock.lockInterruptibly();
		} catch (InterruptedException e) {
			System.out.println("ֹͣ������ʱ,��ȡֹͣ��ʧ��");
			e.printStackTrace();
		}
		try {
			Main.waitCondition.signalAll();
		}finally {
			Main.waitLock.unlock();
		}
	}

}
