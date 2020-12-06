package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_TickCampaign;
import Util.C_Util;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_TickCampaign extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_TICK_CAMPAIGN;

    public static void set(M_TickCampaign data){
        JsonObject obj = CouchBase.get(Module + "::" + data.id)
                .put(CmdDefine.ModuleTickCampaign.STAR, data.star);
        CouchBase.set(Module + "::" + data.id, obj);
    }

    public static int insert(int id_ac, int id_campaign, int star){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Tick Campaign
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleTickCampaign.ID, id)
                    .put(CmdDefine.ModuleTickCampaign.STAR, star);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link nAc <-> nCampaign => TickCampaign
        C_Util.Linkn_n(CmdDefine.ModuleAccount.ID, id_ac, CmdDefine.Module.MODULE_ACCOUNT,
                CmdDefine.ModuleCampaign.ID, id_campaign, CmdDefine.Module.MODULE_CAMPAIGN,
                id, CmdDefine.Module.MODULE_TICK_CAMPAIGN);
        // Update count
        updateCount(Module, id);

        return id;
    }

    public static M_TickCampaign get(int id_ac, int id_camp){
        if(CouchBase.containKey(CmdDefine.ModuleAccount.ID + "&" + CmdDefine.ModuleCampaign.ID + "->" + Module + "::" + id_ac + "&" + id_camp)){
            String key = CouchBase.get(CmdDefine.ModuleAccount.ID + "&" + CmdDefine.ModuleCampaign.ID + "->" + Module + "::" + id_ac + "&" + id_camp).getString("key");
            return get(key);
        }
        return null;
    }

    public static ArrayList<M_TickCampaign> gets(int id_ac){
        ArrayList<M_TickCampaign> result = new ArrayList<>();
        if(CouchBase.containKey(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac)){
            JsonArray tick_milestones = CouchBase.get(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac).getArray("keys");
            for (int i = 0; i < tick_milestones.size(); i++){
                result.add(get(tick_milestones.getString(i)));
            }
        }
        return result;
    }

    private static M_TickCampaign get(String key){
        return (CouchBase.containKey(key)) ? new M_TickCampaign(CouchBase.get(key)) : null;
    }

    public static M_TickCampaign get(int id){
        return get(Module + "::" + id);
    }
}
