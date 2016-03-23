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

    public static String getValueFromJson(JsonObject currentJsonObject,String key){
        String result = "";
        if(currentJsonObject.containsKey(key) ){
            switch (currentJsonObject.get(key).getValueType()){
                case STRING:
                    result = currentJsonObject.getString(key);
                    break;
                case NUMBER:
                    result = String.valueOf(currentJsonObject.getInt(key));
                    break;
                case NULL:
                    result = "";
                    break;

            }
        }
        return result;
    }
}
