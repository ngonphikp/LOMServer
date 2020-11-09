package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSArray;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSArray;
import com.smartfoxserver.v2.entities.data.SFSObject;

import java.util.ArrayList;


public class M_Guild {

    public int id;
    public String name;
    public int lv;
    public String noti;
    public int master;

    public ArrayList<M_Account> accounts = new ArrayList<>();

    public M_Guild(){}

    public M_Guild(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleGuild.ID);
        name = obj.getString(CmdDefine.ModuleGuild.NAME);
        lv = obj.getInt(CmdDefine.ModuleGuild.LV);
        master = obj.getInt(CmdDefine.ModuleGuild.MASTER);
        noti = obj.getString(CmdDefine.ModuleGuild.NOTI);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleGuild.ID, id);
        obj.putUtfString(CmdDefine.ModuleGuild.NAME, name);
        obj.putInt(CmdDefine.ModuleGuild.LV, lv);
        obj.putInt(CmdDefine.ModuleGuild.MASTER, master);
        obj.putUtfString(CmdDefine.ModuleGuild.NOTI, noti);
        ISFSArray arr = new SFSArray();
        for(int i = 0; i < accounts.size(); i++){
            arr.addSFSObject(accounts.get(i).parse());
        }
        obj.putSFSArray(CmdDefine.ModuleGuild.ACCOUNTS, arr);
        return  obj;
    }
}
