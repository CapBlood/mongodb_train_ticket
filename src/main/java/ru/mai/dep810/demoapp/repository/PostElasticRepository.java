//package ru.mai.dep810.demoapp.repository;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import org.elasticsearch.action.get.GetRequest;
//import org.elasticsearch.action.get.GetResponse;
//import org.elasticsearch.action.index.IndexRequest;
//import org.elasticsearch.action.index.IndexResponse;
//import org.elasticsearch.action.search.SearchRequest;
//import org.elasticsearch.client.RequestOptions;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.elasticsearch.index.query.QueryBuilders;
//import org.elasticsearch.search.SearchHit;
//import org.elasticsearch.search.builder.SearchSourceBuilder;
//import org.springframework.stereotype.Repository;
//import ru.mai.dep810.demoapp.model.Station;
//
//@Repository
//public class PostElasticRepository {
//
//    private final RestHighLevelClient client;
//
//    public PostElasticRepository(RestHighLevelClient client) {
//        this.client = client;
//    }
//
//    public List<Station> fullTextSearch(String query) {
//        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.simpleQueryStringQuery(query));
//        SearchRequest request = new SearchRequest("so_ru").source(builder);
//        try {
//            SearchHit[] hits = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
//            return Arrays.stream(hits)
//                    .map(hit -> toModelObject(hit.getId(), hit.getSourceAsMap()))
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//    public Station addToIndex(Station post) {
//        IndexRequest indexRequest = new IndexRequest("so_ru").id(post.getId()).source(toMap(post));
//        try {
//            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
//            if (response.status().getStatus() != 201) {
//                throw new RuntimeException("Index failed with status = " + response.status().getStatus());
//            }
//            return findById(post.getId());
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//    public Station findById(String id) {
//        GetResponse getResponse = null;
//        try {
//            getResponse = client.get(new GetRequest("so_ru", id), RequestOptions.DEFAULT);
//        } catch (IOException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//        return toModelObject(getResponse.getId(), getResponse.getSourceAsMap());
//    }
//
//    private Map<String, Object> toMap(Station post) {
//        final Map<String, Object> map = new HashMap<>();
//        map.put("Id", post.getId());
//        map.put("Body", post.getBody());
//        return map;
//    }
//
//    private Station toModelObject(String id, Map<String, Object> map) {
//        return new Station(id, (String) map.get("Body"));
//    }
//}
