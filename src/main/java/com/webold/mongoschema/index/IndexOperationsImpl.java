package com.webold.mongoschema.index;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @author Hossein Mahdevar
 * @version 1.0.0
 */

@AllArgsConstructor
public class IndexOperationsImpl implements IndexOperations {
    private MongoDatabase mongoDatabase;

    @Override
    public String createIndex(String collectionName, Index index) {
        return mongoDatabase.getCollection(collectionName).createIndex(new Document(index.getKey(), index.getDirection()), index);
    }

    @Override
    public String createIndex(String collectionName, String fieldName) {
        return this.createIndex(collectionName, Index.builder()
                .ascending()
                .key(fieldName)
                .name(collectionName.concat("_").concat(fieldName))
                .unique(true)
                .sparse(false)
                .build());
    }

    @Override
    public List<Index> getIndexes(String collectionName) {
        return StreamSupport.stream(mongoDatabase.getCollection(collectionName).listIndexes().spliterator(), false).map(this::covertFromDocument).collect(Collectors.toList());
    }

    @Override
    public Optional<Index> getIndex(String collectionName, String indexName) {
        return this.getIndexes(collectionName).stream().filter(index -> index.getName().equals(indexName)).findFirst();
    }

    @Override
    public void dropIndex(String collectionName, String indexName) {
        mongoDatabase.getCollection(collectionName).dropIndex(indexName);
    }

    @Override
    public void dropIndexes(String collectionName) {
        mongoDatabase.getCollection(collectionName).dropIndexes();

    }

    public Index covertFromDocument(Document document) {
        Index.IndexBuilder builder = Index.builder();
        if (document.containsKey("key"))
            builder.key(document.get("key", Document.class).keySet().toArray()[0].toString());
        if (document.containsKey("expireAfter"))
            builder.expireAfterSeconds(document.get("expireAfter", Long.class));
        if (document.containsKey("textVersion"))
            builder.textVersion(document.get("textVersion", Integer.class));
        if (document.containsKey("languageOverride"))
            builder.languageOverride(document.get("languageOverride", String.class));
        if (document.containsKey("collation"))
            builder.collation(document.get("collation", Collation.class));
        if (document.containsKey("weights"))
            builder.weights(document.get("weights", Bson.class));
        if (document.containsKey("wildcardProjection"))
            builder.wildcardProjection(document.get("wildcardProjection", Bson.class));
        if (document.containsKey("min"))
            builder.min(document.get("min", Double.class));
        if (document.containsKey("sparse"))
            builder.sparse(document.get("sparse", Boolean.class));
        if (document.containsKey("name"))
            builder.name(document.get("name", String.class));
        if (document.containsKey("partialFilterExpression"))
            builder.partialFilterExpression(document.get("partialFilterExpression", Bson.class));
        if (document.containsKey("defaultLanguage"))
            builder.defaultLanguage(document.get("defaultLanguage", String.class));
        return builder.build();

    }
}
