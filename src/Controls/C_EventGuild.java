package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_EventGuild;
import Util.C_Util;
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

        // Link 1Guild -> nEvt
        C_Util.Link1_n(CmdDefine.ModuleGuild.ID, id_guild, Module, id);
        // Link 1Evt -> 1Guild
        C_Util.Link1_1(CmdDefine.ModuleEventGuild.ID, id, CmdDefine.Module.MODULE_GUILD, id_guild);

        // Update count
        updateCount(Module, id);
        return id;
    }

    public static ArrayList<M_EventGuild> getByIdGuild(int id_guild, int count){
        ArrayList<M_EventGuild> evts = new ArrayList<>();
        JsonArray arr = CouchBase.get(CmdDefine.ModuleGuild.ID + "->" + Module + "::" + id_guild).getArray("keys");
        if(arr != null){
            for(int i = count; i < arr.size(); i++){
                evts.add(C_EventGuild.get((String) arr.get(i)));
            }
        }
        return evts;
    }

    private static M_EventGuild get(String key){
        return (CouchBase.containKey(key)) ? new M_EventGuild(CouchBase.get(key)) : null;
    }
}
