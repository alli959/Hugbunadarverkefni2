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

/**
 d8888888888888888888888"
 888888888888888888PYP"'
 Bitchin factory                d88888888888888888D
 Homie                       8888888888888888P'     Thessi er med mikid af random methods
 Y8888888888888b       hann er btw abstract vegna thess ad madur
 C8888888Y888888P       tharf ad extenda svo hann er useful
 Y88888'd88888"
 8888P d8888P
 d8888D 88888
 d888P'  Y88dP
 nY88Pn    Y88            8"""-----....._____
 N   +N    88'            8                  NNNNNN8
 N   +N  nd88n            P                  NNNNNNP
 N   +N  N  +N           d  dNN   ...       dNNNNNN
 __...---"Nn.            N   +N  N  +N           8  NNP  dNNP  dNN  NNNNNNN
 8""         NNNNn          N   +N  N  +N           8       ""'   NNP  NNNNNNN
 8       oo  NNNNN          N   +N  N  +N           8                  NNNNNNP
 Y  dN   NN  NNNNN          N   +N  N  +N           P       ooo       dNNNNNN
 b NY   ""  YNNNN          N   +N  N  +N          d       dNN'  dNN  NNNNNNN
 8        _  bNNNb         N   +N  N  +N          8       """   NNP  NNNNNNN
 8  o8   88  NNNNN         N   +N  N  +N          8                  NNNNNNN
 Y  BP   ""  NNNNN         N   +N  N  +N          8            nnn   NNNNNNP
 b          NNNNN         N   +N  N  +N          P            NNP  dNNNNNN
 8          YNNNN         N   +N  M  +N         d             ""   NNNNNNN
 8           NNNNb        N   +N  N  +N         8                  NNNNNNN
 Y           NNNNN      __N___+N__N  +N         8                  NNNNNNP
 b          NNNNNooooodP""""""""YNNNNNNbcgmmnnn8                 dNNNNNN
 8          """'                         `"""""8                 NNNNNNN
 8                                             P                 NNNNNNN
 Y                          NNNNNNNNN         d                  NNNNNNN
 b                         NNNNNNNNN         8                  NNNNNNP
 8                         NNNNNNNNP         8                 dNNNNNN
 8        Lol              NNNNNNNN;         8                 NNNNNNN
 Y     o                   NNNNNNNN:         P                 NNNNNNN
 b   -|-                  NNNNNNNN;        d                  NNNNNNP
 ______8__........----------""""""""""""------...8..______         NNNNNNN
 _________........----------""""""""""""------......_____  """""----NNNNNNN
 """""----....___ """--
 """--- *
 */


public abstract class EntityController<Ent extends Entity> {
    //need to initialize these before using the "getAll" and "save" methods
    protected String getAllURL = "getAllURL-not-initialized-properly-somehow";
    protected String updateURL = "updateAllURL-not-Initialized";
    protected String removeURL;
    protected String getOneURL;

    /*******************************************
     *       REPO STYLE METHODS
     *******************************************/

    protected abstract Ent jsonToEntity(JSONObject json) throws JSONException;
    public boolean remove(Long id) throws IOException {
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

    public List getAll() throws IOException, JSONException {
        Request r = new Request(getAllURL);
        JSONArray json =  r.resolve();
        String s = json.toString();
        List<Ent> entities = new ArrayList<>();

        for(int i = 0; i < json.length(); i++)
            entities.add(jsonToEntity((JSONObject) json.get(i)));
        return entities;
    }
}

