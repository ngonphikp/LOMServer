package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_TickMilestone {
    public int id;
    public int id_ac;
    public int id_ml;
    public int star;

    public M_TickMilestone(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleTickMilestone.ID);
        id_ml = obj.getInt(CmdDefine.ModuleTickMilestone.ID_ML);
        id_ac = obj.getInt(CmdDefine.ModuleTickMilestone.ID_AC);
        star = obj.getInt(CmdDefine.ModuleTickMilestone.STAR);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleTickMilestone.ID, id);
        obj.putInt(CmdDefine.ModuleTickMilestone.ID_ML, id_ml);
        obj.putInt(CmdDefine.ModuleTickMilestone.ID_AC, id_ac);
        obj.putInt(CmdDefine.ModuleTickMilestone.STAR, star);
        return  obj;
    }
}
