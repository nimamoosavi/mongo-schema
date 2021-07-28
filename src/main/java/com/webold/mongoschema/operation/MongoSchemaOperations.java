package com.webold.mongoschema.operation;

import com.webold.mongoschema.schema.FieldValidation;
import org.bson.Document;

import java.util.Map;
import java.util.Set;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public interface MongoSchemaOperations {
    public static final String MONGO_FIELD_NAME_SEPARATOR = "\\.";

    /**
     *save schema
     * @param collectionName collection name
     * @param schema schema
     * @return if operation success contain "ok" field
     */
    public Document saveSchema(String collectionName, Document schema);

    /**
     * get schema as document
     * @param collectionName collection name
     * @return schema as document
     */
    public Document getSchema(String collectionName);

    /**
     * get validations on nested field of collection
     * @param collectionName collection name
     * @param fieldName field name separate nested with "."
     * @return
     */
    public Document getFieldValidation(String collectionName, String fieldName);

    /**
     * get validation on collection
     * @param collectionName collection name
     * @return validation as document
     */

    public Document getFieldValidation(String collectionName);

    /**
     * save collection document
     * @param collectionName collection name
     * @param fieldName  field name separate nested with "."
     * @param fieldValidation validation needs on field
     * @return changed schema of collection
     */
    public Document saveFieldValidation(String collectionName, String fieldName, FieldValidation fieldValidation);

    /**
     * set field required
     * @param collectionName collection name
     * @param nestedFieldName field name separate nested with "."
     * @return set of required field
     */
    public Set<String> getRequiredField(String collectionName, String nestedFieldName);

    /**
     * set required field on nested document
     * @param collectionName collection name
     * @param nestedFieldName field name separate nested with "."
     * @param requiredField set of required field
     */

    public void setRequiredField(String collectionName, String nestedFieldName, Set<String> requiredField);
    /**
     * save schema with simple dto
     * @param collectionName collection name
     * @param schemaFieldValidation key is fieldName and value property of field
     * @return success
     */
    public Boolean saveSchema(String collectionName, Map<String, FieldPropertyDTO> schemaFieldValidation);
}
