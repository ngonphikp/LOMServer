package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Guild;
import Util.C_Util;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Guild extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_GUILD;

    public static void set(M_Guild data){
        if(CouchBase.containKey(Module + "::" + data.id)){
            JsonObject obj = CouchBase.get(Module + "::" + data.id)
                    .put(CmdDefine.ModuleGuild.NAME, data.name)
                    .put(CmdDefine.ModuleGuild.LV, data.lv)
                    .put(CmdDefine.ModuleGuild.NOTI, data.noti);
            CouchBase.set(Module + "::" + data.id, obj);
        }
    }

    public static int insert(String name){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Guild
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleGuild.ID, id)
                    .put(CmdDefine.ModuleGuild.NAME, name)
                    .put(CmdDefine.ModuleGuild.LV, 1)
                    .put(CmdDefine.ModuleGuild.NOTI, "Please add notice!");
            CouchBase.set(Module + "::" + id, obj);
        }
        // Update count
        updateCount(Module, id);
        return id;
    }

    public static void insertAccount(int id_guild, int id_ac){
        // Link 1Guild -> nAc
        C_Util.Link1_n(CmdDefine.ModuleGuild.ID, id_guild, CmdDefine.Module.MODULE_ACCOUNT, id_ac);
        // Link 1Ac -> 1Guild
        C_Util.Link1_1(CmdDefine.ModuleAccount.ID, id_ac, Module, id_guild);
    }

    public static void removeAccount(int id_guild, int id_ac){
        // UnLink 1Guild -> nAc: 1id_ac
        C_Util.UnLink1_n(CmdDefine.ModuleGuild.ID, id_guild, CmdDefine.Module.MODULE_ACCOUNT, id_ac);
        // UnLink 1Ac -> 1Guild
        C_Util.UnLink1_1(CmdDefine.ModuleAccount.ID, id_ac, Module);
    }

    public static String getKey(int id_ac){
        if(CouchBase.containKey(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac)){
            return CouchBase.get(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac).getString("key");
        }
        return null;
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
        return get(Module + "::" + id);
    }
}
