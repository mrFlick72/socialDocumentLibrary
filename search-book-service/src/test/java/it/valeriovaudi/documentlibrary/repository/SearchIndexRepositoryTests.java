package it.valeriovaudi.documentlibrary.repository;

import it.valeriovaudi.documentlibrary.SearchBookServiceApplicationTests;
import it.valeriovaudi.documentlibrary.model.SearchIndex;
import it.valeriovaudi.documentlibrary.support.BookNameSearchTokenizer;
import it.valeriovaudi.documentlibrary.support.SearchIndexBuilder;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Valerio on 09/06/2015.
 */
public class SearchIndexRepositoryTests extends SearchBookServiceApplicationTests{

    @Autowired
    protected SearchIndexRepository searchIndexRepository;

    @Autowired
    protected BookNameSearchTokenizer bookNameSearchTokenizer;

    protected String bookName = "Building Microservices";
    protected boolean published = true;
    protected String tag1 = "TAG1";
    protected String tag2 = "TAG2";

    protected SearchIndex searchIndex;

    @Before
    public void setUpBookIndexerRepositoryTests(){
        // reset the eventualy sinit from other test methods
        this.searchIndex = null;
        saveIndexTest();
    }

    @Test
    public void saveIndexTest() {
        SearchIndex searchIndexAux = SearchIndexBuilder.newSearchIndexBuilder()
                .bookName(this.bookName)
                .published(this.published)
                .addSearchTags(this.tag1,this.tag2)
                .build();

        this.searchIndex = searchIndexRepository.saveIndex(searchIndexAux);


        assertNotNull(this.searchIndex);
        assertArrayEquals(bookNameSearchTokenizer.bookNameTokenizer(bookName).toArray(), this.searchIndex.getBookNameTokens().toArray());
        LOGGER.info(this.searchIndex.toString());
    }

    @Test
    public void publishSearchBookIndexTest() {
        assertEquals(true, this.searchIndex.isPublished());
        this.searchIndex = searchIndexRepository.publishSearchBookIndex(this.searchIndex.getBookId(), false);
        assertEquals(false, this.searchIndex.isPublished());
        LOGGER.info(this.searchIndex.toString());
    }

    @Test
    public void addSearchTagsTest() {
        String newTag = "NEW_TAG1";
        this.searchIndex = searchIndexRepository.addSearchTags(this.searchIndex.getBookId(), newTag, newTag, newTag, newTag);

        SearchIndex searchIndexAux = searchIndexRepository.findSearchIndexById(searchIndex.getBookId());

        assertNotNull(searchIndexAux);
        LOGGER.info(searchIndexAux.toString());
        assertTrue(this.searchIndex.getSearchTags().contains("NEW_TAG1"));
        assertEquals(4, this.searchIndex.getSearchTags().stream().filter(s -> s.equals(newTag)).count());
    }

    @Test
    public void removeSearchTagsTest() {
        SearchIndex searchIndex = searchIndexRepository.removeSearchTags(this.searchIndex.getBookId(), tag1, tag2);

        SearchIndex searchIndexAux = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());

        assertNotNull(searchIndexAux);
        LOGGER.info(searchIndexAux.toString());
        assertTrue(searchIndex.getSearchTags().isEmpty());
    }

    @Test
    public void findSearckIndexByIdTest() {
        SearchIndex searchIndexAux = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());

        assertNotNull(searchIndexAux);
        LOGGER.info(searchIndexAux.toString());
    }

    @Test
    public void findSearchIndexByMetadataTest() {
        String tag3 = "TAG3";
        SearchIndex searchIndex1 = SearchIndexBuilder.newSearchIndexBuilder().bookName("book with tag1").published(this.published).addSearchTags(this.tag1).build();
        SearchIndex searchIndex2 = SearchIndexBuilder.newSearchIndexBuilder().bookName("book with tag1").published(this.published).addSearchTags(tag3).build();

        searchIndexRepository.saveIndex(searchIndex1);
        searchIndexRepository.saveIndex(searchIndex2);

        List<SearchIndex> searchIndexList = searchIndexRepository.findSearchIndexByMetadata("Building",new String[]{});
        LOGGER.info(searchIndexList.toString());

        assertFalse(searchIndexList.isEmpty());
        assertEquals(1, searchIndexList.size());

        searchIndexList = searchIndexRepository.findSearchIndexByMetadata("Building",new String[] {tag1});
        LOGGER.info(searchIndexList.toString());

        assertFalse(searchIndexList.isEmpty());
        assertEquals(2, searchIndexList.size());

        searchIndexList = searchIndexRepository.findSearchIndexByMetadata("Building",new String[] {tag1,tag3});
        LOGGER.info(searchIndexList.toString());

        assertFalse(searchIndexList.isEmpty());
        assertEquals(3, searchIndexList.size());

        searchIndexList = searchIndexRepository.findSearchIndexByMetadata("Building", new String[]{tag1,tag2,tag3});
        LOGGER.info(searchIndexList.toString());

        assertFalse(searchIndexList.isEmpty());
        assertEquals(3, searchIndexList.size());

        searchIndexList = searchIndexRepository.findSearchIndexByMetadata("", new String[]{tag1});
        LOGGER.info(searchIndexList.toString());

        assertFalse(searchIndexList.isEmpty());
        assertEquals(2, searchIndexList.size());
    }
}
