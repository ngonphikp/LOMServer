package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Account;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;

public class C_Account extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_ACCOUNT;

    public static void setName(int id, String name){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleAccount.NAME, name);
            CouchBase.set(Module + "::" + id, obj);
        }
        else {
            System.out.println("Không tồn tại tài khoản: " + id);
        }
    }

    public static void insert(String username, String password){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Account
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleAccount.ID, id)
                    .put(CmdDefine.ModuleAccount.USERNAME, username)
                    .put(CmdDefine.ModuleAccount.PASSWORD, password)
                    .put(CmdDefine.ModuleAccount.NAME, "");
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
    }

    public static M_Account get(String username, String password){
        if(CouchBase.containKey(CmdDefine.ModuleAccount.USERNAME + "->" + Module + "::" + username)){
            String key = CouchBase.get(CmdDefine.ModuleAccount.USERNAME + "->" + Module + "::" + username).getString("key");
            JsonObject obj = CouchBase.get(key);
            return (obj.getString(CmdDefine.ModuleAccount.PASSWORD).equals(password)) ? new M_Account(obj): null;
        }
        return null;
    }

    public static M_Account getByUserName(String username){
        if(CouchBase.containKey(CmdDefine.ModuleAccount.USERNAME + "->" + Module + "::" + username)){
            String key = CouchBase.get(CmdDefine.ModuleAccount.USERNAME + "->" + Module + "::" + username).getString("key");
            JsonObject obj = CouchBase.get(key);
            return new M_Account(obj);
        }
        return null;
    }

    public static M_Account getByKey(String key){
        return (CouchBase.containKey(key)) ? new M_Account(CouchBase.get(key)) : null;
    }

    public static M_Account getByID(int id){
        return getByKey(Module + "::" + id);
    }
}
