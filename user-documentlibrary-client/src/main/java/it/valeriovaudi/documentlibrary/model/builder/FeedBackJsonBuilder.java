package it.valeriovaudi.documentlibrary.model.builder;

import it.valeriovaudi.documentlibrary.utility.JsonUtility;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.StringReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Valerio on 23/03/2016.
 */
public class FeedBackJsonBuilder {

    private JsonObjectBuilder jsonObjectBuilder;
    private JsonObject master;
    private Set<String> actualFilds = new HashSet<>();
    private Map<String,String> frontEndKeysMap;
    private String[] avaiableKeys = new String[] {"id","bookId","userName","score","feedbackTitle","feedbackBody","firstNameAndLastName"};

    public FeedBackJsonBuilder() {
        frontEndKeysMap = new HashMap<>();

        frontEndKeysMap.put("id","id");
        frontEndKeysMap.put("bookId","bookId");
        frontEndKeysMap.put("userName","userName");
        frontEndKeysMap.put("score","score");
        frontEndKeysMap.put("feedbackTitle","feedbackTitle");
        frontEndKeysMap.put("feedbackBody","feedbackBody");
        frontEndKeysMap.put("firstNameAndLastName","firstNameAndLastName");
    }

    private void setJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder) {
        this.jsonObjectBuilder = jsonObjectBuilder;
    }

    private void setMaster(JsonObject master) {
        this.master = master;
    }

    public static FeedBackJsonBuilder newFeedBackJsonBuilder(JsonObject master){
        FeedBackJsonBuilder jsonObjectBuilder = new FeedBackJsonBuilder();
        jsonObjectBuilder.setJsonObjectBuilder(Json.createObjectBuilder());

        jsonObjectBuilder.setMaster(master);

        return jsonObjectBuilder;
    }

    public static FeedBackJsonBuilder newFeedBackJsonBuilder(String body){
        FeedBackJsonBuilder jsonObjectBuilder = new FeedBackJsonBuilder();
        jsonObjectBuilder.setJsonObjectBuilder(Json.createObjectBuilder());

        jsonObjectBuilder.setMaster(Json.createReader(new StringReader(body)).readObject());

        return jsonObjectBuilder;
    }

    public FeedBackJsonBuilder id(String id){
        jsonObjectBuilder.add("id", id);
        actualFilds.add("id");
        return this;
    }

    public FeedBackJsonBuilder bookId(String bookId){
        jsonObjectBuilder.add("bookId",bookId);
        actualFilds.add("bookId");
        return this;
    }

    public FeedBackJsonBuilder userName(String userName){
        jsonObjectBuilder.add("userName", userName);
        actualFilds.add("userName");
        return this;
    }

    public FeedBackJsonBuilder score(String score){
        jsonObjectBuilder.add("score",score);
        actualFilds.add("score");
        return this;
    }

    public FeedBackJsonBuilder feadbackTitle(String feadbackTitle){
        jsonObjectBuilder.add("feedbackTitle", feadbackTitle);
        actualFilds.add("feedbackTitle");
        return this;
    }

    public FeedBackJsonBuilder feadbackBody(String feadbackBody){
        jsonObjectBuilder.add("feedbackBody",feadbackBody);
        actualFilds.add("feedbackBody");
        return this;
    }

    public FeedBackJsonBuilder userFirstNameAndLastName(String firstName,String lastName){
        jsonObjectBuilder.add("firstNameAndLastName",String.format("%s %s",firstName, lastName));
        actualFilds.add("firstNameAndLastName");
        return this;
    }

    public JsonObject buildJson(){
        for (String key : avaiableKeys) {
            if(!actualFilds.contains(key)){
                if(!String.valueOf(JsonUtility.getValueFromJson(master, key)).trim().equals("")){
                    jsonObjectBuilder.add(frontEndKeysMap.get(key),JsonUtility.getValueFromJson(master,key));
                }
            }
        }
        return jsonObjectBuilder.build();
    }
}
