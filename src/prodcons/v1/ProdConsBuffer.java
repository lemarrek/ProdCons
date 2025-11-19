package prodcons.v1;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int bufSize;
	private int total = 0;
	private int count = 0;

	private int idx_prod = 0;
	private int idx_cons = 0;

	public ProdConsBuffer(int bufSize) {
		this.bufSize = bufSize;
		this.buffer = new Message[bufSize];
	}

	@Override
	public synchronized void put(Message m) throws InterruptedException {
		while (count == bufSize) {
			wait();
		}
		buffer[idx_prod] = m;
		idx_prod = (idx_prod + 1) % bufSize;
		count++;
		total++;
		notifyAll();
	}

	@Override
	public synchronized Message get() throws InterruptedException {
		while (count == 0) {
			wait();
		}
		Message m = buffer[idx_cons];
		idx_cons = (idx_cons + 1) % bufSize;
		count--;
		notifyAll();
		return m;
	}

	@Override
	public int nmsg() {
		return count;
	}

	@Override
	public int totmsg() {
		return total;
	}

}
