package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Character;
import Util.C_Util;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Character extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_CHARACTER;

    public static void set(M_Character data){
        if(CouchBase.containKey(Module + "::" + data.id)){
            JsonObject obj = CouchBase.get(Module + "::" + data.id)
                    .put(CmdDefine.ModuleCharacter.ID_CFG, data.id_cfg)
                    .put(CmdDefine.ModuleCharacter.LV, data.lv)
                    .put(CmdDefine.ModuleCharacter.IDX, data.idx);
            CouchBase.set(Module + "::" + data.id, obj);
        }
    }

    public static int insert(int id_ac, String id_cfg, int lv, int idx){
        // Get Count
        int id = getCount(Module) + 1;
        // Create Character
        {
            JsonObject obj = JsonObject.create()
                    .put(CmdDefine.ModuleCharacter.ID, id)
                    .put(CmdDefine.ModuleCharacter.ID_CFG, id_cfg)
                    .put(CmdDefine.ModuleCharacter.LV, lv)
                    .put(CmdDefine.ModuleCharacter.IDX, idx);
            CouchBase.set(Module + "::" + id, obj);
        }
        // Link 1Ac -> nChar
        C_Util.Link1_n(CmdDefine.ModuleAccount.ID, id_ac, Module, id);
        // Link 1Char -> 1Ac
        C_Util.Link1_1(CmdDefine.ModuleCharacter.ID, id, CmdDefine.Module.MODULE_ACCOUNT, id_ac);
        // Update count
        updateCount(Module, id);

        return id;
    }

    public static ArrayList<M_Character> gets(int id_ac){
        ArrayList<M_Character> result = new ArrayList<>();
        if(CouchBase.containKey(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac)){
            JsonArray characters = CouchBase.get(CmdDefine.ModuleAccount.ID + "->" + Module + "::" + id_ac).getArray("keys");
            for (int i = 0; i < characters.size(); i++){
                result.add(get((String) characters.get(i)));
            }
        }
        return result;
    }

    private static M_Character get(String key){
        return (CouchBase.containKey(key)) ? new M_Character(CouchBase.get(key)) : null;
    }

    public static M_Character get(int id){
        return get(Module + "::" + id);
    }
}
