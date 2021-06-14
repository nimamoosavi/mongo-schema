package com.nicico.mongoschema.operation;

import com.nicico.mongoschema.schema.FieldValidation;
import org.bson.Document;

import java.util.Set;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public interface MongoSchemaOperations {
    public Document saveSchema(String collectionName, Document schema);


    public Document getSchema(String collectionName);


    public Document getFieldValidation(String collectionName, String fieldName);

    public Document getFieldValidation(String collectionName);

    public Document saveFieldValidation(String collectionName, String fieldName, FieldValidation fieldValidation);

    public Set<String> getRequiredField(String collectionName, String nestedFieldName);

    public void setRequiredField(String collectionName, String nestedFieldName, Set<String> requiredField);
}
