import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Dictionary {
	
	private static Dictionary dictionary = new Dictionary();
	private Map<String, String> values = new HashMap<>();
	private Map<String, String> stage = new HashMap<>();
	
	private Dictionary() {
		values.put("קריאון", "kreon");
		values.put("משרת", "servant");
		values.put("אנטיגונה", "antigone");
		values.put("הכהן", "cohen");
		values.put("איסמנה", "ismene");
		values.put("אפורבוס", "eforbos");
		values.put("כהן", "cohen");
		values.put("מקהלה", "choros");
		values.put("איוקסטה", "iyokesta");
		values.put("הכוהן", "cohen");
		values.put("רועה", "roe");
		values.put("טירסיאס", "tirsias");
		values.put("המקהלה", "choros");
		values.put("אידיפוס", "oedipus");
		
		stage.put("סטרופה", "strophe1");
		stage.put("אנטיסטרופה",  "antistrophe1");
		stage.put("סטרופה א", "strophe1");
		stage.put("אנטיסטרופה א",  "antistrophe1");
		stage.put("סטרופה ב", "strophe2");
		stage.put("אנטיסטרופה ב",  "antistrophe2");
		stage.put("סטרופה ג", "strophe3");
		stage.put("אנטיסטרופה ג",  "antistrophe3");
	}
	
	public static Dictionary getInstance() {
		return dictionary;
	}
	
	public String getValue(String key) {
		String val = values.get(key);
		return (val == null) ? key : val;
	}
	
	public String getStage(String key) {
		String val = stage.get(key);
		return (val == null) ? key : val;
	}
	
	public String dicToString() {
		String s = "";
		for(Entry<String, String> e : this.values.entrySet()) {
			s += e.getKey() + "$" + e.getValue() + "\n";
		}
		return s.length() > 0 ? s.substring(0, s.length() - 1) : "";
	}
	
	public void dicFromString(String s) {
		String[] names = s.split("\n");
		this.values = new HashMap<>();
		for(int i = 0; i < names.length; i++) {
			String[] split = names[i].split("\\$");
			if(split.length > 0)
				this.values.put(split[0], split.length > 1 ? split[1] : "");
		}
	}
	
	public String stageToString() {
		String s = "";
		for(Entry<String, String> e : this.stage.entrySet()) {
			s += e.getKey() + "$" + e.getValue() + "\n";
		}
		return s.length() > 0 ? s.substring(0, s.length() - 1) : "";
	}
	
	public void stageFromString(String s) {
		String[] names = s.split("\n");
		this.stage = new HashMap<>();
		for(int i = 0; i < names.length; i++) {
			String[] split = names[i].split("\\$");
			if(split.length > 0)
				this.stage.put(split[0], split.length > 1 ? split[1] : "");
		}
	}
}
