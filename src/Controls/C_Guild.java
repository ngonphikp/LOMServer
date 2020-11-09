package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Account;
import Models.M_Guild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Guild extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_GUILD;

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
        // Link account master
        {
            JsonObject obj = JsonObject.create();
            JsonArray accounts = JsonArray.create();
            if(CouchBase.containKey("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id)){
                accounts = CouchBase.get("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id).getArray("keys");
            }
            accounts.add(CmdDefine.Module.MODULE_ACCOUNT + "::" + master);
            obj.put("keys", accounts);

            CouchBase.set("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id, obj);
        }

        // Update count
        updateCount(Module, id);
        return id;
    }

    public static ArrayList<M_Guild> getAll(){
        ArrayList<M_Guild> result = new ArrayList<>();
        for(int i = 0; i <= getCount(Module); i++){
            M_Guild guild = get(i);
            if(guild != null){
                result.add(guild);
            }
        }
        return result;
    }

    private static M_Guild get(String key){
        return (CouchBase.containKey(key)) ? new M_Guild(CouchBase.get(key)) : null;
    }

    public static M_Guild get(int id){
        M_Guild guild = get(Module + "::" + id);
        if(guild != null){
            guild.accounts = getAccounts(id);
        }
        return guild;
    }

    private static ArrayList<M_Account> getAccounts(int id){
        ArrayList<M_Account> accounts = new ArrayList<>();
        JsonArray arr = CouchBase.get("id_guild->" + CmdDefine.Module.MODULE_ACCOUNT + "::" + id).getArray("keys");
        if(arr != null){
            for(int i = 0; i < arr.size(); i++){
                accounts.add(C_Account.getByKey((String) arr.get(i)));
            }
        }
        return accounts;
    }
}
