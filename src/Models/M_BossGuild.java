package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_BossGuild {
    public int id;
    public int id_boss;
    public int status;
    public int cur_hp;

    public M_BossGuild(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleBossGuild.ID);
        id_boss = obj.getInt(CmdDefine.ModuleBossGuild.ID_BOSS);
        status = obj.getInt(CmdDefine.ModuleBossGuild.STATUS);
        cur_hp = obj.getInt(CmdDefine.ModuleBossGuild.CUR_HP);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleBossGuild.ID, id);
        obj.putInt(CmdDefine.ModuleBossGuild.ID_BOSS, id_boss);
        obj.putInt(CmdDefine.ModuleBossGuild.STATUS, status);
        obj.putInt(CmdDefine.ModuleBossGuild.CUR_HP, cur_hp);
        return  obj;
    }
}
