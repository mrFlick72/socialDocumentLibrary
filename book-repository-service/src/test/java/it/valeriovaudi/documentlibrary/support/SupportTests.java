package it.valeriovaudi.documentlibrary.support;

import it.valeriovaudi.documentlibrary.builder.PageBuilder;
import it.valeriovaudi.documentlibrary.model.Page;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Valerio on 30/04/2015.
 */
public class SupportTests {

    protected int numberPage = 1;
    protected String filePageName = "page1.jpg";

    @Test
    public void PageSupportTests(){
        Integer integer = PageSupport.fileNameToindex(filePageName);
        Assert.assertEquals(numberPage,integer.intValue());

        Page page = PageBuilder.newPageBuilder()
                .id(UUID.randomUUID().toString())
                .fileName(filePageName)
                .build();
        integer = PageSupport.fileNameToindex(page);
        Assert.assertEquals(numberPage,integer.intValue());
    }


    @Test
    public void GridFSDBFileSupportTests(){
        Integer integer = PageSupport.fileNameToindex(filePageName);
        Assert.assertEquals(numberPage,integer.intValue());

        Page page = PageBuilder.newPageBuilder()
                .id(UUID.randomUUID().toString())
                .fileName(filePageName)
                .build();
        integer = PageSupport.fileNameToindex(page);
        Assert.assertEquals(numberPage,integer.intValue());
    }
}
