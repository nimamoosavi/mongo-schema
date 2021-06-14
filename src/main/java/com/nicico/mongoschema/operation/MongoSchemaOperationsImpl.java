package com.nicico.mongoschema.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.nicico.mongoschema.schema.FieldValidation;
import com.nicico.mongoschema.schema.MongoDbSchemaService;
import com.nicico.mongoschema.schema.MongoDbSchemaServiceImpl;
import lombok.SneakyThrows;
import org.bson.Document;

import java.util.Set;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public class MongoSchemaOperationsImpl implements MongoSchemaOperations {


    private MongoDbSchemaService mongoDbSchemaService;
    private ObjectMapper objectMapper;


    public MongoSchemaOperationsImpl(MongoDatabase database) {
        this.objectMapper = new ObjectMapper();
        this.mongoDbSchemaService = new MongoDbSchemaServiceImpl(database) ;
        this.mongoDbSchemaService = new MongoDbSchemaServiceImpl(database);
    }


    @Override
    public Document
    saveSchema(String collectionName, Document schema) {
        return mongoDbSchemaService.saveSchema(collectionName, schema);
    }


    @Override
    public Document getSchema(String collectionName) {
        return mongoDbSchemaService.getJsonSchemaDocument(collectionName);
    }

    @Override
    public Document getFieldValidation(String collectionName, String fieldName) {
        Document schema = getSchema(collectionName);
        schema = nestedSchema(schema, fieldName);
        return schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class);
    }

    @Override
    public Document getFieldValidation(String collectionName) {
        return getFieldValidation(collectionName, null);
    }

    @Override
    public Set<String> getRequiredField(String collectionName, String nestedFieldName) {
        Document schema = getSchema(collectionName);
        schema = nestedSchema(schema, nestedFieldName);
        return schema.get(MongoDbSchemaService.REQUIRED, Set.class);
    }

    private Document nestedSchema(Document schema, String nestedFieldName) {
        if (nestedFieldName != null)
            for (String fieldName : nestedFieldName.split("/."))
                schema = schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).get(fieldName, Document.class);
        return schema;
    }

    @Override
    public void setRequiredField(String collectionName, String nestedFieldName, Set<String> requiredField) {
        Document schema = getSchema(collectionName);
        nestedSchema(schema, nestedFieldName).put(MongoDbSchemaService.REQUIRED, requiredField);
        saveSchema(collectionName, schema);
    }

    @SneakyThrows
    @Override
    public Document saveFieldValidation(String collectionName, String fieldName, FieldValidation fieldValidation) {
        Document schema = getSchema(collectionName);
        schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).put(fieldName, Document.parse(objectMapper.writeValueAsString(fieldValidation)));
        return saveSchema(collectionName, schema);
    }
}
