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

    public int lv = 1;
    public int power = 0;

    public int job = -1;
    public int dediTotal = 0;
    public int dediWeek = 0;

    public M_Account(){}

    public M_Account(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleAccount.ID);
        username = obj.getString(CmdDefine.ModuleAccount.USERNAME);
        password = obj.getString(CmdDefine.ModuleAccount.PASSWORD);

        name = obj.getString(CmdDefine.ModuleAccount.NAME);

        lv = obj.getInt(CmdDefine.ModuleAccount.LV);
        power = obj.getInt(CmdDefine.ModuleAccount.POWER);

        job = obj.getInt(CmdDefine.ModuleAccount.JOB);
        dediTotal = obj.getInt(CmdDefine.ModuleAccount.DEDITOTAL);
        dediWeek = obj.getInt(CmdDefine.ModuleAccount.DEDIWEEK);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleAccount.ID, id);
        obj.putUtfString(CmdDefine.ModuleAccount.USERNAME, username);
        obj.putUtfString(CmdDefine.ModuleAccount.PASSWORD, password);
        obj.putUtfString(CmdDefine.ModuleAccount.NAME, name);
        obj.putInt(CmdDefine.ModuleAccount.LV, lv);
        obj.putInt(CmdDefine.ModuleAccount.POWER, power);
        obj.putInt(CmdDefine.ModuleAccount.JOB, job);
        obj.putInt(CmdDefine.ModuleAccount.DEDITOTAL, dediTotal);
        obj.putInt(CmdDefine.ModuleAccount.DEDIWEEK, dediWeek);
        return  obj;
    }
}
