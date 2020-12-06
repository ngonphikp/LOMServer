package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_TickCampaign {
    public int id;
    public int star;

    public M_TickCampaign(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleTickCampaign.ID);
        star = obj.getInt(CmdDefine.ModuleTickCampaign.STAR);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleTickCampaign.ID, id);
        obj.putInt(CmdDefine.ModuleTickCampaign.STAR, star);
        return  obj;
    }
}
