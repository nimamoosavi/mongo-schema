package com.nicico.mongoschema;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.IndexOptions;
import com.nicico.mongoschema.index.Index;
import com.nicico.mongoschema.index.IndexOperations;
import com.nicico.mongoschema.schema.MongoDbSchemaService;
import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.core.MongoTemplate;

@SpringBootApplication
@ComponentScan({"com.nicico.cost.crud", "com.nicico.cost.swagger", "com.nicico.cost.framework", "com.nicico.mongoclient", "com.nicico.mongoschema"})
@ServletComponentScan(basePackages = {"com.nicico.cost.crud", "com.nicico.cost.swagger", "com.nicico.cost.framework", "com.nicico.cost.jdbcclient", "com.nicico.cost"})
public class MongoSchemaApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(MongoSchemaApplication.class, args);
        MongoTemplate mongoTemplate = applicationContext.getBean(MongoTemplate.class);
        MongoDbSchemaService mongoDbSchemaService = applicationContext.getBean(MongoDbSchemaService.class);
        mongoDbSchemaService.getJsonSchemaDocument("costHeader");
//        IndexOperations indexOperations = applicationContext.getBean(IndexOperations.class);
//        indexOperations.dropIndex("costHeader","costHeader_name");

/*		IndexOptions idx = new IndexOptions();
		idx.unique(true);
		idx.sparse(true);*/
//        Index index = Index.builder().name("sample").key("descriptions").expireAfterSeconds(1000L).unique(true).sparse(false).build();

//        mongoTemplate.getDb().getCollection("costHeader").createIndex(new Document(index.getKey(), 1), index);
         mongoTemplate.getDb().getCollection("costHeader").listIndexes();

    }

}
