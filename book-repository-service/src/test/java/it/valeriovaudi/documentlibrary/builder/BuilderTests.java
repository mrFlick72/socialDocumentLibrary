package it.valeriovaudi.documentlibrary.builder;

import it.valeriovaudi.documentlibrary.model.Book;
import it.valeriovaudi.documentlibrary.model.Page;
import it.valeriovaudi.documentlibrary.model.PageTestFactory;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Created by Valerio on 30/04/2015.
 */
public class BuilderTests {
    protected static final Logger LOGGER = LoggerFactory.getLogger(BuilderTests.class);

    @Test
    public void bookBuildTest() {
        Book category = BookBuilder.newBookBuilder()
                .id(UUID.randomUUID().toString())
                .name("My First Book")
                .author("Valerio Vaudi")
                .description("description")
                .addPage(PageTestFactory.createPage("page1.jpg"))
                .build();
        LOGGER.info(category.toString());
        Assert.assertNotNull(category);
        Assert.assertEquals(category.getPageId().size(), 1);

        Book category2 = BookBuilder.newBookBuilder()
                .id(UUID.randomUUID().toString())
                .name("My First Book")
                .author("Valerio Vaudi")
                .description("description")
                .addPage(PageTestFactory.createPage("page1.jpg"))
                .addPage(PageTestFactory.createPage("page2.jpg"))
                .build();
        LOGGER.info(category2.toString());
        Assert.assertNotNull(category2);
        Assert.assertEquals(category2.getPageId().size(), 2);
    }

    @Test
    public void pageBuilderTest(){
        String id = UUID.randomUUID().toString();
        Page build = PageBuilder.newPageBuilder()
                                .fileName("page1.jpg")
                                .id(id)
                                .bytes(new byte[]{})
                             .build();
        LOGGER.info(build.toString());

        Assert.assertNotNull(build);
        Assert.assertEquals(build.getFileName(), "page1.jpg");
        Assert.assertEquals(build.getId(), id);
        Assert.assertEquals(build.getBytes().length, 0);
    }
}
