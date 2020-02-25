import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Dictionary {
	
	private static Dictionary dictionary = new Dictionary();
	private Map<String, String> values = new HashMap<>();
	private Map<String, String> stage = new HashMap<>();
	
	private Dictionary() {
		values.put("������", "kreon");
		values.put("����", "servant");
		values.put("��������", "antigone");
		values.put("����", "cohen");
		values.put("������", "ismene");
		values.put("�������", "eforbos");
		values.put("���", "cohen");
		values.put("�����", "choros");
		values.put("�������", "iyokesta");
		values.put("�����", "cohen");
		values.put("����", "roe");
		values.put("�������", "tirsias");
		values.put("������", "choros");
		values.put("�������", "oedipus");
		
		stage.put("������", "strophe1");
		stage.put("����������",  "antistrophe1");
		stage.put("������ �", "strophe1");
		stage.put("���������� �",  "antistrophe1");
		stage.put("������ �", "strophe2");
		stage.put("���������� �",  "antistrophe2");
		stage.put("������ �", "strophe3");
		stage.put("���������� �",  "antistrophe3");
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
