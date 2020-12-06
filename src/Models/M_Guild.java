package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_Guild {

    public int id;
    public String name;
    public int lv;
    public String noti;

    public M_Guild(){}

    public M_Guild(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleGuild.ID);
        name = obj.getString(CmdDefine.ModuleGuild.NAME);
        lv = obj.getInt(CmdDefine.ModuleGuild.LV);
        noti = obj.getString(CmdDefine.ModuleGuild.NOTI);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleGuild.ID, id);
        obj.putUtfString(CmdDefine.ModuleGuild.NAME, name);
        obj.putInt(CmdDefine.ModuleGuild.LV, lv);
        obj.putUtfString(CmdDefine.ModuleGuild.NOTI, noti);
        return  obj;
    }
}
