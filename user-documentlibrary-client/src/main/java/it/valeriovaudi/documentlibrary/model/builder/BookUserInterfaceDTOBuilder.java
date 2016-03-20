package it.valeriovaudi.documentlibrary.model.builder;

import it.valeriovaudi.documentlibrary.utility.JsonUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static it.valeriovaudi.documentlibrary.utility.JsonUtility.getValueFromJson;
import static it.valeriovaudi.documentlibrary.utility.JsonUtility.getValueFromMap;

/**
 * Created by Valerio on 24/06/2015.
 */
public class BookUserInterfaceDTOBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookUserInterfaceDTOBuilder.class);

    private JsonObjectBuilder jsonObjectBuilder;

    private void setJsonObjectBuilder(JsonObjectBuilder jsonObjectBuilder) {
        this.jsonObjectBuilder = jsonObjectBuilder;
    }

    public static BookUserInterfaceDTOBuilder newBookUserInterfaceDTOBuilder(){
        BookUserInterfaceDTOBuilder bookUserInterfaceDTOBuilder = new BookUserInterfaceDTOBuilder();
        bookUserInterfaceDTOBuilder.setJsonObjectBuilder(Json.createObjectBuilder());

        return bookUserInterfaceDTOBuilder;
    }

    public BookUserInterfaceDTOBuilder bookName(String bookRepositoryJsonString){
        JsonObject bookRepositoryJson = Json.createReader(new StringReader(bookRepositoryJsonString)).readObject();
        jsonObjectBuilder.add("bookName", bookRepositoryJson.getString("name"));
        return this;
    }

    public BookUserInterfaceDTOBuilder bookId(String bookRepositoryJsonString){
        JsonObject bookRepositoryJson = Json.createReader(new StringReader(bookRepositoryJsonString)).readObject();
        jsonObjectBuilder.add("bookId", bookRepositoryJson.getString("id"));
        return this;
    }

    public BookUserInterfaceDTOBuilder thumbnail(String bookRepositoryJsonString){
        JsonObject bookRepositoryJson = Json.createReader(new StringReader(bookRepositoryJsonString)).readObject();
        String thumbnail = "";
        try{
            thumbnail = bookRepositoryJson.getJsonObject("pageId").getJsonObject("1").getJsonArray("links").getJsonObject(0).getString("href");
        } catch (NullPointerException e){
            LOGGER.info(e.getMessage());
        }

        jsonObjectBuilder.add("thumbnail", thumbnail);
        return this;
    }


    public BookUserInterfaceDTOBuilder feedBack(String feedBackJsonString){
        JsonArray bookRepositoryJson = Json.createReader(new StringReader(feedBackJsonString)).readArray();
        Optional.of(SecurityContextHolder.getContext().getAuthentication()).ifPresent(authentication -> {
            String userName = authentication.getName();
            Map<String, String> reduce = bookRepositoryJson.parallelStream().filter(jsonValue -> {
                JsonObject jsonObject = (JsonObject) jsonValue;
                return jsonObject.getString("userName").equals(userName);
            }).map(jsonValueAux -> {
                Map<String, String> map = new HashMap<>();
                JsonObject jsonObjectAux = (JsonObject) jsonValueAux;
                map.put("id", getValueFromJson(jsonObjectAux, "id"));
                map.put("bookId", getValueFromJson(jsonObjectAux, "bookId"));
                map.put("userName", getValueFromJson(jsonObjectAux, "userName"));
                map.put("feedbackTitle", getValueFromJson(jsonObjectAux, "feedbackTitle"));
                map.put("feedbackBody", getValueFromJson(jsonObjectAux, "feedbackBody"));
                map.put("score", getValueFromJson(jsonObjectAux, "score"));

                String scoreAux = getValueFromJson(jsonObjectAux, "score");
                if (!scoreAux.equals("")) {
                    map.put("scoreCount", scoreAux);
                }
                return map;
            })
                    .sequential()
                    .reduce(new HashMap<>(), (op1, op2) -> {
                        String[] keySet = new String[]{"id", "bookId", "userName", "feedbackTitle", "feedbackBody", "score"};

                        if (op2.keySet().contains("userName")) {
                            // when find the map with the user information I copy the information in the final map
                            for (String key : keySet) {
                                if (op2.keySet().contains(key)) {
                                    op1.put(key, op2.get(key));
                                }
                            }
                        }

                        // count the score amount
                        String op2Score = op2.get("scoreCount");
                        String op1Score = op1.get(op2Score);

                        if (op1Score == null) {
                            op1.put(op2Score, "1");
                        } else {
                            op1.remove(op2Score);
                            op1.put(op2Score, String.valueOf(Integer.parseInt(op1Score) + 1));
                        }
                        return op1;
                    });

            jsonObjectBuilder.add("feedback", Json.createObjectBuilder()
                    .add("id", getValueFromMap(reduce, "id"))
                    .add("score", getValueFromMap(reduce, "score"))
                    .add("feedbackTitle", getValueFromMap(reduce, "feedbackTitle"))
                    .add("feedbackBody", getValueFromMap(reduce, "feedbackBody"))
                    .add("scores", Json.createObjectBuilder()
                            .add("5", getValueFromMap(reduce, "5"))
                            .add("4", getValueFromMap(reduce, "4"))
                            .add("3", getValueFromMap(reduce, "3"))
                            .add("2", getValueFromMap(reduce, "2"))
                            .add("1", getValueFromMap(reduce, "1"))));
        });

        return this;
    }

    public BookUserInterfaceDTOBuilder description(String bookRepositoryJsonString){
        String descrizioneKey = "description";
        JsonObject bookRepositoryJson = Json.createReader(new StringReader(bookRepositoryJsonString)).readObject();
        jsonObjectBuilder.add(descrizioneKey, getValueFromJson(bookRepositoryJson, descrizioneKey));
        return this;
    }

    public BookUserInterfaceDTOBuilder userFeedBacks(String userFeedBackJsonString){
        String userFeedBacksKey = "userFeedBacks";
        jsonObjectBuilder.add(userFeedBacksKey, Json.createReader(new StringReader(userFeedBackJsonString)).readArray());
        return this;
    }

    public JsonObject buildJson(){
        return jsonObjectBuilder.build();
    }

    public String buildJsonString(){
        return jsonObjectBuilder.build().toString();
    }

}
