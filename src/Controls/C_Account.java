package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Account;
import Models.M_Guild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Account extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_ACCOUNT;

    public static void setJob(int id, int job, boolean resetDedi){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id);
            obj.put(CmdDefine.ModuleAccount.JOB, job);

            if(resetDedi){
                obj.put(CmdDefine.ModuleAccount.DEDIWEEK, 0);
                obj.put(CmdDefine.ModuleAccount.DEDITOTAL, 0);
            }

            CouchBase.set(Module + "::" + id, obj);
        }
    }

    public static void setName(int id, String name){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleAccount.NAME, name);
            CouchBase.set(Module + "::" + id, obj);
        }
    }

    public static int insert(String username, String password){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Account
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleAccount.ID, id)
                    .put(CmdDefine.ModuleAccount.USERNAME, username)
                    .put(CmdDefine.ModuleAccount.PASSWORD, password)
                    .put(CmdDefine.ModuleAccount.NAME, "")
                    .put(CmdDefine.ModuleAccount.LV, 1)
                    .put(CmdDefine.ModuleAccount.POWER, 0)
                    .put(CmdDefine.ModuleAccount.JOB, -1)
                    .put(CmdDefine.ModuleAccount.DEDITOTAL, 0)
                    .put(CmdDefine.ModuleAccount.DEDIWEEK, 0);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link username
        {
            JsonObject obj = JsonObject.create()
                    .put("key", Module + "::" + id);
            CouchBase.set("username->" + Module + "::" + username, obj);
        }
        // Update count
        updateCount(Module, id);

        return id;
    }

    public static M_Account get(String username, String password){
        if(CouchBase.containKey("username->" + Module + "::" + username)){
            String key = CouchBase.get("username->" + Module + "::" + username).getString("key");
            JsonObject obj = CouchBase.get(key);
            return (obj.getString(CmdDefine.ModuleAccount.PASSWORD).equals(password)) ? new M_Account(obj): null;
        }
        return null;
    }

    public static M_Account getByUserName(String username){
        if(CouchBase.containKey("username->" + Module + "::" + username)){
            String key = CouchBase.get("username->" + Module + "::" + username).getString("key");
            JsonObject obj = CouchBase.get(key);
            return new M_Account(obj);
        }
        return null;
    }

    private static M_Account getByKey(String key){
        return (CouchBase.containKey(key)) ? new M_Account(CouchBase.get(key)) : null;
    }

    public static M_Account get(int id){
        return getByKey(Module + "::" + id);
    }

    public static ArrayList<M_Account> getByIdGuild(int id_guild){
        ArrayList<M_Account> accounts = new ArrayList<>();
        JsonArray arr = CouchBase.get("id_guild->" + Module + "::" + id_guild).getArray("keys");
        if(arr != null){
            for(int i = 0; i < arr.size(); i++){
                accounts.add(getByKey((String) arr.get(i)));
            }
        }
        return accounts;
    }

    public static ArrayList<M_Account> getAll(){
        ArrayList<M_Account> result = new ArrayList<>();
        for(int i = 0; i <= getCount(Module); i++){
            M_Account account = get(i);
            if(account != null){
                result.add(account);
            }
        }
        return result;
    }
}
