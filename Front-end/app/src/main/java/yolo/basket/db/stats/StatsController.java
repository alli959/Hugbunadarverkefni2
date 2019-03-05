package yolo.basket.db.stats;

import org.json.JSONException;
import org.json.JSONObject;

import yolo.basket.db.Entity;
import yolo.basket.db.EntityController;

public class StatsController extends EntityController {

    @Override
    protected Stats jsonToEntity(JSONObject json) throws JSONException {
        System.out.println(json.toString());
        return null;
    }
}
