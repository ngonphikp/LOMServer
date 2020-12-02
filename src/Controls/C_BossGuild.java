package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_BossGuild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_BossGuild extends BaseControl {
    private static String Module = CmdDefine.Module.MODULE_BOSS_GUILD;

    public static void set(M_BossGuild data){
        if(CouchBase.containKey(Module + "::" + data.id)){
            JsonObject obj = CouchBase.get(Module + "::" + data.id);
            obj.put(CmdDefine.ModuleBossGuild.STATUS, data.status);
            obj.put(CmdDefine.ModuleBossGuild.CUR_HP, data.cur_hp);
            CouchBase.set(Module + "::" + data.id, obj);
        }
    }

    public static ArrayList<M_BossGuild> gets(int id_guild){
        ArrayList<M_BossGuild> result = new ArrayList<>();
        if(CouchBase.containKey("id_guild->" + Module + "::" + id_guild)){
            JsonArray boss_guilds = CouchBase.get("id_guild->" + Module + "::" + id_guild).getArray("keys");
            for (int i = 0; i < boss_guilds.size(); i++){
                result.add(get(boss_guilds.getString(i)));
            }
        }
        return result;
    }

    public static void insert(int id_guild, int id_boss, int status, int cur_hp){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Boss Guild
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleBossGuild.ID, id)
                    .put(CmdDefine.ModuleGuild.ID, id_guild)
                    .put(CmdDefine.ModuleBossGuild.ID_BOSS, id_boss)
                    .put(CmdDefine.ModuleBossGuild.STATUS, status)
                    .put(CmdDefine.ModuleBossGuild.CUR_HP, cur_hp);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link id_guild
        {
            {
                JsonObject obj = JsonObject.create();
                JsonArray boss_guild = JsonArray.create();
                if(CouchBase.containKey("id_guild->" + Module + "::" + id_guild)){
                    boss_guild = CouchBase.get("id_guild->" + Module + "::" + id_guild).getArray("keys");
                }
                boss_guild.add(Module + "::" + id);
                obj.put("keys", boss_guild);

                CouchBase.set("id_guild->" + Module + "::" + id_guild, obj);
            }
        }
        // Update count
        updateCount(Module, id);
    }

    private static M_BossGuild get(String key){
        return (CouchBase.containKey(key)) ? new M_BossGuild(CouchBase.get(key)) : null;
    }

    public static M_BossGuild get(int id){
        return get(Module + "::" + id);
    }
}
