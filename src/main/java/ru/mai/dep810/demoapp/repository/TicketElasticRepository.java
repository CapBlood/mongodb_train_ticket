package ru.mai.dep810.demoapp.repository;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;
import ru.mai.dep810.demoapp.model.Route;
import ru.mai.dep810.demoapp.model.Ticket;
import ru.mai.dep810.demoapp.model.Train;

@Repository
public class TicketElasticRepository {

    private final RestHighLevelClient client;

    public TicketElasticRepository(RestHighLevelClient client) {
        this.client = client;
    }

    public List<Ticket> fullTextSearch(String id) {
        List<Ticket> tickets;

        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.simpleQueryStringQuery(id));
        builder.size(100);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(
                QueryBuilders.queryStringQuery("bought:false"));
        builder = builder.query(boolQueryBuilder);

        SearchRequest request = new SearchRequest("train_test.ticket").source(builder);
        try {
            SearchHit[] hits = client.search(request, RequestOptions.DEFAULT).getHits().getHits();
            tickets = Arrays.stream(hits)
                    .map(hit -> toModelObject(hit.getId(), hit.getSourceAsMap()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return tickets;
    }

    public Ticket addToIndex(Ticket train) {
        IndexRequest indexRequest = new IndexRequest("train_test.ticket").id(train.getId()).source(toMap(train));
        try {
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
//            client.up
            if (response.status().getStatus() != 201) {
                throw new RuntimeException("Index failed with status = " + response.status().getStatus());
            }
            return findById(train.getId());
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    public void updateIndex(Ticket train) {
        UpdateRequest request = new UpdateRequest("train_test.ticket", train.getId())
                .doc("bought", train.getBought());
        try {
            UpdateResponse updateResponse = client.update(
                    request, RequestOptions.DEFAULT);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Ticket findById(String id) {
        GetResponse getResponse = null;
        try {
            getResponse = client.get(new GetRequest("train_test.ticket", id), RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return toModelObject(getResponse.getId(), getResponse.getSourceAsMap());
    }

    private Map<String, Object> toMap(Ticket ticket) {
        final Map<String, Object> map = new HashMap<>();
        map.put("Id", ticket.getId());
        map.put("bought", ticket.getBought());
        map.put("train", ticket.getTrain());
        return map;
    }

    private Ticket toModelObject(String id, Map<String, Object> map) {
        return new Ticket(id, (boolean) map.get("bought"), (String) map.get("train"));
    }
}
