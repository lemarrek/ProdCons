package prodcons.v1;

public class Producer implements Runnable {

	private IProdConsBuffer buffer;
	private int prodMin;
	private int prodMax;
	private int prodTime;

	public Producer(IProdConsBuffer buffer, int prodMin, int prodMax, int prodTime) {
		this.buffer = buffer;
		this.prodMin = prodMin;
		this.prodMax = prodMax;
		this.prodTime = prodTime;
	}

	@Override
	public void run() {
		int nbMsg = (int) (Math.random() * (prodMax - prodMin + 1)) + prodMin;
		for (int i = 0; i < nbMsg; i++) {
			Message m = new Message("Message " + i + " produit par " + Thread.currentThread().getName());
			try {
				buffer.put(m);
				System.out.println("[P-" + Thread.currentThread().getName() + "] -> Produit : " + m.getMsg());
				Thread.sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
