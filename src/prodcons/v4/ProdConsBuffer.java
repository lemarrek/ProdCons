package prodcons.v4;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int bufSize;
	private int in = 0, out = 0;
	private int count = 0, total = 0;

	private int totProd;
	private int finishedProd = 0;

	private ReentrantLock lock;
	private Condition placesDispo;
	private Condition messDispo;

	public ProdConsBuffer(int bufSize, int totProd) {
		this.bufSize = bufSize;
		this.totProd = totProd;
		this.buffer = new Message[bufSize];
		this.lock = new ReentrantLock();
		this.placesDispo = lock.newCondition();
		this.messDispo = lock.newCondition();
	}

	@Override
	public void put(Message m) throws InterruptedException {
		lock.lock();

		while (count == bufSize) {
			placesDispo.await();
		}
		buffer[in] = m;
		in = (in + 1) % bufSize;
		count++;
		total++;

		messDispo.signal();
		lock.unlock();

	}

	@Override
	public Message get() throws InterruptedException {
		lock.lock();
		while (count == 0) {
			if (finishedProd == totProd) {
				lock.unlock();
				return null;
			}
			messDispo.await();
		}

		Message m = buffer[out];
		out = (out + 1) % bufSize;	
		count--;

		placesDispo.signal();
		lock.unlock();
		return m;
	}

	@Override
	public int nmsg() {
		lock.lock();
		int c = count;
		lock.unlock();
		return c;

	}

	@Override
	public int totmsg() {
		lock.lock();
		int tot = total;
		lock.unlock();
		return tot;
	}

	@Override
	public void produced() {
		lock.lock();
		finishedProd++;
		if (finishedProd == totProd) {
			messDispo.signalAll();
		}
		lock.unlock();
	}
}