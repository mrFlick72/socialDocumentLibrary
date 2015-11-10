package it.valeriovaudi.documentlibrary.support;

import it.valeriovaudi.documentlibrary.model.Page;

/**
 * Created by Valerio on 30/04/2015.
 */
public abstract class PageSupport {

    private PageSupport() {
    }

    public static Integer fileNameToindex(String pageName){
        assert pageName!=null;
        String[] split = pageName.split("\\.");
        return Integer.valueOf(split[0].substring(4));
    }

    public static String fileName4Page(String bookPageNameFormat, int pageNumber, String bookPageFileFormat){
        return String.format(bookPageNameFormat, pageNumber, bookPageFileFormat);
    }

    public static Integer fileNameToindex(Page page){
        return fileNameToindex(page.getFileName());
    }
}
