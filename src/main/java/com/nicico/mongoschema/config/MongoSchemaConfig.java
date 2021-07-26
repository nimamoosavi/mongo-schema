package com.nicico.mongoschema.config;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:mongo-schema-exceptions.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class MongoSchemaConfig {
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.port}")
    private Integer port;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;

    @Bean
    public MongoDatabase createMongoDatabase() {
        MongoClient mongoClient = new MongoClient(host, port);
        MongoCredential credential = MongoCredential.createCredential(username, database,
                password.toCharArray());
        return mongoClient.getDatabase(database);
    }
}
