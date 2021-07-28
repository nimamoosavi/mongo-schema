package com.webold.mongoschema.schema;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.webold.framework.service.exception.ApplicationException;
import com.webold.framework.service.exception.ServiceException;
import com.webold.mongoschema.enums.MongoSchemaException;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public class MongoDbSchemaServiceImpl implements MongoDbSchemaService {
    private final MongoDatabase database;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ApplicationException<ServiceException> applicationException;

    public MongoDbSchemaServiceImpl(MongoDatabase database) {
        this.database = database;
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


    @Override
    public Boolean saveSchema(String collectionName, FieldValidation schemaFieldValidation) {

        Document result = null;
        try {
            result = saveSchema(collectionName, Document.parse(objectMapper.writeValueAsString(schemaFieldValidation)));
        } catch (JsonProcessingException e) {
            throw applicationException.createApplicationException(MongoSchemaException.SCHEMA_CAST, HttpStatus.BAD_REQUEST);
        }
        return result.containsKey("ok");
    }

    @Override
    public void drop(String collectionName) {
        this.saveSchema(collectionName, new Document());
    }


    @Override
    public FieldValidation getJsonSchema(String collectionName) {
        try {
            return objectMapper.readValue(getJsonSchemaDocument(collectionName).toJson(), FieldValidation.class);
        } catch (JsonProcessingException e) {
            throw applicationException.createApplicationException(MongoSchemaException.SCHEMA_CAST, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public Document getJsonSchemaDocument(String collectionName) {
        Document collection = retrieveValidatorDocument(collectionName);
        if (collection == null)
            throw applicationException.createApplicationException(MongoSchemaException.NOTFOUND, HttpStatus.NOT_FOUND);
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
