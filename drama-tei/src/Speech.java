
public class Speech {

	private String speaker;
	private String content;
	
	public Speech(String speaker, String content) {
		this.speaker = speaker;
		this.content = content;
	}
	
	public Speech() {
		this.speaker = "";
		this.content = "";
	}
	
	public String getSpeaker() {
		return speaker;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setSpeaker(String speaker) {
		this.speaker = speaker;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
