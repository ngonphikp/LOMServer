package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_TickCampaign {
    public int id;
    public int id_ac;
    public int id_campaign;
    public int star;

    public M_TickCampaign(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleTickCampaign.ID);
        id_campaign = obj.getInt(CmdDefine.ModuleTickCampaign.ID_CAMPAIGN);
        id_ac = obj.getInt(CmdDefine.ModuleAccount.ID);
        star = obj.getInt(CmdDefine.ModuleTickCampaign.STAR);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleTickCampaign.ID, id);
        obj.putInt(CmdDefine.ModuleTickCampaign.ID_CAMPAIGN, id_campaign);
        obj.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        obj.putInt(CmdDefine.ModuleTickCampaign.STAR, star);
        return  obj;
    }
}
