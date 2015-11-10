package it.valeriovaudi.documentlibrary.service;

import com.google.common.collect.ImmutableList;
import it.valeriovaudi.documentlibrary.model.SearchIndex;
import it.valeriovaudi.documentlibrary.repository.SearchIndexRepositoryTests;
import it.valeriovaudi.documentlibrary.support.SearchIndexBuilder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import javax.json.Json;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;

/**
 * Created by Valerio on 09/06/2015.
 */
public class SearchIndexServiceTests extends SearchIndexRepositoryTests {

    /* insert methods */
//    SearchIndex saveIndex(I bookId, String bookName, boolean published, String... searchTags);
    @Test
    public void saveIndexServiceTest() throws Exception {
        SearchIndex searchIndexAux = SearchIndexBuilder.newSearchIndexBuilder().bookName(bookName).published(true).addSearchTags(tag1,tag2).build();
        genericPost(fromPath("/searchBookIndex").build(),201,MediaType.APPLICATION_JSON,objectMapper.writeValueAsString(searchIndexAux));
    }

    /* update methods */
//    SearchIndex publishSearchBookIndex(I bookId, boolean published);
    @Test
    public void publishSearchBookIndexServiceTest() throws Exception {
        searchIndex = searchIndexRepository.publishSearchBookIndex(this.searchIndex.getBookId(), false);
        Assert.assertFalse(this.searchIndex.isPublished());
        mockMvc.perform(put(fromPath("/searchBookIndex/{bookId}/publishBook").buildAndExpand(this.searchIndex.getBookId()).toUri()))
                .andExpect(status()
                        .isNoContent());

        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());
        Assert.assertNotNull(searchIndexById);
        Assert.assertTrue(searchIndexById.isPublished());
    }

//     unPublishSearchBookIndex(@PathVariable(value = "bookId") String bookId){
    @Test
    public void unPublishSearchBookIndexServiceTest() throws Exception {
        Assert.assertTrue(this.searchIndex.isPublished());
        mockMvc.perform(put(fromPath("/searchBookIndex/{bookId}/unPublishBook").buildAndExpand(this.searchIndex.getBookId()).toUri()))
                                .andExpect(status()
                                        .isNoContent());

        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());
        Assert.assertNotNull(searchIndexById);
        Assert.assertFalse(searchIndexById.isPublished());
    }

//    SearchIndex addSearchTags(I bookId, String... searchTags);
    @Test
    public void addSearchTagsServiceTest() throws Exception {
        String tag = "NEW TAG";
        Assert.assertFalse(this.searchIndex.getSearchTags().contains(tag));

        mockMvc.perform(put(fromPath("/searchBookIndex/{bookId}/addSearchTags").buildAndExpand(this.searchIndex.getBookId()).toUri())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(ImmutableList.of(tag))))
                .andExpect(status()
                        .isNoContent());

        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());
        Assert.assertNotNull(searchIndexById);
        Assert.assertTrue(searchIndexById.getSearchTags().contains(tag));
    }

    //    SearchIndex removeSearchTags(I bookId, String...searchTags);
    @Test
    public void removeSearchTagsServiceTest() throws Exception {
        Assert.assertTrue(this.searchIndex.getSearchTags().contains(tag1));

        mockMvc.perform(put(fromPath("/searchBookIndex/{bookId}/removeSearchTags").buildAndExpand(this.searchIndex.getBookId()).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ImmutableList.of(tag1))))
                .andExpect(status()
                        .isNoContent());

        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());
        Assert.assertNotNull(searchIndexById);
        Assert.assertFalse(searchIndexById.getSearchTags().contains(tag1));
    }

//    ResponseEntity<Void> updateSearchIndex(@PathVariable(value = "bookId") String bookId,@RequestBody String body)
    @Test
    public void updateSearchIndexTest() throws Exception {
        Assert.assertTrue(this.searchIndex.isPublished());
        Assert.assertFalse(this.searchIndex.getSearchTags().containsAll(Arrays.asList("NEW_JAVA","NEW_.NET")));

        String build = Json.createObjectBuilder().add("published", false).add("searchTags", Json.createArrayBuilder().add("NEWJAVA").add("NEWNET")).build().toString();

        mockMvc.perform(put(fromPath("/searchBookIndex/{bookId}").buildAndExpand(this.searchIndex.getBookId()).toUri())
                .contentType(MediaType.APPLICATION_JSON)
                .content(build))
                .andExpect(status()
                        .isNoContent());

        SearchIndex searchIndexById = searchIndexRepository.findSearchIndexById(this.searchIndex.getBookId());
        Assert.assertNotNull(searchIndexById);
        LOGGER.info(searchIndexById.toString());
        Assert.assertFalse(searchIndexById.isPublished());
        Assert.assertTrue(searchIndexById.getSearchTags().containsAll(Arrays.asList("NEWJAVA", "NEWNET")));
    }

    /* read methods*/
//    SearchIndex findSearckIndexById(I bookId);
    @Test
    public void findSearchIndexByIdServiceTest() throws Exception {
        SearchIndex searchIndexById = objectMapper.readValue(genericGet(fromPath("/searchBookIndex/{bookId}").buildAndExpand(this.searchIndex.getBookId())), SearchIndex.class);
        LOGGER.info(searchIndexById.toString());
        Assert.assertNotNull(searchIndexById);

        mockMvc.perform(get(fromPath("/searchBookIndex/{bookId}").buildAndExpand(UUID.randomUUID().toString()).toUri())).
                andExpect(status().isNotFound()).
                andReturn();
    }

//    List<SearchIndex> findSearchIndex(Map<String,String> paramiters);
    @Test
    public void findSearchIndexServiceTest() throws Exception {
        String tag3 = "TAG3";
        SearchIndex searchIndex1 = SearchIndexBuilder.newSearchIndexBuilder().bookName("book with tag1").published(this.published).addSearchTags(this.tag1).build();
        SearchIndex searchIndex2 = SearchIndexBuilder.newSearchIndexBuilder().bookName("book with tag1").published(this.published).addSearchTags(tag3).build();

        searchIndexRepository.saveIndex(searchIndex1);
        searchIndexRepository.saveIndex(searchIndex2);

        String bookNameSearckCriteria = "Building";

        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("bookName",bookNameSearckCriteria);

        UriComponentsBuilder searchBookUriComponentsBuilder = fromPath("/searchBookIndex").queryParam("q",String.format("bookName=%s",bookNameSearckCriteria));

        List searchIndexList = objectMapper.readValue(genericGet(searchBookUriComponentsBuilder.build()), List.class);
        LOGGER.info(searchIndexList.toString());

        Assert.assertFalse(searchIndexList.isEmpty());
        Assert.assertEquals(1,searchIndexList.size());

        searchIndexList = objectMapper.readValue(genericGet(searchBookUriComponentsBuilder.queryParam("q",String.format("bookName=%s;searchTags=%s",bookNameSearckCriteria,tag1)).build()), List.class);
        Assert.assertFalse(searchIndexList.isEmpty());
        Assert.assertEquals(2,searchIndexList.size());

        searchIndexList = objectMapper.readValue(genericGet(searchBookUriComponentsBuilder.queryParam("q",String.format("bookName=%s;searchTags=%s,%s",bookNameSearckCriteria,tag1,tag3)).build()), List.class);

        LOGGER.info(searchIndexList.toString());

        Assert.assertFalse(searchIndexList.isEmpty());
        Assert.assertEquals(3,searchIndexList.size());

        searchIndexList = objectMapper.readValue(genericGet(searchBookUriComponentsBuilder.queryParam("q", String.format("bookName=%s;searchTags=%s,%s,%s",bookNameSearckCriteria, tag1, tag3, tag2)).build()), List.class);

        LOGGER.info(searchIndexList.toString());

        Assert.assertFalse(searchIndexList.isEmpty());
        Assert.assertEquals(3,searchIndexList.size());

        searchBookUriComponentsBuilder = fromPath("/searchBookIndex");

        searchIndexList = objectMapper.readValue(genericGet(searchBookUriComponentsBuilder.build()), List.class);

        Assert.assertNotNull(searchIndexList);

        LOGGER.info("searchIndexList: " + searchIndexList.toString());
        Assert.assertEquals(0,searchIndexList.size());
    }
}
