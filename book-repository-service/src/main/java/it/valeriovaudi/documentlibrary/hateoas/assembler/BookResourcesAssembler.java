package it.valeriovaudi.documentlibrary.hateoas.assembler;

import it.valeriovaudi.documentlibrary.endpoint.BookServiceEndPoint;
import it.valeriovaudi.documentlibrary.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


/**
 * Created by Valerio on 09/05/2015.
 */
@Component
public class BookResourcesAssembler extends ResourceAssemblerSupport<Book,Resource> {

    @Autowired
    private Environment environment;

    public BookResourcesAssembler() {
        super(BookServiceEndPoint.class, Resource.class);
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Resource<Book> toResource(Book entity) {
        Resource<Book> bookResource = new Resource<>(entity);

        Map<Integer, Object> pageIdResourceMap = new HashMap<>();
        Resource pageIdResource;

        for (Map.Entry<Integer, Object> integerStringEntry : entity.getPageId().entrySet()) {
            pageIdResource = new Resource(integerStringEntry.getValue());

            ControllerLinkBuilder linkBuilder = linkTo(BookServiceEndPoint.class).slash(entity.getId());

            // add the link to the service that return the page in a binary way
            pageIdResource.add(linkBuilder
                    .slash("pageData")
                    .slash(String.format("%s.%s",
                            integerStringEntry.getKey(),
                            environment.getProperty("bookService.bookPageFileFormat")))
                    .withSelfRel());

            // add the link to the service thath return the page in a binary way with other info about the page
            pageIdResource.add(linkBuilder
                    .slash("page")
                    .slash(integerStringEntry.getKey())
                    .withSelfRel());

            pageIdResourceMap.put(integerStringEntry.getKey(),pageIdResource);
        }

        bookResource.getContent().setPageId(pageIdResourceMap);
        bookResource.add(linkTo(BookServiceEndPoint.class)
                            .slash("book")
                            .slash(entity.getId())
                        .withSelfRel());

        return bookResource;
    }
}
