package prodcons.v5;

public class Consumer implements Runnable {

	private IProdConsBuffer buffer;
	private int consTime;

	public Consumer(IProdConsBuffer buffer, int consTime) {
		this.buffer = buffer;
		this.consTime = consTime;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Message m = buffer.get();
				if(m == null) {
					System.out.println("[C-" + Thread.currentThread().getName() + "] -> End of process");
					return;
				}
				System.out.println("[C-" + Thread.currentThread().getName() + "] -> Consommé : " + m.getMsg());
				Thread.sleep(consTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
