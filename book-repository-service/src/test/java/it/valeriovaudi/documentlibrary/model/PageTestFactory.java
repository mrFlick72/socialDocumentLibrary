package it.valeriovaudi.documentlibrary.model;

import it.valeriovaudi.documentlibrary.builder.PageBuilder;

import java.util.UUID;

/**
 * Created by Valerio on 30/04/2015.
 */
public class PageTestFactory {

    public static Page createPage(String pageName){
        return PageBuilder.newPageBuilder()
                .fileName(pageName)
                .id(UUID.randomUUID().toString())
                .bytes(new byte[]{})
                .build();
    }
}
