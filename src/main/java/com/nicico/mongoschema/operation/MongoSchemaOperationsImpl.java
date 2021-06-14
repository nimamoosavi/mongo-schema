package com.nicico.mongoschema.operation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoDatabase;
import com.nicico.cost.framework.service.exception.ApplicationException;
import com.nicico.cost.framework.service.exception.ServiceException;
import com.nicico.mongoschema.enums.MongoSchemaException;
import com.nicico.mongoschema.schema.FieldValidation;
import com.nicico.mongoschema.schema.MongoDbSchemaService;
import com.nicico.mongoschema.schema.MongoDbSchemaServiceImpl;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public class MongoSchemaOperationsImpl implements MongoSchemaOperations {


    private MongoDbSchemaService mongoDbSchemaService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    ApplicationException<ServiceException> applicationException;

    public MongoSchemaOperationsImpl(MongoDatabase database) {
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


    @Override
    public Document saveFieldValidation(String collectionName, String fieldName, FieldValidation fieldValidation) {
        Document schema = getSchema(collectionName);
        try {
            schema.get(MongoDbSchemaService.MONGO_PROPERTIES, Document.class).put(fieldName, Document.parse(objectMapper.writeValueAsString(fieldValidation)));
        } catch (JsonProcessingException e) {
            throw applicationException.createApplicationException(MongoSchemaException.VALIDATION_SAVE, HttpStatus.BAD_REQUEST);
        }
        return saveSchema(collectionName, schema);
    }

    @Override
    public Boolean saveSchema(String collectionName, Map<String, FieldPropertyDTO> schemaFieldValidation) {
        FieldValidation fieldValidation = mongoDbSchemaService.getJsonSchema(collectionName);
        for (String fieldKey : schemaFieldValidation.keySet()) {
            String[] fieldName = fieldKey.split(MONGO_FIELD_NAME_SEPARATOR);
            FieldValidation schemaField = getTargetSchema(fieldValidation, fieldName);
            FieldPropertyDTO dto = schemaFieldValidation.get(fieldKey);
            castDtoToValidation(schemaField, dto);
            FieldValidation parentSchemaField = getTargetSchema(fieldValidation, Arrays.copyOfRange(fieldName, 0, fieldName.length - 1));
            if (parentSchemaField.getRequired() == null)
                parentSchemaField.setRequired(new HashSet<>());
            if (dto.getRequired()) {
                parentSchemaField.getRequired().add(fieldName[fieldName.length - 1]);
            } else {
                parentSchemaField.getRequired().remove(fieldName[fieldName.length - 1]);
            }
        }
        return mongoDbSchemaService.saveSchema(collectionName, fieldValidation);
    }

    /**
     * cast FieldPropertyDTO to FieldValidation
     *
     * @param schemaField destination object
     * @param dto         source object
     */

    private void castDtoToValidation(FieldValidation schemaField, FieldPropertyDTO dto) {
        schemaField.setMinimum(dto.getMinimum() == null ? null : dto.getMinimum().doubleValue());
        schemaField.setMaximum(dto.getMaximum() == null ? null : dto.getMaximum().doubleValue());
        schemaField.setEnums(dto.getEnums());
        schemaField.setPattern(dto.getPattern());
        schemaField.setDescription(dto.getDescription());
        schemaField.setType(Collections.singleton(dto.getType()));
    }

    /**
     * get nested schema of field from parent schema
     *
     * @param schema          parent schema
     * @param nestedFieldName nested field name
     * @return schema of field name
     */

    private FieldValidation getTargetSchema(FieldValidation schema, String[] nestedFieldName) {
        if (nestedFieldName != null)
            for (String fieldName : nestedFieldName) {
                if (schema.getProperties() == null)
                    schema.setProperties(new HashMap<>());
                if (!schema.getProperties().containsKey(fieldName))
                    schema.getProperties().put(fieldName, FieldValidation.builder().properties(new HashMap<>()).build());
                schema = schema.getProperties().get(fieldName);
            }
        return schema;
    }

}
