package app.ladderproject.mongoschema.schema;

import org.bson.Document;

/**
 * MongoDb Schema Service
 * fetch and save mongoDB schema
 *
 * @author Hossien Mahdevar
 * @version 1.0.0
 */
public interface MongoDbSchemaService {
    String MONGO_OPTION = "options";
    String MONGO_VALIDATOR = "validator";
    String MONGO_SCHEMA = "$jsonSchema";
    String MONGO_PROPERTIES = "properties";
    String REQUIRED = "required";
    String MONGO_COLL_MOD = "collMod";

    /**
     * get Json Schema as Pojo
     *
     * @param collectionName collection name
     * @return schema pojo
     */
    FieldValidation getJsonSchema(String collectionName);

    /**
     * get Json Schema as document
     *
     * @param collectionName collection name
     * @return schema document
     */
    Document getJsonSchemaDocument(String collectionName);

    /**
     * save schema document
     *
     * @param collectionName collection name
     * @param schema         schema document
     * @return schema document
     */
    Document saveSchema(String collectionName, Document schema);

    /**
     * save schema FieldValidation
     *
     * @param collectionName        collection name
     * @param schemaFieldValidation schema field validation
     * @return schema document
     */
    Boolean saveSchema(String collectionName, FieldValidation schemaFieldValidation);


    /**
     * remove collection schema
     *
     * @param collectionName collection name
     */
    void drop(String collectionName);
}
