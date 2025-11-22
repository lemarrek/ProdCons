package prodcons.v6;

public class Producer implements Runnable {

	private IProdConsBuffer buffer;
	private int prodMin;
	private int prodMax;
	private int prodTime;
	private int nbCopies;

	public Producer(IProdConsBuffer buffer, int prodMin, int prodMax, int prodTime, int nbCopies) {
		this.buffer = buffer;
		this.prodMin = prodMin;
		this.prodMax = prodMax;
		this.prodTime = prodTime;
		this.nbCopies = nbCopies;
	}

	@Override
	public void run() {

		int nbMsg = (int) (Math.random() * (prodMax - prodMin + 1)) + prodMin;
		for (int i = 0; i < nbMsg; i++) {
			Message m = new Message("Message " + i + " produit par " + Thread.currentThread().getName());
			try {
				if (nbCopies == 1) {
					buffer.put(m);
					System.out.println("[P-" + Thread.currentThread().getName() + "] -> Produit : " + m.getMsg());
				} else {
					buffer.put(m, nbCopies);
					System.out.println("[P-" + Thread.currentThread().getName() + "] -> Produit (x" + nbCopies
							+ ") et LIBÉRÉ : " + m.getMsg());
				}

				Thread.sleep(prodTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer.produced();

	}

}
