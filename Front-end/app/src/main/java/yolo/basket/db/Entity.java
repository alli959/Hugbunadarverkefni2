/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: generator
 */

package yolo.basket.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class Entity {
	protected Long id;

	public Entity() {

	}

	public Long getId()        { return id;     }
	public void setId(Long id) { this.id = id;  }


	public abstract List<Param> getParameters();

	protected static Param pair(String key, String value) {
		return new Param(key, value);
	}

	protected static Param pair(String key, Long value) {
		return pair(key, "" + value);
	}

	protected static Param pair(String key, Integer value) {
		return pair(key, "" + value);
	}

	protected static Param pair(String key, Double value) {
		return pair(key, "" + value);
	}

	protected static Param pair(String key, Boolean value) {
		return pair(key, "" + value);
	}

	protected static List<Param> mapToListOfPairs(String keyName, Map<Integer, ?> map) {
		List<Param> list = new ArrayList<>();
		for(Map.Entry<Integer, ?> entry : map.entrySet())
			list.add(pair(keyName, entry.getValue().toString()));
		return list;
	}
}
