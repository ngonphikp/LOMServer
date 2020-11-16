package Util;

public class C_Util {

    public static int KeyToId(String module, String key){
        if(key != null){
            int length = module.length();
            return Integer.parseInt(key.substring(length + 2));
        }
        return -1;
    }
}