package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_Character {
    public int id = -1;
    public String id_cfg;
    public int lv;
    public int idx = -1;

    public M_Character(){}

    public M_Character(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleCharacter.ID);
        id_cfg = obj.getString(CmdDefine.ModuleCharacter.ID_CFG);
        lv = obj.getInt(CmdDefine.ModuleCharacter.LV);
        idx = obj.getInt(CmdDefine.ModuleCharacter.IDX);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleCharacter.ID, id);
        obj.putUtfString(CmdDefine.ModuleCharacter.ID_CFG, id_cfg);
        obj.putInt(CmdDefine.ModuleCharacter.LV, lv);
        obj.putInt(CmdDefine.ModuleCharacter.IDX, idx);
        return  obj;
    }
}
