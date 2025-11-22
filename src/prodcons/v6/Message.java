package prodcons.v6;

public class Message {

	private String msg;
	private int messConsumed;
	private int copies;

	public Message(String msg) {
		this.msg = msg;
	}

	public void printMsg() {
		System.out.println(msg);
	}

	public String getMsg() {
		return msg;
	}

	public synchronized void setCopies(int n) {
		this.copies = n;
		this.messConsumed = 0;
	}

	public synchronized void waitProd() throws InterruptedException {
		while (messConsumed < copies) {
			wait();
		}
	}

	public synchronized void consumeAndWait() throws InterruptedException {
		messConsumed++;
		if (messConsumed < copies) {
			wait();
		} else {
			notifyAll();
		}
	}

}
