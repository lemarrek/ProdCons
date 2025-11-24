package prodcons.v5;

public class Consumer implements Runnable {

	private IProdConsBuffer buffer;
	private int consTime;
	private int k;

	public Consumer(IProdConsBuffer buffer, int consTime, int k) {
		this.buffer = buffer;
		this.consTime = consTime;
		this.k = k;
	}

	@Override
	public void run() {
		try {
			while (true) {
				if (k == 1) {
					Message m = buffer.get();
					if (m == null) {
						break;
					}
					System.out.println("[C-" + Thread.currentThread().getName() + "] -> Consommé : " + m.getMsg());
				} else {
					Message[] mess = buffer.get(k);
					if (mess == null) {
                        System.out.println("[C-" + Thread.currentThread().getName() + "] -> Arrêt demandé.");
                        break; 
                    }
					System.out.println("[C-" + Thread.currentThread().getName() + "] -> Consommé BLOC de " + k);
					for(Message m : mess) {
						System.out.println("    -> " + m.getMsg());
					}
				}
				Thread.sleep(consTime);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}