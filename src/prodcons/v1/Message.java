package prodcons.v1;

public class Message {

	private String msg;
	
	public Message(String msg) {
		this.msg = msg;
	}
	
	public void printMsg() {
		System.out.println(msg);
	}
	
	public String getMsg() {
		return msg;
	}
	
}
