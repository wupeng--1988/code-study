package analyze;

public class Server implements ServerMXBean{

	public void stopServer() {
		Main.stopTime = 1;
		try {
			Main.waitLock.lockInterruptibly();
		} catch (InterruptedException e) {
			System.out.println("停止服务器时,获取停止锁失败");
			e.printStackTrace();
		}
		try {
			Main.waitCondition.signalAll();
		}finally {
			Main.waitLock.unlock();
		}
	}

}
