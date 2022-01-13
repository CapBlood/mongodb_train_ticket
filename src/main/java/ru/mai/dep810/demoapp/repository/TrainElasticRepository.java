package ru.mai.dep810.demoapp.repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Route;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.model.Train;

@Repository
public class TrainElasticRepository {

    private final RestHighLevelClient client;

    public TrainElasticRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Train> fullTextSearch(String date, String from, String to) {
        List<Train> trains;

        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.simpleQueryStringQuery(date));
        SearchRequest request = new SearchRequest("train_test.train").source(builder);
        try {
            SearchHit[] hits = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
            trains = Arrays.stream(hits)
                    .map(hit -> toModelObject(hit.getId(), hit.getSourceAsMap()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        List<String> routes;
        builder = new SearchSourceBuilder().query(QueryBuilders.simpleQueryStringQuery(from));
        builder = builder.query(QueryBuilders.simpleQueryStringQuery(to));

        request = new SearchRequest("train_test.route").source(builder);
        try {
            SearchHit[] hits = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
            routes = Arrays.stream(hits)
                    .map(hit -> toRouteModelObject(hit.getId(), hit.getSourceAsMap()))
                    .map(route -> route.getId())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        trains = trains.stream()
                .filter(t -> routes.contains(t.getRoute())).collect(Collectors.toList());

        trains = trains.stream()
                 .filter(t -> {
                     SearchSourceBuilder builder2 = new SearchSourceBuilder().query(
                             QueryBuilders.simpleQueryStringQuery(t.getId()));
                     SearchRequest request2 = new SearchRequest("train_test.ticket").source(builder2);
                     BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                     boolQueryBuilder.must(
                             QueryBuilders.queryStringQuery("bought: false").defaultField("*")
                                     .analyzeWildcard(true));
                     builder2 = builder2.query(boolQueryBuilder);
                     long hits;
                     try {
                         hits = client.search(request2, RequestOptions.DEFAULT).getHits().getTotalHits().value;
                     } catch (IOException e) {
                         throw new RuntimeException(e.getMessage(), e);
                     }
                     return hits > 0;
                 }).collect(Collectors.toList());

        return trains;
    }

    public Train addToIndex(Train train) {
        IndexRequest indexRequest = new IndexRequest("train_test.train").id(train.getId()).source(toMap(train));
        try {
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            if (response.status().getStatus() != 201) {
                throw new RuntimeException("Index failed with status = " + response.status().getStatus());
            }
            return findById(train.getId());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Train findById(String id) {
        GetResponse getResponse = null;
        try {
            getResponse = client.get(new GetRequest("train_test.train", id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return toModelObject(getResponse.getId(), getResponse.getSourceAsMap());
    }

    private Map<String, Object> toMap(Train train) {
        final Map<String, Object> map = new HashMap<>();
        map.put("Id", train.getId());
        map.put("date", train.getDate());
        map.put("route", train.getRoute());
        return map;
    }

    private Train toModelObject(String id, Map<String, Object> map) {
        return new Train(id, (String) map.get("date"), (String) map.get("route"));
    }

    private Map<String, Object> toRouteMap(Route route) {
        final Map<String, Object> map = new HashMap<>();
        map.put("Id", route.getId());
        map.put("route", route.getRoute());
        return map;
    }

    private Route toRouteModelObject(String id, Map<String, Object> map) {
        return new Route(id, (ArrayList<String>) map.get("route"));
    }
}
