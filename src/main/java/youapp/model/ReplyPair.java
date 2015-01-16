package youapp.model;

public class ReplyPair {

	private Reply replyA;
	
	private Reply replyB;
	
	public ReplyPair(Reply replyA, Reply replyB) {
		this.replyA = replyA;
		this.replyB = replyB;
	}
	
	public Reply getReplyA() {
		return replyA;
	}
	
	public Reply getReplyB() {
		return replyB;
	}
	
}
