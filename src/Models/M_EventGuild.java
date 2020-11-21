package Models;

import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;
import com.smartfoxserver.v2.entities.data.ISFSObject;
import com.smartfoxserver.v2.entities.data.SFSObject;

public class M_EventGuild {

    public int id;
    public long time;
    public String content;

    public M_EventGuild(){}

    public M_EventGuild(JsonObject obj){
        id = obj.getInt(CmdDefine.ModuleEventGuild.ID);
        time = obj.getLong(CmdDefine.ModuleEventGuild.TIME);
        content = obj.getString(CmdDefine.ModuleEventGuild.CONTENT);
    }

    public ISFSObject parse(){
        ISFSObject obj = new SFSObject();
        obj.putInt(CmdDefine.ModuleEventGuild.ID, id);
        obj.putLong(CmdDefine.ModuleEventGuild.TIME, time);
        obj.putUtfString(CmdDefine.ModuleEventGuild.CONTENT, content);
        return  obj;
    }
}
