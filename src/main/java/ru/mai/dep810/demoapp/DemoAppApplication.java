package ru.mai.dep810.demoapp;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

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
