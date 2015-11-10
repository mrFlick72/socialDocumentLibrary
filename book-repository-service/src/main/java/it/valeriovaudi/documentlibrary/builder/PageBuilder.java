package it.valeriovaudi.documentlibrary.builder;

import it.valeriovaudi.documentlibrary.model.Page;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Valerio on 30/04/2015.
 */
public class PageBuilder {
    private static final Logger LOGGER = Logger.getLogger(PageBuilder.class);

    private Page page;

    private PageBuilder(){
        this.page = new Page();
    }

    public static PageBuilder newPageBuilder(){
        return new PageBuilder();
    }

    public PageBuilder id(String id){
        this.page.setId(id);
        return this;
    }

    public PageBuilder fileName(String fileName) {
        this.page.setFileName(fileName);
        return this;
    }

    public PageBuilder bytes(byte[] bytes){
        this.page.setBytes(bytes);
        return this;
    }
    public PageBuilder bytes(InputStream bytes){
        try {
            this.page.setBytes(IOUtils.toByteArray(bytes));
        } catch (IOException e) {
            LOGGER.info("IOException " + e.getMessage());
        }
        return this;
    }

    public Page build(){
        return page;
    }
}
