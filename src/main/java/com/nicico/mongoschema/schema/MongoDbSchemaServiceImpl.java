package com.nicico.mongoschema.schema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import lombok.SneakyThrows;
import org.bson.Document;

import java.util.*;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public class MongoDbSchemaServiceImpl implements MongoDbSchemaService {
    private MongoDatabase database;

    private ObjectMapper objectMapper;

    public MongoDbSchemaServiceImpl(MongoDatabase database) {
        this.database = database;
        objectMapper = new ObjectMapper();
    }

    @Override
    public Document saveSchema(String collectionName, Document schema) {
        Map<String, Object> jsonSchema = new HashMap<>();
        jsonSchema.put(MONGO_COLL_MOD, collectionName);
        Document validator = retrieveValidatorDocument(collectionName).get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class);
        if (validator == null)
            validator = new Document();
        validator.put(MONGO_SCHEMA, schema);
        jsonSchema.put(MONGO_VALIDATOR, validator);
        return database.runCommand(new Document(jsonSchema));
    }

    @SneakyThrows
    @Override
    public Boolean saveSchema(String collectionName, FieldValidation schemaFieldValidation) {
        Document result = saveSchema(collectionName, Document.parse(objectMapper.writeValueAsString(schemaFieldValidation)));
        return result.containsKey("ok");
    }

    @Override
    public void drop(String collectionName) {
        this.saveSchema(collectionName, new Document());
    }

    @SneakyThrows
    @Override
    public FieldValidation getJsonSchema(String collectionName) {
        return objectMapper.readValue(getJsonSchemaDocument(collectionName).toJson(), FieldValidation.class);
    }

    @Override
    public Document getJsonSchemaDocument(String collectionName) {
        Document collection = retrieveValidatorDocument(collectionName);
        assert (collection == null) : "collection not found";
        return readJsonSchema(collection);
    }

    /**
     * get collection definition document
     *
     * @param collectionName collection name
     * @return collection definition document
     */
    public Document retrieveValidatorDocument(String collectionName) {
        return database.listCollections().filter(Filters.eq("name", collectionName)).first();
    }

    /**
     * @param collection collection definition
     * @return document in $jsonSchema
     */
    private static Document readJsonSchema(Document collection) {
        return (collection.containsKey(MONGO_OPTION)
                && collection.get(MONGO_OPTION, Document.class).containsKey(MONGO_VALIDATOR) &&
                collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).containsKey(MONGO_SCHEMA))
                ? collection.get(MONGO_OPTION, Document.class).get(MONGO_VALIDATOR, Document.class).get(MONGO_SCHEMA, Document.class) : new Document();
    }
}
