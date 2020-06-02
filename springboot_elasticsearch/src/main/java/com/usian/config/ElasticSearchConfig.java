package com.usian.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchProperties {

    @Bean
    public RestHighLevelClient getRestHighLevelClient(){
        String[] hosts = getClusterNodes().split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < hosts.length; i++) {
            String h = hosts[i];//192.168.146.146:9200
            httpHosts[0] = new HttpHost(h.split(":")[0],Integer.parseInt(h.split(":")[1]));
        }
        //创建ElasticSearchConfig(ip和port)
        return new RestHighLevelClient(RestClient.builder(httpHosts));
    }

}
