package yolo.basket.db;

import java.util.ArrayList;
import java.util.List;

public class Param {
    String key;
    String value;

    public Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static List<Param> getIdsOfEntitiesAsParams(String key, List<? extends Entity> listOfEntities) {
        List<Param> params = new ArrayList<>();
        for (Entity ent : listOfEntities)
            params.add(new Param(key, ent.getId().toString()));
        return params;
    }

    public static List<Param> listOfLongToParams(String key, List<Long> longs) {
      List<Param> params = new ArrayList<>();
      for (Long l : longs)
        params.add(new Param(key, l.toString()));
      return params;
    }

}
