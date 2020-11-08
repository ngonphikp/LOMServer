package Controls;

import Base.BaseControl;
import Base.CouchBase;
import Models.M_Character;
import Util.CmdDefine;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.ArrayList;

public class C_Character extends BaseControl {

    private static String Module = CmdDefine.Module.MODULE_CHARACTER;

    public static void setLv(int id, int lv){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleCharacter.LV, lv);
            CouchBase.set(Module + "::" + id, obj);
        }
        else {
            System.out.println("Không tồn tại tài khoản: " + id);
        }
    }

    public static void setIdx(int id, int idx){
        if(CouchBase.containKey(Module + "::" + id)){
            JsonObject obj = CouchBase.get(Module + "::" + id)
                    .put(CmdDefine.ModuleCharacter.IDX, idx);
            CouchBase.set(Module + "::" + id, obj);
        }
        else {
            System.out.println("Không tồn tại tài khoản: " + id);
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
        // Link id_ac
        {
            JsonObject obj = JsonObject.create();
            JsonArray characters = JsonArray.create();
            if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
                characters = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
            }
            characters.add(Module + "::" + id);
            obj.put("keys", characters);

            CouchBase.set("id_ac->" + Module + "::" + id_ac, obj);
        }
        // Update count
        updateCount(Module, id);

        return id;
    }

    public static ArrayList<M_Character> gets(int id_ac){
        ArrayList<M_Character> result = new ArrayList<>();
        if(CouchBase.containKey("id_ac->" + Module + "::" + id_ac)){
            JsonArray characters = CouchBase.get("id_ac->" + Module + "::" + id_ac).getArray("keys");
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
