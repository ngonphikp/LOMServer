package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Account;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class C_Account extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_ACCOUNT;

    public static void set(M_Account data){
        if(CouchBase.containKey(Module + "::" + data.id)){
            JsonObject obj = CouchBase.get(Module + "::" + data.id)
                    .put(CmdDefine.ModuleAccount.USERNAME, data.username)
                    .put(CmdDefine.ModuleAccount.PASSWORD, data.password)
                    .put(CmdDefine.ModuleAccount.NAME, data.name)
                    .put(CmdDefine.ModuleAccount.LV, data.lv)
                    .put(CmdDefine.ModuleAccount.POWER, data.power)
                    .put(CmdDefine.ModuleAccount.JOB, data.job)
                    .put(CmdDefine.ModuleAccount.DEDITOTAL, data.dediTotal)
                    .put(CmdDefine.ModuleAccount.DEDIWEEK, data.dediWeek);
            CouchBase.set(Module + "::" + data.id, obj);
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

    public static void insertFriends(int id_ac_A, int id_ac_B){
        JsonObject obj = JsonObject.create();
        JsonArray friends = JsonArray.create();
        if(CouchBase.containKey("id_ac->friends::" + id_ac_A)){
            friends = CouchBase.get("id_ac->friends::" + id_ac_A).getArray("keys");
        }
        friends.add(Module + "::" + id_ac_B);
        obj.put("keys", friends);

        CouchBase.set("id_ac->friends::" + id_ac_A, obj);
    }

    public static void removeFriends(int id_ac_A, int id_ac_B){
        if(CouchBase.containKey("id_ac->friends::" + id_ac_A)){
            JsonObject obj = JsonObject.create();
            JsonArray friends = CouchBase.get("id_ac->friends::" + id_ac_A).getArray("keys");
            int find = -1;
            for (int i = 0; i < friends.size(); i++) {
                if(friends.getString(i).equals(Module + "::" + id_ac_B)) {
                    find = i;
                    break;
                }
            }
            if (find != -1) {
                List<Object> lst = friends.toList();
                lst.remove(find);
                obj.put("keys", lst);
                CouchBase.set("id_ac->friends::" + id_ac_A, obj);
            }
        }
    }
    
    public static boolean checkFriend(int id_ac_A, int id_ac_B){
        if(CouchBase.containKey("id_ac->friends::" + id_ac_A)){
            JsonArray friends = CouchBase.get("id_ac->friends::" + id_ac_A).getArray("keys");
            for (int i = 0; i < friends.size(); i++) {
                String keyAc = friends.getString(i);
                if(keyAc.equals(Module + "::" + id_ac_B)) return true;
            }
        }
        return false;
    }

    public static ArrayList<M_Account> getFriends(int id){
        ArrayList<M_Account> accounts = new ArrayList<>();
        if(CouchBase.containKey("id_ac->friends::" + id)){
            JsonArray arr = CouchBase.get("id_ac->friends::" + id).getArray("keys");
            if(arr != null){
                for(int i = 0; i < arr.size(); i++){
                    accounts.add(getByKey((String) arr.get(i)));
                }
            }
        }
        return accounts;
    }
}
