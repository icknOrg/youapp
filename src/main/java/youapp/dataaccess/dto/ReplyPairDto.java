package youapp.dataaccess.dto;

public class ReplyPairDto {

	private ReplyDto replyA;
	
	private ReplyDto replyB;
	
	public ReplyPairDto() {
		// Default constructor.
	}
	
	public ReplyPairDto(ReplyDto replyA, ReplyDto replyB) {
		this.replyA = replyA;
		this.replyB = replyB;
	}

	public ReplyDto getReplyA() {
		return replyA;
	}

	public void setReplyA(ReplyDto replyA) {
		this.replyA = replyA;
	}

	public ReplyDto getReplyB() {
		return replyB;
	}

	public void setReplyB(ReplyDto replyB) {
		this.replyB = replyB;
	}
	
}
