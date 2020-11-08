package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_Account {
    public int id;
    public String username;
    public String password;
    public String name;

    public M_Account(){}

    public M_Account(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleAccount.ID);
        username = obj.getString(CmdDefine.ModuleAccount.USERNAME);
        password = obj.getString(CmdDefine.ModuleAccount.PASSWORD);
        name = obj.getString(CmdDefine.ModuleAccount.NAME);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleAccount.ID, id);
        obj.putUtfString(CmdDefine.ModuleAccount.USERNAME, username);
        obj.putUtfString(CmdDefine.ModuleAccount.PASSWORD, password);
        obj.putUtfString(CmdDefine.ModuleAccount.NAME, name);
        return  obj;
    }
}
