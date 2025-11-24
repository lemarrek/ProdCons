package prodcons.v5;

public class ProdConsBuffer implements IProdConsBuffer {

    private Message[] buffer;
    private int bufSize;
    private int total = 0;
    private int count = 0;
    private int in = 0;
    private int out = 0;
    private int totProd;
    private int finishedProd = 0;

    public ProdConsBuffer(int bufSize, int totProd) {
        this.bufSize = bufSize;
        this.totProd = totProd;
        this.buffer = new Message[bufSize];
    }

    @Override
    public synchronized void put(Message m) throws InterruptedException {
        while (count == bufSize) {
            wait();
        }
        buffer[in] = m;
        in = (in + 1) % bufSize;
        count++;
        total++;
        notifyAll();
    }

    @Override
    public synchronized Message get() throws InterruptedException {
        while (count == 0) {
            if (finishedProd == totProd) {
                return null;
            }
            wait();
        }
        Message m = buffer[out];
        out = (out + 1) % bufSize;
        count--;
        notifyAll();
        return m;
    }

    @Override
    public synchronized Message[] get(int k) throws InterruptedException {
        while (count < k) {
            if (finishedProd == totProd) {
                return null;
            }
            wait();
        }

        Message[] messages = new Message[k];
        for (int i = 0; i < k; i++) {
            messages[i] = buffer[out];
            out = (out + 1) % bufSize;
        }

        count -= k;
        notifyAll();
        return messages;
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
    public synchronized void produced() {
        finishedProd++;
        notifyAll();
    }
}