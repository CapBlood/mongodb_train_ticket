package ru.mai.dep810.demoapp.repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Ticket;

@Repository
public class TrainTicketElasticRepository {

    private final RestHighLevelClient client;

    public TrainTicketElasticRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Ticket> fullTextSearch(String query) {
        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.simpleQueryStringQuery(query));
        SearchRequest request = new SearchRequest("train_test").source(builder);
        try {
            SearchHit[] hits = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
            return Arrays.stream(hits)
                    .map(hit -> toModelObject(hit.getId(), hit.getSourceAsMap()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Ticket addToIndex(Ticket ticket) {
        IndexRequest indexRequest = new IndexRequest("train_test").id(ticket.getId()).source(toMap(ticket));
        try {
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
            if (response.status().getStatus() != 201) {
                throw new RuntimeException("Index failed with status = " + response.status().getStatus());
            }
            return findById(ticket.getId());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Ticket findById(String id) {
        GetResponse getResponse = null;
        try {
            getResponse = client.get(new GetRequest("train_test", id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return toModelObject(getResponse.getId(), getResponse.getSourceAsMap());
    }

    private Map<String, Object> toMap(Ticket ticket) {
        final Map<String, Object> map = new HashMap<>();
        map.put("Id", ticket.getId());
        map.put("Bought", ticket.getBought());
        map.put("Train", ticket.getTrain());
        return map;
    }

    private Ticket toModelObject(String id, Map<String, Object> map) {
        return new Ticket(id, (boolean) map.get("bought"), (String) map.get("train"));
    }
}
