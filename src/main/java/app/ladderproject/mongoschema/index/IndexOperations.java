package app.ladderproject.mongoschema.index;

import java.util.List;
import java.util.Optional;

/**
 * Indexes Operations need on indexes of mongoDB
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
public interface IndexOperations {
    /** 
     * create index with index object
     * @param collectionName Document name of collection  
     * @param index the index object that you want save it
     * @return index name
     */
    public String createIndex(String collectionName, Index index);
    /**
     * just create index with fieldName
     * @param collectionName Document name of collection  
     * @param fieldName the fieldName that you want set index on it
     * @return index name
     * Note: default implementation set unique=true , sparse=false , indexName=collectionName_fieldName ,direction=ASC
     */
    public String createIndex(String collectionName, String fieldName);

    /**
     * get All indexes of collection
     * @param collectionName Document name of collection  
     * @return list of all index on collection
     */
    public List<Index> getIndexes(String collectionName);

    /**
     * get index of collection with index name
     * @param collectionName Document name of collection  
     * @param indexName  index name
     * @return if index exist return that
     */
    public Optional<Index> getIndex(String collectionName, String indexName);

    /**
     * drop index of collection with index name
     * @param collectionName Document name of collection  
     * @param indexName index name 
     */
    public void dropIndex(String collectionName, String indexName);

    /**
     * drop all indexes of collection
     * @param collectionName Document name of collection  
     */
    public void dropIndexes(String collectionName);

}
