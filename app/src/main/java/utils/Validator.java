package utils;

/**
 * Created by kenn on 7/7/2017.
 */

public class Validator {
    public static boolean isBlankOrNull(String str){
        if(str == null){
            return  true;
        } else if (str.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
