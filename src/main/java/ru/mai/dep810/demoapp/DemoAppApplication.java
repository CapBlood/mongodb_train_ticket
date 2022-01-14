package ru.mai.dep810.demoapp;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class DemoAppApplication {

    @Bean
    public Docket petApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("full-petstore-api")
                .apiInfo(apiInfo())
                .select()
//                .paths(petstorePaths())
                .build()
                ;
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("API продажи билетов")
                .description("")
                .termsOfServiceUrl("")
                .contact(new Contact("Aksentev_Ksemidov", "", ""))
                .version("2.0")
                .build();
    }

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
                new MapConfig("station")
                        .setEvictionConfig(
                                new EvictionConfig()
                                        .setEvictionPolicy(EvictionPolicy.LRU)
                                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                                        .setSize(1000))
                        .setNearCacheConfig(new NearCacheConfig("station-near-cache"))
        );
        return Hazelcast.newHazelcastInstance(cfg);
    }


    public static void main(String[] args) {
        SpringApplication.run(DemoAppApplication.class, args);
    }

}
