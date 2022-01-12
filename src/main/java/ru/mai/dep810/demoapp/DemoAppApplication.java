package ru.mai.dep810.demoapp;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoAppApplication {

    @Bean
    RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                )
        );
    }

    @Bean
    HazelcastInstance hazelcastInstance() {
        Config cfg = new Config().addMapConfig(
                new MapConfig("train")
                        .setEvictionConfig(
                                new EvictionConfig()
                                        .setEvictionPolicy(EvictionPolicy.LRU)
                                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                        .setSize(1000))
                        .setNearCacheConfig(new NearCacheConfig("train-near-cache"))
        );
        return Hazelcast.newHazelcastInstance(cfg);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoAppApplication.class, args);
    }

}
