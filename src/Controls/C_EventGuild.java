package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_EventGuild;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.time.Instant;
import java.util.ArrayList;

public class C_EventGuild extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_EVENT_GUILD;

    public static int insert(String content, int id_guild){
        // Get Time
        Instant instant = Instant.now();
        long time = instant.getEpochSecond();

        // Get Count
        int id = getCount(Module) + 1;
        // Create Event Guild
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleEventGuild.ID, id)
                    .put(CmdDefine.ModuleEventGuild.TIME, time)
                    .put(CmdDefine.ModuleEventGuild.CONTENT, content);
            CouchBase.set(Module + "::" + id, obj);
        }

        // Link id_guild
        {
            JsonObject obj = JsonObject.create();
            JsonArray evts = JsonArray.create();
            if(CouchBase.containKey("id_guild->" + Module + "::" + id_guild)){
                evts = CouchBase.get("id_guild->" + Module + "::" + id_guild).getArray("keys");
            }
            evts.add(Module + "::" + id);
            obj.put("keys", evts);

            CouchBase.set("id_guild->" + Module + "::" + id_guild, obj);
        }

        // Update count
        updateCount(Module, id);
        return id;
    }

    private static M_EventGuild getByKey(String key){
        return (CouchBase.containKey(key)) ? new M_EventGuild(CouchBase.get(key)) : null;
    }

    public static ArrayList<M_EventGuild> getByIdGuild(int id_guild, int count){
        ArrayList<M_EventGuild> evts = new ArrayList<>();
        JsonArray arr = CouchBase.get("id_guild->" + Module + "::" + id_guild).getArray("keys");
        if(arr != null){
            for(int i = count; i < arr.size(); i++){
                evts.add(C_EventGuild.getByKey((String) arr.get(i)));
            }
        }
        return evts;
    }
}
