package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_TickBossGuild {
    public int id;
    public int id_bg;
    public int id_ac;
    public int cur_turn;
    public boolean is_reward;

    public M_TickBossGuild(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleTickBossGuild.ID);
        id_bg = obj.getInt(CmdDefine.ModuleBossGuild.ID);
        id_ac = obj.getInt(CmdDefine.ModuleAccount.ID);
        cur_turn = obj.getInt(CmdDefine.ModuleTickBossGuild.CUR_TURN);
        is_reward = obj.getBoolean(CmdDefine.ModuleTickBossGuild.IS_REWARD);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleTickBossGuild.ID, id);
        obj.putInt(CmdDefine.ModuleBossGuild.ID, id_bg);
        obj.putInt(CmdDefine.ModuleAccount.ID, id_ac);
        obj.putInt(CmdDefine.ModuleTickBossGuild.CUR_TURN, cur_turn);
        obj.putBool(CmdDefine.ModuleTickBossGuild.IS_REWARD, is_reward);
        return  obj;
    }
}
