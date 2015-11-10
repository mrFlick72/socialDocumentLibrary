package it.valeriovaudi.documentlibrary.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Valerio on 05/06/2015.
 */
@Document(collection = "SearchMetadaTagCollection")
public class SearchMetadaTag {

    @Id
    @JsonIgnore
    private String id;
    private List<String> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMetadata() {
        return metadata;
    }

    public void setMetadata(List<String> metadata) {
        this.metadata = metadata;
    }
}
