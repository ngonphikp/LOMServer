package Util;

import Base.CouchBase;
import com.couchbase.client.java.document.json.JsonArray;
import com.couchbase.client.java.document.json.JsonObject;

import java.util.List;

public class C_Util {

    public static void Linkn_n(String name1, int id1, String module1, String name2, int id2, String module2, int id, String module){
        JsonObject obj = JsonObject.create()
                .put("key", module + "::" + id);
        CouchBase.set(name1 + "&" + name2 + "->" + module + "::" + id1 + "&" + id2, obj);

        C_Util.Link1_n(name2, id2, module, id);

        C_Util.Link1_1(module, id, module2, id2);

        C_Util.Link1_n(name1, id1, module, id);

        C_Util.Link1_1(module, id, module1, id1);
    }

    public static void Link1_n(String nameThis, Object valThis, String module, Object idThat){
        JsonObject obj = JsonObject.create();
        JsonArray array = JsonArray.create();
        if(CouchBase.containKey(nameThis + "->" + module + "::" + valThis)){
            array = CouchBase.get(nameThis + "->" + module + "::" + valThis).getArray("keys");
        }
        if(idThat != null) array.add(module + "::" + idThat);
        obj.put("keys", array);

        CouchBase.set(nameThis + "->" + module + "::" + valThis, obj);
    }

    public static void UnLink1_n(String nameThis, Object valThis, String module, Object idThat){
        if(CouchBase.containKey(nameThis + "->" + module + "::" + valThis)){
            JsonObject obj = JsonObject.create();
            JsonArray array = CouchBase.get(nameThis + "->" + module + "::" + valThis).getArray("keys");
            int find = -1;
            for(int i = 0; i < array.size(); i++){
                if(array.getString(i).equals(module + "::" + idThat)){
                    find = i;
                    break;
                }
            }
            if (find != -1) {
                List<Object> lst = array.toList();
                lst.remove(find);
                obj.put("keys", lst);
                CouchBase.set(nameThis + "->" + module + "::" + valThis, obj);
            }
        }
    }

    public static void Link1_1(String nameThis, Object valThis, String module, int idThat){
        if(valThis == null) return;
        JsonObject obj = JsonObject.create()
                .put("key", module + "::" + idThat);
        CouchBase.set(nameThis + "->" + module + "::" + valThis, obj);
    }

    public static void UnLink1_1(String nameThis, Object valThis, String module){
        if (CouchBase.containKey(nameThis + "->" + module + "::" + valThis))
            CouchBase.delete(nameThis + "->" + module + "::" + valThis);
    }

    public static int KeyToId(String module, String key){
        if(key != null){
            int length = module.length();
            return Integer.parseInt(key.substring(length + 2));
        }
        return -1;
    }
}