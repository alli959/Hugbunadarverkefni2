package yolo.basket.db;
/**
 * Author: Olafur Palsson
 * HImail: olp6@gmail.com
 * Actual: olafur.palsson
 * Heiti verkefnis: search.generator
 */

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

public abstract class EntityController<Ent extends Entity> {
    //need to initialize these before using the "getAll" and "save" methods
    protected String getAllURL = "getAllURL-not-initialized-properly-somehow";
    protected String updateURL = "updateURL-not-initialized";
    protected String removeURL = "removeURL-not-initialized";
    protected String getOneURL = "getOneURL-not-initialized";

    protected abstract Ent jsonToEntity(JSONObject json) throws JSONException;
    public boolean remove(Long id) throws IOException, JSONException {
        Pair<String, String> pair = new Pair<>("id", "" + id);
        Request r = new Request(removeURL, pair);
        r.resolve();
        return true;
    }

    //returns the id
    public Long save(Ent ent) throws IOException, JSONException {
        JSONObject o = new JSONObject();
        List<Pair<String, String>> params = ent.getParameters();
        Request r = new Request(updateURL, ent.getParameters());
        JSONArray a = r.resolve();
        Long l = Long.parseLong(a.get(0).toString());
        return l;
    }

    public Long getAsLong(String key, JSONObject o) throws JSONException {
        Long l;
        try{ l = Long.parseLong((String) o.get(key)); }
        catch(ClassCastException e) {
            try { l = new Long((Integer) o.get(key)); }
            catch(ClassCastException f) {
                l = (Long) o.get(key);
            }
        }
        return l;
    }

    public Map<Integer, String> getStringMapFromJSON(JSONObject o) {
        Map<Integer, String> map = new HashMap<>();
        Integer i = 0;
        while(true) {
            try { map.put(i, (String) o.get(i.toString()).toString()); i++; }
            catch(JSONException e) { return map; }
        }
    }

    public Map<Integer, Long> getLongMapFromJSON(JSONObject o) {
        Map<Integer, String> stringMap = getStringMapFromJSON(o);
        Map<Integer, Long> longMap = new HashMap<>();
        for(Map.Entry<Integer, String> e : stringMap.entrySet())
            longMap.put(e.getKey(), Long.parseLong(e.getValue()));
        return longMap;
    }

    public List getOne(Long id) throws JSONException {
        Pair pair = new Pair("id", "" + id);
        JSONArray json;
        try {
            Request r = new Request(getOneURL, pair);
            json = r.resolve();
        } catch(IOException e) {
            e.printStackTrace(System.out);
            return new ArrayList<>();
        }

        List<Ent> entity = new ArrayList<>();
        entity.add(jsonToEntity((JSONObject) json.get(0)));
        return entity;
    }

    public Ent one(Long id) throws IOException, JSONException {
        List<Ent> list = getOne(id);
        return list.get(0);
    }

    public List getAll() throws Exception {
        Request r = new Request(getAllURL);
        JSONArray json =  r.resolve();
        String s = json.toString();
        List<Ent> entities = new ArrayList<>();

        for(int i = 0; i < json.length(); i++)
            entities.add(jsonToEntity((JSONObject) json.get(i)));
        return entities;
    }
}

