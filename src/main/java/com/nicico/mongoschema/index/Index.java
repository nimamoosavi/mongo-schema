package com.nicico.mongoschema.index;

import com.mongodb.client.model.Collation;
import com.mongodb.client.model.IndexOptions;
import lombok.*;
import org.bson.conversions.Bson;

import java.util.concurrent.TimeUnit;

/**
 * simple and custom implement of IndexDefinition
 * @author Hossein Mahdevar
 * @version 1.0.0
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Index extends IndexOptions {
    private String key;
    private Integer direction=1;
    public void ascending(){
        this.direction=1;
    }
    public void descending(){
        this.direction=-1;
    }
    public static IndexBuilder builder(){
        return new IndexBuilder();
    }

    public static final class IndexBuilder {
        private String key;
        private boolean background;
        private boolean unique;
        private String name;
        private boolean sparse;
        private Long expireAfterSeconds;
        private Integer version;
        private Bson weights;
        private String defaultLanguage;
        private String languageOverride;
        private Integer textVersion;
        private Integer sphereVersion;
        private Integer bits;
        private Double min;
        private Double max;
        private Double bucketSize;
        private Bson storageEngine;
        private Bson partialFilterExpression;
        private Collation collation;
        private Bson wildcardProjection;
        private Integer direction=1;
        public IndexBuilder ascending(){
            this.direction=1;
            return this;
        }
        public IndexBuilder descending(){
            this.direction=-1;
            return this;
        }

        private IndexBuilder() {
        }

        public static IndexBuilder anIndex() {
            return new IndexBuilder();
        }

        public IndexBuilder key(String key) {
            this.key = key;
            return this;
        }

        public IndexBuilder background(boolean background) {
            this.background = background;
            return this;
        }

        public IndexBuilder unique(boolean unique) {
            this.unique = unique;
            return this;
        }

        public IndexBuilder name(String name) {
            this.name = name;
            return this;
        }

        public IndexBuilder sparse(boolean sparse) {
            this.sparse = sparse;
            return this;
        }

        public IndexBuilder expireAfterSeconds(Long expireAfterSeconds) {
            this.expireAfterSeconds = expireAfterSeconds;
            return this;
        }

        public IndexBuilder version(Integer version) {
            this.version = version;
            return this;
        }

        public IndexBuilder weights(Bson weights) {
            this.weights = weights;
            return this;
        }

        public IndexBuilder defaultLanguage(String defaultLanguage) {
            this.defaultLanguage = defaultLanguage;
            return this;
        }

        public IndexBuilder languageOverride(String languageOverride) {
            this.languageOverride = languageOverride;
            return this;
        }

        public IndexBuilder textVersion(Integer textVersion) {
            this.textVersion = textVersion;
            return this;
        }

        public IndexBuilder sphereVersion(Integer sphereVersion) {
            this.sphereVersion = sphereVersion;
            return this;
        }

        public IndexBuilder bits(Integer bits) {
            this.bits = bits;
            return this;
        }

        public IndexBuilder min(Double min) {
            this.min = min;
            return this;
        }

        public IndexBuilder max(Double max) {
            this.max = max;
            return this;
        }

        public IndexBuilder bucketSize(Double bucketSize) {
            this.bucketSize = bucketSize;
            return this;
        }

        public IndexBuilder storageEngine(Bson storageEngine) {
            this.storageEngine = storageEngine;
            return this;
        }

        public IndexBuilder partialFilterExpression(Bson partialFilterExpression) {
            this.partialFilterExpression = partialFilterExpression;
            return this;
        }

        public IndexBuilder collation(Collation collation) {
            this.collation = collation;
            return this;
        }

        public IndexBuilder wildcardProjection(Bson wildcardProjection) {
            this.wildcardProjection = wildcardProjection;
            return this;
        }

        public Index build() {
            Index index = new Index();
            index.setKey(this.key);
            index.expireAfter(this.expireAfterSeconds, TimeUnit.SECONDS);
            index.textVersion(this.textVersion);
            index.languageOverride(this.languageOverride);
            index.collation(this.collation);
            index.weights(this.weights);
            index.wildcardProjection(this.wildcardProjection);
            index.min(this.min);
            index.sparse(this.sparse);
            index.name(this.name);
            index.partialFilterExpression(this.partialFilterExpression);
            index.defaultLanguage(this.defaultLanguage);
            index.version(this.version);
            index.bucketSize(this.bucketSize);
            index.bits(this.bits);
            index.unique(this.unique);
            index.sphereVersion(this.sphereVersion);
            index.storageEngine(this.storageEngine);
            index.background(this.background);
            index.max(this.max);
            index.setDirection(this.direction);
            return index;
        }
    }
}
