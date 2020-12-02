package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_TickBossGuild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_TickBossGuild extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_TICK_BOSS_GUILD;

    public static void set(M_TickBossGuild data){
        if(CouchBase.containKey(Module + "::" + data.id)){
            JsonObject obj = CouchBase.get(Module + "::" + data.id);
            obj.put(CmdDefine.ModuleTickBossGuild.CUR_TURN, data.cur_turn);
            obj.put(CmdDefine.ModuleTickBossGuild.IS_REWARD, data.is_reward);
            CouchBase.set(Module + "::" + data.id, obj);
        }
    }

    public static int insert(int id_ac, int id_bg, int cur_turn, boolean is_reward){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Tick Boss Guild
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleTickBossGuild.ID, id)
                    .put(CmdDefine.ModuleBossGuild.ID, id_bg)
                    .put(CmdDefine.ModuleAccount.ID, id_ac)
                    .put(CmdDefine.ModuleTickBossGuild.CUR_TURN, cur_turn)
                    .put(CmdDefine.ModuleTickBossGuild.IS_REWARD, is_reward);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link id_ac&id_bg
        {
            JsonObject obj = JsonObject.create()
                    .put("key", Module + "::" + id);
            CouchBase.set("id_ac&id_bg->" + Module + "::" + id_ac + "&" + id_bg, obj);
        }
        // Link id_bg
        {
            JsonObject obj = JsonObject.create();
            JsonArray tick_boss_guild = JsonArray.create();
            if(CouchBase.containKey("id_bg->" + Module + "::" + id_bg)){
                tick_boss_guild = CouchBase.get("id_bg->" + Module + "::" + id_bg).getArray("keys");
            }
            tick_boss_guild.add(Module + "::" + id);
            obj.put("keys", tick_boss_guild);

            CouchBase.set("id_bg->" + Module + "::" + id_bg, obj);
        }
        // Update count
        updateCount(Module, id);

        return id;
    }

    private static M_TickBossGuild get(String key){
        return (CouchBase.containKey(key)) ? new M_TickBossGuild(CouchBase.get(key)) : null;
    }

    public static M_TickBossGuild get(int id){
        return get(Module + "::" + id);
    }

    public static M_TickBossGuild get(int id_ac, int id_bg){
        if(CouchBase.containKey("id_ac&id_bg->" + Module + "::" + id_ac + "&" + id_bg)){
            String key = CouchBase.get("id_ac&id_bg->" + Module + "::" + id_ac + "&" + id_bg).getString("key");
            return get(key);
        }
        return null;
    }

    public static ArrayList<M_TickBossGuild> gets(int id_bg){
        ArrayList<M_TickBossGuild> result = new ArrayList<>();
        if(CouchBase.containKey("id_bg->" + Module + "::" + id_bg)){
            JsonArray tick = CouchBase.get("id_bg->" + Module + "::" + id_bg).getArray("keys");
            for (int i = 0; i < tick.size(); i++){
                result.add(get((String) tick.get(i)));
            }
        }
        return result;
    }
}
