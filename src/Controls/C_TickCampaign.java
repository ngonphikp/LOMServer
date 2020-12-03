package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_TickCampaign;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_TickCampaign extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_TICK_CAMPAIGN;

    public static void set(M_TickCampaign data){
        JsonObject obj = CouchBase.get(Module + "::" + data.id)
                .put(CmdDefine.ModuleAccount.ID, data.id_ac)
                .put(CmdDefine.ModuleTickCampaign.ID_CAMPAIGN, data.id_campaign)
                .put(CmdDefine.ModuleTickCampaign.STAR, data.star);
        CouchBase.set(Module + "::" + data.id, obj);
    }

    public static ArrayList<M_TickCampaign> gets(int id_ac){
        ArrayList<M_TickCampaign> result = new ArrayList<>();
        if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
            JsonArray tick_milestones = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
            for (int i = 0; i < tick_milestones.size(); i++){
                result.add(get(tick_milestones.getString(i)));
            }
        }
        return result;
    }

    public static int insert(int id_ac, int id_campaign, int star){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Tick Milestone
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleTickCampaign.ID, id)
                    .put(CmdDefine.ModuleAccount.ID, id_ac)
                    .put(CmdDefine.ModuleTickCampaign.ID_CAMPAIGN, id_campaign)
                    .put(CmdDefine.ModuleTickCampaign.STAR, star);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link id_ac&id_ml
        {
            JsonObject obj = JsonObject.create()
                    .put("key", Module + "::" + id);
            CouchBase.set("id_ac&id_campaign->" + Module + "::" + id_ac + "&" + id_campaign, obj);
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

    public static M_TickCampaign get(int id_ac, int id_campaign){
        if(CouchBase.containKey("id_ac&id_campaign->" + Module + "::" + id_ac + "&" + id_campaign)){
            String key = CouchBase.get("id_ac&id_campaign->" + Module + "::" + id_ac + "&" + id_campaign).getString("key");
            return get(key);
        }
        return null;
    }

    private static M_TickCampaign get(String key){
        return (CouchBase.containKey(key)) ? new M_TickCampaign(CouchBase.get(key)) : null;
    }

    public static M_TickCampaign get(int id){
        return get(Module + "::" + id);
    }
}
