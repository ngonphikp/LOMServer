package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_TickCampain;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_TickCampain extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_TICK_CAMPAIN;

    public static void set(M_TickCampain data){
        JsonObject obj = CouchBase.get(Module + "::" + data.id)
                .put(CmdDefine.ModuleTickCampain.ID_AC, data.id_ac)
                .put(CmdDefine.ModuleTickCampain.ID_ML, data.id_ml)
                .put(CmdDefine.ModuleTickCampain.STAR, data.star);
        CouchBase.set(Module + "::" + data.id, obj);
    }

    public static ArrayList<M_TickCampain> gets(int id_ac){
        ArrayList<M_TickCampain> result = new ArrayList<>();
        if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
            JsonArray tick_milestones = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
            for (int i = 0; i < tick_milestones.size(); i++){
                result.add(get(tick_milestones.getString(i)));
            }
        }
        return result;
    }

    public static int insert(int id_ac, int id_ml, int star){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Tick Milestone
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleTickCampain.ID, id)
                    .put(CmdDefine.ModuleTickCampain.ID_AC, id_ac)
                    .put(CmdDefine.ModuleTickCampain.ID_ML, id_ml)
                    .put(CmdDefine.ModuleTickCampain.STAR, star);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link id_ac&id_ml
        {
            JsonObject obj = JsonObject.create()
                    .put("key", Module + "::" + id);
            CouchBase.set("id_ac&id_ml->" + Module + "::" + id_ac + "&" + id_ml, obj);
        }
        // Link id_ac
        {
            JsonObject obj = JsonObject.create();
            JsonArray tick_milestones = JsonArray.create();
            if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
                tick_milestones = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
            }
            tick_milestones.add(Module + "::" + id);
            obj.put("keys", tick_milestones);

            CouchBase.set("id_ac->" + Module + "::" + id_ac, obj);
        }
        // Update count
        updateCount(Module, id);

        return id;
    }

    public static M_TickCampain get(int id_ac, int id_ml){
        if(CouchBase.containKey("id_ac&id_ml->" + Module + "::" + id_ac + "&" + id_ml)){
            String key = CouchBase.get("id_ac&id_ml->" + Module + "::" + id_ac + "&" + id_ml).getString("key");
            return get(key);
        }
        return null;
    }

    private static M_TickCampain get(String key){
        return (CouchBase.containKey(key)) ? new M_TickCampain(CouchBase.get(key)) : null;
    }

    public static M_TickCampain get(int id){
        return get(Module + "::" + id);
    }
}
