package Base;

import com.couchbase.client.java.document.json.JsonObject;

public abstract class BaseControl {

    protected static int getCount(String Module){
        if(CouchBase.containKey("Count" + Module)){
            JsonObject object = CouchBase.get("Count" + Module);
            return object.getInt("count");
        }
        else {
            JsonObject obj = JsonObject.create()
                    .put("count", 0);
            CouchBase.set("Count" + Module, obj);
            return 0;
        }
    }

    protected static void updateCount(String Module, int count){
        JsonObject obj = JsonObject.create()
                .put("count", count);
        CouchBase.set("Count" + Module, obj);
    }
}
