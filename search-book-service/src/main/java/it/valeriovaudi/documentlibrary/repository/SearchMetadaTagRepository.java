package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.model.SearchMetadaTag;

/**
 * Created by Valerio on 05/06/2015.
 */
public interface SearchMetadaTagRepository {

    SearchMetadaTag addSearchMetadata(String searchMetadata);
    SearchMetadaTag removeSearchMetadata(String searchMetadata);

    SearchMetadaTag getSearchMetadaTag();

    boolean containsSearchMetadaTag(String tag);
}
