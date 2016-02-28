package it.valeriovaudi.documentlibrary.utility;

import javax.json.JsonObject;
import javax.json.JsonValue;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Valerio on 27/02/2016.
 */
public class JsonUtility {

    private JsonUtility(){

    }
    public static String getValueFromJson(JsonObject currentJsonObject,String key){
        String result = "";
        if(!currentJsonObject.isNull(key)){
            JsonValue jsonValue = currentJsonObject.get(key);
            if(jsonValue.getValueType().compareTo(JsonValue.ValueType.NUMBER) == 0){
                result = String.valueOf(currentJsonObject.getInt(key));
            } else {
                result = currentJsonObject.getString(key);
            }
        }

        return result;
    }

    public static String getValueFromMap(Map<String,String> map,String key){
        return Optional.ofNullable(map.get(key)).orElse("");
    }
}
