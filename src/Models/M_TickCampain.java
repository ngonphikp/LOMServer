package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_TickCampain {
    public int id;
    public int id_ac;
    public int id_ml;
    public int star;

    public M_TickCampain(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleTickCampain.ID);
        id_ml = obj.getInt(CmdDefine.ModuleTickCampain.ID_ML);
        id_ac = obj.getInt(CmdDefine.ModuleTickCampain.ID_AC);
        star = obj.getInt(CmdDefine.ModuleTickCampain.STAR);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleTickCampain.ID, id);
        obj.putInt(CmdDefine.ModuleTickCampain.ID_ML, id_ml);
        obj.putInt(CmdDefine.ModuleTickCampain.ID_AC, id_ac);
        obj.putInt(CmdDefine.ModuleTickCampain.STAR, star);
        return  obj;
    }
}
