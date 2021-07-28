package com.webold.mongoschema.enums;

import com.webold.framework.utility.view.Message;

public enum MongoSchemaException implements Message {
    NOTFOUND{
        @Override
        public String key() {
            return this.name();
        }

    },SCHEMA_CAST{
        @Override
        public String key() {
            return this.name();
        }
    },VALIDATION_SAVE{
        @Override
        public String key() {
            return this.name();
        }
    }
}
