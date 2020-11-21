package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Guild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Guild extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_GUILD;

    public static void setMaster(int id, int master){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleGuild.MASTER, master);
            CouchBase.set(Module + "::" + id, obj);
        }
    }

    public static void setNoti(int id, String noti){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleGuild.NOTI, noti);
            CouchBase.set(Module + "::" + id, obj);
        }
    }

    public static int insert(String name, int master){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Guild
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleGuild.ID, id)
                    .put(CmdDefine.ModuleGuild.NAME, name)
                    .put(CmdDefine.ModuleGuild.MASTER, master)
                    .put(CmdDefine.ModuleGuild.LV, 1)
                    .put(CmdDefine.ModuleGuild.NOTI, "Please add notice!");
            CouchBase.set(Module + "::" + id, obj);
        }

        linkAccounts(id, master);
        lindId_ac(id, master);

        // Update count
        updateCount(Module, id);
        return id;
    }

    public static void insertAccount(int id_guild, int id_ac){
        linkAccounts(id_guild, id_ac);
        lindId_ac(id_guild, id_ac);
    }

    public static void deleteAccount(int id_guild, int id_ac){
        // UnLinkAccounts
        JsonObject obj = JsonObject.create();
        JsonArray accounts = JsonArray.create();
        if(CouchBase.containKey("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild)){
            JsonArray old = CouchBase.get("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild).getArray("keys");
            for(int i = 0; i < old.size(); i++){
                if(!old.getString(i).equals(CmdDefine.Module.MODULE_ACCOUNT + "::" + id_ac)){
                    accounts.add(old.getString(i));
                }
            }
        }
        obj.put("keys", accounts);
        CouchBase.set("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild, obj);

        // UnLinkId_ac
        CouchBase.delete("id_ac->" + Module + "::" + id_ac);
    }

    private static void linkAccounts(int id_guild, int id_ac){
        JsonObject obj = JsonObject.create();
        JsonArray accounts = JsonArray.create();
        if(CouchBase.containKey("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild)){
            accounts = CouchBase.get("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild).getArray("keys");
        }
        accounts.add(CmdDefine.Module.MODULE_ACCOUNT + "::" + id_ac);
        obj.put("keys", accounts);

        CouchBase.set("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id_guild, obj);
    }

    private static void lindId_ac(int id_guild, int id_ac){
        JsonObject obj = JsonObject.create()
                .put("key", Module + "::" + id_guild);
        CouchBase.set("id_ac->" + Module + "::" + id_ac, obj);
    }

    public static ArrayList<M_Guild> getAll(){
        ArrayList<M_Guild> result = new ArrayList<>();
        for(int i = 0; i <= getCount(Module); i++){
            M_Guild guild = get(i, true);
            if(guild != null){
                result.add(guild);
            }
        }
        return result;
    }

    private static M_Guild get(String key){
        return (CouchBase.containKey(key)) ? new M_Guild(CouchBase.get(key)) : null;
    }

    public static M_Guild get(int id, boolean isGetAccs){
        M_Guild guild = get(Module + "::" + id);
        if(guild != null && isGetAccs){
            guild.accounts = C_Account.getByIdGuild(id);
        }
        return guild;
    }

    public static String getKey(int id_ac){
        if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
            return CouchBase.get("id_ac->" + Module + "::" + id_ac).getString("key");
        }
        return null;
    }
}
