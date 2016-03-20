package it.valeriovaudi.documentlibrary.utility;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.StringReader;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Valerio on 27/02/2016.
 */
public class JsonUtility {

    private JsonUtility(){

    }
    public static String getValueFromJson(JsonObject currentJsonObject,String key){
//        String result = "";
        return Optional.ofNullable(currentJsonObject)
                .filter(jsonObject -> jsonObject!=null)
                .filter(jsonObjectAux -> jsonObjectAux.containsKey(key))
                .map(jsonObjectAux -> jsonObjectAux.get(key))
                .map(jsonValue ->
                     jsonValue.getValueType().compareTo(JsonValue.ValueType.NUMBER) == 0 ?
                             String.valueOf(currentJsonObject.getInt(key)) :
                             currentJsonObject.getString(key)
                ).orElse("");

 /*       if(!currentJsonObject.isNull(key)){
            JsonValue jsonValue = currentJsonObject.get(key);
            if(jsonValue.getValueType().compareTo(JsonValue.ValueType.NUMBER) == 0){
                result = String.valueOf(currentJsonObject.getInt(key));
            } else {
                result = currentJsonObject.getString(key);
            }
        }*/

//        return result;
    }

    public static String getValueFromMap(Map<String,String> map,String key){
        return Optional.ofNullable(map.get(key)).orElse("");
    }

    public static String getValueFromJsonString(String jsonString, String key){
        return Optional.ofNullable(jsonString)
                .filter(stage1 -> !stage1.trim().equals(""))
                .map(stage2 -> Json.createReader(new StringReader(stage2)).readObject())
                .filter(jsonObject -> jsonObject.containsKey(key))
                .map(jsonObjectAux -> jsonObjectAux.getString(key))
                .orElse("");
    }
}
