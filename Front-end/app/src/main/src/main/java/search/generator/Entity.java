/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: generator
 */

package search.generator;

import javafx.util.Pair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Entity {
	protected Long id;

	public Entity() {

	}

	public Long getId()        { return id;     }
	public void setId(Long id) { this.id = id;  }


	public abstract List<Pair<String, String>> getParameters();

	protected static Pair<String, String> pair(String key, String value) {
		return new Pair<>(key, value);
	}

	protected static Pair<String, String> pair(String key, Long value) {
		return pair(key, "" + value);
	}

	protected static Pair<String, String> pair(String key, Integer value) {
		return pair(key, "" + value);
	}

	protected static Pair<String, String> pair(String key, Double value) {
		return pair(key, "" + value);
	}

	protected static Pair<String, String> pair(String key, Boolean value) {
		return pair(key, "" + value);
	}

	protected static List<Pair<String, String>> mapToListOfPairs(String keyName, Map<Integer, ?> map) {
		List<Pair<String, String>> list = new ArrayList<>();
		for(Map.Entry<Integer, ?> entry : map.entrySet())
			list.add(pair(keyName, entry.getValue().toString()));
		return list;
	}
}
