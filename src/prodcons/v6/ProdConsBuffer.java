package prodcons.v6;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

	private Message[] buffer;
	private int bufSize;
	private int in = 0, out = 0;
	private int count = 0, total = 0;

	private int totProd;
	private int finishedProd = 0;

	private Semaphore verrou;
	private Semaphore placesDispo;
	private Semaphore messDispo;

	public ProdConsBuffer(int bufSize, int totProd) {
		this.bufSize = bufSize;
		this.totProd = totProd;
		this.buffer = new Message[bufSize];

		this.verrou = new Semaphore(1);
		this.placesDispo = new Semaphore(bufSize);
		this.messDispo = new Semaphore(0);
	}

	@Override
	public void put(Message m) throws InterruptedException {
		placesDispo.acquire();
		verrou.acquire();

		buffer[in] = m;
		in = (in + 1) % bufSize;
		count++;
		total++;

		verrou.release();
		messDispo.release();
	}

	@Override
	public Message get() throws InterruptedException {
		messDispo.acquire();
		verrou.acquire();
		if (count == 0) {
			messDispo.release();
			verrou.release();
			return null;
		}

		Message m = buffer[out];
		out = (out + 1) % bufSize;
		count--;

		verrou.release();
		placesDispo.release();

		if(m != null) {
			m.consumeAndWait();
		}
		
		return m;
	}

	@Override
	public Message[] get(int k) throws InterruptedException {
		Message[] mess = new Message[k];
		for (int i = 0; i < k; i++) {
			Message m = get();
			if (m == null) {
				return null;
			}
			mess[i] = m;
		}
		return mess;
	}

	@Override
	public int nmsg() {
		return count;
	}

	@Override
	public int totmsg() {
		return total;
	}

	@Override
	public void produced() {
		try {
			verrou.acquire();
			finishedProd++;
			if (finishedProd == totProd) {
				messDispo.release();
			}
			verrou.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void put(Message m, int n) throws InterruptedException {
		if (n > bufSize) {
			return;
		}
		for (int i = 0; i < n; i++) {
			put(m);
		}
		m.waitProd();
	}
}