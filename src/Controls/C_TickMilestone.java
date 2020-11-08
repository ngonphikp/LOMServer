package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_TickMilestone;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_TickMilestone extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_TICK_MILESTONE;

    public static ArrayList<M_TickMilestone> gets(int id_ac){
        ArrayList<M_TickMilestone> result = new ArrayList<>();
        if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
            JsonArray tick_milestones = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
            for (int i = 0; i < tick_milestones.size(); i++){
                result.add(get((String) tick_milestones.get(i)));
            }
        }
        return result;
    }

    private static void insert(int id_ac, int id_ml, int star){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Tick Milestone
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleTickMilestone.ID, id)
                    .put(CmdDefine.ModuleTickMilestone.ID_AC, id_ac)
                    .put(CmdDefine.ModuleTickMilestone.ID_ML, id_ml)
                    .put(CmdDefine.ModuleTickMilestone.STAR, star);
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
        }
        // Update count
        updateCount(Module, id);
    }

    public static void setStar(int id_ac, int id_ml, int star){
        if(CouchBase.containKey("id_ac&id_ml->" + Module + "::" + id_ac + "&" + id_ml)){
            String key = CouchBase.get("id_ac&id_ml->" + Module + "::" + id_ac + "&" + id_ml).getString("key");
            setStar(key, star);
        }
        else {
            insert(id_ac, id_ml, star);
        }
    }

    private static void setStar(String key, int star){
        JsonObject obj = CouchBase.get(key)
                .put(CmdDefine.ModuleTickMilestone.STAR, star);
        CouchBase.set(key, obj);
    }

    private static M_TickMilestone get(String key){
        return (CouchBase.containKey(key)) ? new M_TickMilestone(CouchBase.get(key)) : null;
    }

    public static M_TickMilestone get(int id){
        return get(Module + "::" + id);
    }
}
