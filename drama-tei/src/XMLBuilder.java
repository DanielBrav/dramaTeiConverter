import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class XMLBuilder {

	// This is the class that will create the TEI file
	private Map<String, String> participants = null;
	private List<Speech> speeches = null;
	private Converter converter = null;
	private String textBody = null;
	private String listPerson = null;
	public Dictionary dic = Dictionary.getInstance();
	
	public String TITLE = "";
	public String AUTHOR = "";
	public String EDITOR = "";
	
	public XMLBuilder(Converter converter) {
		this.converter = converter;
		this.participants = converter.getParticipants();
		this.speeches = converter.getSpeeches();
	}
	
	private String getTitleStmt(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<titleStmt>\n";
		tabs += '\t';
		text += tabs + "<title n=\"Pl.\" type=\"work\">" + TITLE + "</title>\n";
		text += tabs + "<author key=\"wikidata:Q43353\">" + AUTHOR + "</author>\n";
		text += tabs + "<editor n=\"OCT\" role=\"editor\">" + EDITOR + "</editor>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</titleStmt>\n";
		return text;
	}
	
	private String getPublicationStms(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<publicationStmt>\n";
		text += tabs + "</publicationStmt>\n";
		return text;
	}
	
	private String extentSourceDesc(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<extent>\n";
		text += tabs + "</extent>\n";
		text += tabs + "<sourceDesc>\n";
		text += tabs + "</sourceDesc>\n";
		return text;
	}
	
	
	private String bibl(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<bibl>\n";
		text += tabs + "</bibl>\n";
		return text;
	}
	
	private String fileDesc(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<fileDesc>\n";
		text += getTitleStmt(tabs_amount+1);
		text += getPublicationStms(tabs_amount+1);
		text += extentSourceDesc(tabs_amount+1);
		text += bibl(tabs_amount+1);
		text += tabs + "</fileDesc>\n";
		return text;
	}
	
	private String particDesc(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<particDesc>\n";
		text += getListPerson(tabs_amount+1);
		text += tabs + "</particDesc>\n";
		return text;
	}
	
	private String textClass(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<textClass>\n";
		tabs += '\t';
		text += tabs + "<keywords>\n";
		tabs += '\t';
		text += tabs + "<term subtype=\"comedy\" type=\"genreTitle\">Comedy</term>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</keywords>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</textClass>\n";
		return text;
	}

	private String language(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<langUsage>\n";
		tabs += '\t';
		text += tabs + "<language id=\"greek\">Greek</language>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</langUsage>\n";
		return text;
	}
	
	private String profileDesc(int tabs_amount) {
		String text = "", tabs = "";
		int i;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		text += tabs + "<profileDesc>\n";
		text += particDesc(tabs_amount+1);
		text += textClass(tabs_amount+1);
		text += language(tabs_amount+1);
		text += tabs + "</profileDesc>\n";
		return text;
	}
	
	public String fullXML() {
		String text = "";
		String tabs = "";
		text += tabs + "<?xml version=\"1.0\" ?>\n";
		text += tabs + "<?xml-stylesheet type=\"text/css\" href=\"https://dracor.org/tei.css\"?>\n";
		text += tabs + "<TEI xml:lang=\"ell\" xmlns=\"http://www.tei-c.org/ns/1.0\">\n";
		tabs += '\t';
		text += tabs + "<teiHeader>\n";
		text += fileDesc(tabs.length()+1);
		text += profileDesc(tabs.length()+1);
		text += tabs + "</teiHeader>\n";
		text += getTextAttribute(tabs.length());
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</TEI>";
		return text;
	}
	
	private void createListPerson(int tabs_amount) {
		if(participants == null) {
			listPerson = "Not able to generate person list.";
			return;
		}
		int i;
		String tabs = "";
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		listPerson = tabs + "<listPerson>\n";
		tabs += '\t';
		for(Entry<String, String> person : participants.entrySet()) {
			listPerson += tabs + "<person xml:id=\"" + dic.getValue(person.getKey()) + "\">\n";
			tabs += '\t';
			listPerson += tabs + "<persName>" + person.getKey() + "</persName>\n";
			tabs = tabs.substring(0, tabs.length() - 1);
			listPerson += tabs + "</person>\n";
		}
		tabs = tabs.substring(0, tabs.length() - 1);
		listPerson += tabs + "</listPerson>\n";
	}
	
	public String getListPerson(int tabs_amount) {
		createListPerson(tabs_amount);
		return this.listPerson;
	}
	
	public void setParticipants(Map<String, String> p) {
		this.participants = p;
	}
	
	public void setSpeeches(List<Speech> s) {
		this.speeches = s;
	}
	
	public String showRawSpeeches() {
		if(speeches == null)
			return "Raw speeches not generated.";
		String ret = "";
		for(Speech s : this.speeches) {
			ret += s.getSpeaker() + ":\n";
			ret += s.getContent() + "\n";
		}
		return ret;
	}
	
	private void buildTextAttribute(int tabs_amount) {
		if(speeches == null || speeches.size() == 0) {
			this.textBody = "";
			return;
		}
		String tabs = "", speaker = "", prev_speaker = "", stage_val = "";
		String[] content = null, prev_content = null;
		char c;
		boolean after = false, div2 = false;
		int i, j;
		for(i = 0; i < tabs_amount; i++) tabs += '\t';
		String text = "";
		text += tabs + "<text>\n";
		tabs += '\t';
		text += tabs + "<body>\n";
		tabs += '\t';
		text += tabs + "<div1 type=\"episode\">\n";
		tabs += '\t';
		
		for(i = 0; i < speeches.size(); i++) {
			prev_speaker = speaker;
			prev_content = content;
			content = speeches.get(i).getContent().split("\n");
			speaker = speeches.get(i).getSpeaker();
			
			if(speaker.length() > 0) {
				
				c = speaker.charAt(speaker.length()-1);
				
				if(content[0].equals("")) {
					if(c == '@' || c == '$') {
						// if no content, can't be "strophe" for example,
						// so no need to check this instance.
						text += tabs + "<stage>" + speaker.substring(0, speaker.length() - 1) + "</stage>\n";
					}
				} else {
					if(c == '@' || c == '$') {
						String line = speaker.substring(0, speaker.length() - 1);
						stage_val = dic.getStage(line);
						if(stage_val.equals(line)) {
							text += tabs + "<stage>" + speaker.substring(0, speaker.length() - 1) + "</stage>\n";
						} else {
							div2 = true;
							text += tabs + "<div2 type=\"" + stage_val.substring(0, stage_val.length() - 1) + 
											"\" n=\"" + stage_val.charAt(stage_val.length() - 1) + "\">\n";
							tabs += '\t';
						}
						speaker = prev_speaker;
					}
					
					text += tabs + "<sp who=\"#" + dic.getValue(speaker) + "\">\n";
					tabs += '\t';
					text += tabs + "<speaker>" + speaker + "</speaker>\n";
					
					for(j = 0; j < content.length; j++)
						text += tabs + "<l>" + content[j] + "</l>\n";
					tabs = tabs.substring(0, tabs.length() - 1);
					text += tabs + "</sp>\n";
					if(div2) {
						tabs = tabs.substring(0, tabs.length() - 1);
						text += tabs + "</div2>\n";
						div2 = false;
					}
				}
			} else {
				speaker = prev_speaker;
			}
		}
		
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</div1>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</body>\n";
		tabs = tabs.substring(0, tabs.length() - 1);
		text += tabs + "</text>\n";
		
		this.textBody = text;
	}
	
	public String getTextAttribute(int tabs_amount) {
		buildTextAttribute(tabs_amount);
		return this.textBody.length() == 0 ? "Not able to generate <text> attribute." : this.textBody;
	}
}
