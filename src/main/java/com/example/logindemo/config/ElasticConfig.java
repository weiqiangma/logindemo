package com.example.logindemo.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

public class ElasticConfig {

    @Bean
    RestHighLevelClient client(){
        ClientConfiguration clientConfiguration = ClientConfiguration.builder().connectedTo("localhost:9200","localhost:9201").build();
        return RestClients.create(clientConfiguration).rest();
    }
}
