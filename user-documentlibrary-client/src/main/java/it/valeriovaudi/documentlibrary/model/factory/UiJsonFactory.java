package it.valeriovaudi.documentlibrary.model.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Valerio on 26/02/2016.
 */
public class UiJsonFactory {

    private Map<String,String> instance = new HashMap<>();
    public static final String EMPTY_JOSON_OBJECT ="{}";
    private UiJsonFactory(){}

    private void setInstance(Map<String, String> instance) {
        this.instance = instance;
    }

    public static UiJsonFactory newUiJsonFactory(Map<String,String> instance){
        UiJsonFactory uiJsonFactory = new UiJsonFactory();
        uiJsonFactory.setInstance(instance);
        return uiJsonFactory;
    }

    public static UiJsonFactory newUiJsonFactory(){
        UiJsonFactory uiJsonFactory = new UiJsonFactory();
        return uiJsonFactory;
    }

    private void setMapValue(String key, String value){
        if(value!=null){
            instance.put(key,value);
        }
    }

    public UiJsonFactory putProperty(String key, String value){
        setMapValue(key, value);
        return this;
    }

    public UiJsonFactory removeProperty(String key){
        instance.remove(key);
        return this;
    }

    public UiJsonFactory trasformPropertyKey(String previousKey, String newKey){
        String value;
        if(instance.containsKey(previousKey)){
            value = instance.get(previousKey);
            instance.remove(previousKey);
            setMapValue(newKey, value);
        }

        return this;
    }

    public UiJsonFactory trasformProperty(String previousKey, String newKey,String value){
        if(instance.containsKey(previousKey)){
            instance.remove(previousKey);
            setMapValue(newKey, value);
        }

        return this;
    }

    public UiJsonFactory trasformPropertyValue(String key, String newValue){
        setMapValue(key, newValue);
        return this;
    }

    public Map build(){
        return instance;
    }
}
