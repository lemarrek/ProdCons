package prodcons.v5;

import java.util.concurrent.Semaphore;

public class ProdConsBuffer implements IProdConsBuffer {

    private Message[] buffer;
    private int bufSize;
    private int in = 0, out = 0;
    private int count = 0, total = 0;

    private int totProd;
    private int finishedProd = 0;

    private Semaphore mutex;
    private Semaphore notFull;
    private Semaphore notEmpty;

    public ProdConsBuffer(int bufSize, int totProd) {
        this.bufSize = bufSize;
        this.totProd = totProd;
        this.buffer = new Message[bufSize];

        this.mutex = new Semaphore(1);
        this.notFull = new Semaphore(bufSize);
        this.notEmpty = new Semaphore(0);
    }

    @Override
    public void put(Message m) throws InterruptedException {
        notFull.acquire();
        mutex.acquire();

        buffer[in] = m;
        in = (in + 1) % bufSize;
        count++;
        total++;

        mutex.release();
        notEmpty.release();
    }

    @Override
    public Message get() throws InterruptedException {
        notEmpty.acquire();
        mutex.acquire();   
        if (count == 0) {
            notEmpty.release();
            mutex.release();
            return null;
        }

        Message m = buffer[out];
        out = (out + 1) % bufSize;
        count--;

        mutex.release();
        notFull.release();
        
        return m;
    }
    
	@Override
	public Message[] get(int k) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
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
            mutex.acquire();
            finishedProd++;
            if (finishedProd == totProd) {
                notEmpty.release();
            }
            mutex.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}