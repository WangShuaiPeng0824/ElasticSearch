package com.usian.test;

import com.usian.ElasticsearchApp;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {ElasticsearchApp.class})
public class IndexWriterTest {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testCreateIndex() throws IOException {
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("java1906");
        createIndexRequest.settings(Settings.builder().
                put("number_of_shards",2).
                put("number_of_replicas",0));
        createIndexRequest.mapping("course","{\n" +
                "  \"_source\": {\n" +
                "    \"excludes\":[\"description\"]\n" +
                "  }, \n" +
                " \t\"properties\": {\n" +
                "      \"name\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\":\"ik_max_word\",\n" +
                "          \"search_analyzer\":\"ik_smart\"\n" +
                "      },\n" +
                "      \"description\": {\n" +
                "          \"type\": \"text\",\n" +
                "          \"analyzer\":\"ik_max_word\",\n" +
                "          \"search_analyzer\":\"ik_smart\"\n" +
                "       },\n" +
                "       \"studymodel\": {\n" +
                "          \"type\": \"keyword\"\n" +
                "       },\n" +
                "       \"price\": {\n" +
                "          \"type\": \"float\"\n" +
                "       },\n" +
                "       \"pic\":{\n" +
                "\t\t   \"type\":\"text\",\n" +
                "\t\t   \"index\":false\n" +
                "\t    },\n" +
                "       \"timestamp\": {\n" +
                "      \t\t\"type\":   \"date\",\n" +
                "      \t\t\"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd\"\n" +
                "    \t }\n" +
                "  }\n" +
                "}", XContentType.JSON);
        CreateIndexResponse response = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    @Test
    public void testDeleteIndex() throws IOException {
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("java1906");
        //restHighLevelClient.indices()：返回索引库客户端
        DeleteIndexResponse response = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    @Test
    public void testAddDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest("java1906","course","1");
        indexRequest.source("{\n" +
                "  \"name\": \"spring cloud实战\",\n" +
                "  \"description\": \"本课程主要从四个章节进行讲解： 1.微服务架构入门 2.spring cloud 基础入门 3.实战Spring Boot 4.注册中心eureka。\",\n" +
                "  \"studymodel\": \"201001\",\n" +
                "  \"price\": 5.6\n" +
                "}",XContentType.JSON);
        IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @Test
    public void testBulkAddDoc() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(new IndexRequest("java1906","course").source("{\"name\":\"php实战\",\"description\":\"php谁都不服\",\"studymodel\":\"201001\",\"price\":\"5.6\"}",XContentType.JSON));
        bulkRequest.add(new IndexRequest("java1906","course").source("{\"name\":\"net实战\",\"description\":\"net从入门到放弃\",\"studymodel\":\"201001\",\"price\":\"7.6\"}",XContentType.JSON));
        BulkResponse response = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println(response.hasFailures());
    }

    @Test
    public void testUpdateDoc() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("java1906","course","1");
        updateRequest.doc("{\n" +
                " \"price\":7.77\n" +
                "}",XContentType.JSON);
        UpdateResponse response = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

    @Test
    public void testDelDoc() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("java1906", "course", "1");
        DeleteResponse response = restHighLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(response.toString());
    }

}
