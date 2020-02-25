
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Converter implements Runnable {
	public String PARTICIPANT_REGEX = ".*<strong>(.*?)</strong>.*";
	public String PARTICIPANT_JOB = ".*<p><strong>.*</strong>(.*?)</p>.*";
	public String PARTICIPANT_HEADLINE = "<h2 id=\"הנפשות\">הנפשות</h2>";
	public String PARTICIPANT_ENDLINE = "<br>";
	public String SPEEKER_P = ".*<p>(.*?)</p>.*";
	public String SQUARE_BRACKETS = "\\[(.*?)\\]";
	public String LAST_LINE_IN_PLAY = "ומלא צבאו על ארץ, שבר לא ידע ואיד.";
	public String BLOCKQUOTE = "<blockquote>";
	public String END_BLOCKQUOTE = "</blockquote>";
	public String SPLIT_LINE = "\n";
	public String CONTENT = ".*<p>(.*?)</p>.*";
	public int START_LINE = 0;
	
	private Thread t = null;
	private String file_content;
	private String file_location;
	private boolean finished = false;
	
	private Map<String, String> participants;
	private Map<String, String> participants_extra = new HashMap<>();
	
	private String[] linesSplitted = null;
	
	public List<Speech> speeches = null;
	
	synchronized public boolean isFinished() {
		return this.finished;
	}
	
	public Converter(String location) {
		this.file_content = "None";
		this.file_location = location;
	}
	
	public String getFileContent() {
		return this.file_content;
	}

	public void setFileLocation(String loc) {
		this.file_location = loc;
	}
	
	public String getFileLocation() {
		return this.file_location;
	}
	
	@Override
	public void run() {
		System.out.println("Reading file " + this.file_location + "...");
		try {
			this.file_content = new String(Files.readAllBytes(Paths.get(this.file_location)), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			this.finished = true;
			System.out.println("Finished reading file " + this.file_location + ".");
		}
	}
	
	public void start() {
		this.finished = false;
		t = new Thread(this);
		t.start();
	}
	
	public String[] getLinesSplitted() {
		if(finished) {
			this.linesSplitted = this.file_content.split(SPLIT_LINE);
			return this.linesSplitted;
		}
		return null;
	}

	public void createParticipants() {
		String[] lines_splitted = getLinesSplitted();
		int nefashot_start = -1, nefashot_end = -1, i;
		String job;
		
		for(i = 0; i < lines_splitted.length; i++) {
			if(lines_splitted[i].contains(PARTICIPANT_HEADLINE)) {
				nefashot_start = i + 1;
				break;
			}
		}
		
		for(i = nefashot_start + 1; i < lines_splitted.length; i++) {
			if(lines_splitted[i].equals(PARTICIPANT_ENDLINE)) {
				nefashot_end = i - 1;
				break;
			}
		}
		
		Pattern pattern_name = Pattern.compile(PARTICIPANT_REGEX);
		Pattern pattern_job = Pattern.compile(PARTICIPANT_JOB);
        Matcher matcher, matcher2;
        
		if(nefashot_start != -1 && nefashot_end != -1) {
			this.participants = new HashMap<>();
			for(i = nefashot_start; i < nefashot_end; i++) {
				matcher = pattern_name.matcher(lines_splitted[i]);
				matcher2 = pattern_job.matcher(lines_splitted[i]);
				job = matcher2.matches() ? matcher2.group(1) : "";
		        if(matcher.matches() && matcher.group(1) != null) { 
		            this.participants.put(removeUselessChars(matcher.group(1)), job);
		        }
			}
			// Not always works well in text, so need to add those
			/*this.participants.put("הכוהן", "");
			this.participants.put("הכהן", "");
			this.participants.put("המקהלה", "");*/
		} else if(this.participants_extra.size() == 0) {
			this.participants = new HashMap<>();
			this.participants.put("Error", "Could not generate participant list.");
		}
		
		if(participants_extra.size() > 0)
			this.participants_extra.remove("");
			this.participants.putAll(participants_extra);
		
		
	}
	
	public String removeUselessChars(String s) {
		return s.replaceAll("\\p{P}","");
	}
	
	public String removeHyperLinks(String s) {
		return s.replaceAll("\\s*<a.*</a>\\s*", "");
	}
	
	public Map<String, String> getParticipants() {
		createParticipants();
		return this.participants;
	}
	
	public List<Speech> getSpeeches() {
		createSpeeches();
		return this.speeches;
	}
	
	public void createSpeeches() {
		this.speeches = new ArrayList<Speech>();
		String[] lines_splitted = getLinesSplitted(), match_split;
		Map<String, String> participants = getParticipants();
		
		int i, j, start_index = -1, end_index = -1;
		
		boolean blockquote = false;
		
		Pattern pattern = Pattern.compile(SPEEKER_P), brackets_pattern = Pattern.compile(SQUARE_BRACKETS),
				content_pattern = Pattern.compile(CONTENT);
        Matcher matcher, matcher2, content_matcher;
        
        
        String speeker = "", prev_speeker = "", speech_content = "", match = "", strophe = "",
        	   content_match = "";
        
        Speech speech = new Speech(), brackets_speech;
        
		for(i = START_LINE; i < lines_splitted.length; i++) {
			
			// for debbuging
			if(i == 3100) {
				int tt = 5;
			}
			String line = lines_splitted[i];
			
			matcher = pattern.matcher(lines_splitted[i]);
			
			content_matcher = content_pattern.matcher(lines_splitted[i]);
			
			matcher2 = brackets_pattern.matcher(line);
			
			if(content_matcher.matches() && content_matcher.group(1) != null) {
				content_match = content_matcher.group(1);
			}
			
	        if((matcher.matches() && matcher.group(1) != null) || (content_matcher.matches() && content_matcher.group(1) != null)) {
	        	
	        	// match can be a content or a name
	        	// we will check if its name
	        	
	        	match = "";
	        	if(matcher.matches() && matcher.group(1) != null)
	        		match = matcher.group(1);
	        	
	        	matcher2 = brackets_pattern.matcher(match);
	        	
	        	match_split = match.split(" ", 2);
	        	
	        	
	        	if(participants.containsKey(match) && !match.equals(speeker)) {
	        		// Some of the speech content contains hyper links
	        		// that connects to references
	        		
	        		speech.setContent(removeHyperLinks(speech_content));
	        		
	        		this.speeches.add(speech); // add previous speech
	        		
	        		prev_speeker = speeker;
	        		speeker = match;
	        		
	        		// Someone new is speaking. Lets hear what he says.
	        		
	        		speech = new Speech();
	        		speech.setSpeaker(speeker);
	        		speech_content = "";
	        		
	        	} else if(blockquote) {
	        		speech_content += content_match + "\n";
	        		
	        		// if it's the last line in the play
	        		
	        		if(content_match.equals(LAST_LINE_IN_PLAY)) {
		        		speech.setContent(removeHyperLinks(speech_content));
		        		this.speeches.add(speech); // add previous speech
		        		break;
	        		}
	        	} else if(match_split.length > 1 && participants.containsKey(match_split[0])) {
	        		// This is the case we have "המקהלה [סטרופה א]" for example
	        		matcher2 = brackets_pattern.matcher(match_split[1].trim());
	        		strophe = matcher2.matches() ? matcher2.group(1) : "";
	        		
	        		if(!strophe.equals("")) {
		        		speech.setContent(removeHyperLinks(speech_content));
		        		
		        		this.speeches.add(speech); // add previous speech
		        		
		        		this.speeches.add(new Speech(match_split[0], ""));
		        		
		        		prev_speeker = speeker;
		        		speeker = strophe;
		        		
		        		speech = new Speech();
		        		speech.setSpeaker(strophe + "$");
		        		speech_content = "";
	        		} else {
	        			
		        		speech.setContent(removeHyperLinks(speech_content));
		        		
		        		this.speeches.add(speech); // add previous speech
		        		
		        		this.speeches.add(new Speech(match_split[0], ""));
		        		
		        		prev_speeker = speeker;
		        		speeker = strophe;
		        		
		        		speech = new Speech();
		        		
		        		speech.setSpeaker(matcher.group(1) + "@");
		        		speech_content = "";
	        		}
	        	} else if(matcher2.matches() && matcher2.group(1) != null) {
	        		// Means something in square brackets
	        		/*brackets_speech = new Speech();
	        		brackets_speech.setSpeaker("SquareBrackets");
	        		brackets_speech.setContent(matcher2.group(1));
	        		this.speeches.add(brackets_speech);*/
	        		
	        		speech.setContent(removeHyperLinks(speech_content));
	        		
	        		this.speeches.add(speech); // add previous speech
	        		
	        		prev_speeker = speeker;
	        		speeker = matcher2.group(1);
	        		
	        		speech = new Speech();
	        		speech.setSpeaker(matcher2.group(1) + "$");
	        		speech_content = "";
	        		    
	        	}
	        	
	        } else if((matcher2.matches() && matcher2.group(1) != null)) {
	        	// SQUARE BRACKETS ONLY (without content wrap)
        		speech.setContent(removeHyperLinks(speech_content));
        		
        		this.speeches.add(speech); // add previous speech
        		
        		prev_speeker = speeker;
        		speeker = matcher2.group(1);
        		
        		speech = new Speech();
        		speech.setSpeaker(matcher2.group(1) + "$");
        		speech_content = "";
	        	
	        } else if(lines_splitted[i].equals(BLOCKQUOTE)) {
	        	blockquote = true;
	        } else if(lines_splitted[i].equals(END_BLOCKQUOTE)) {
	        	blockquote = false;
	        }
	          
		}
	}
	
	public String participantsToString() {
		String s = "";
		for(Entry<String, String> e : this.participants_extra.entrySet()) {
			s += e.getKey() + "$" + e.getValue() + "\n";
		}
		return s.length() > 0 ? s.substring(0, s.length() - 1) : "";
	}
	
	public void participantsFromString(String s) {
		String[] names = s.split("\n");
		this.participants_extra = new HashMap<>();
		for(int i = 0; i < names.length; i++) {
			String[] split = names[i].split("\\$");
			if(split.length > 0)
				this.participants_extra.put(split[0], split.length > 1 ? split[1] : "");
		}
	}
}
