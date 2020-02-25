import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PlayRelations {

	public PlayRelations() {}
	
	public int[][] relationMatrix(Converter a) {
		Set<String> participants = a.getParticipants().keySet();
		Map<String, Integer> keys = new HashMap<>();
		Map<Integer, String> vals = new HashMap<>();
		int i = 0, j = 0, size = participants.size();
		for(String p : participants) {
			keys.put(p, i);
			vals.put(i, p);
			i++;
		}
		List<Speech> speeches = a.getSpeeches();
		int[][] relations = new int[size][size];
		
		String speaker = "", prev_speaker = speeches.get(0).getSpeaker();
		
		for(i = 1; i < speeches.size(); i++) {
			
			speaker = speeches.get(i).getSpeaker();
			
			if(participants.contains(speaker) && participants.contains(prev_speaker)) {
				relations[keys.get(speaker)][keys.get(prev_speaker)]++;
			}
				
			prev_speaker = speaker;
		}
		
		for(i = 0; i < size; i++) {
			for(j = 0; j < size; j++) {
				System.out.println(vals.get(i) + ", " + vals.get(j) + ": " + relations[i][j]);
			}
		}
		return relations;
	}
	
}
