package com.nicico.mongoschema.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:mongo-schema-exceptions.properties", encoding = "UTF-8", ignoreResourceNotFound = true)
public class MongoSchemaConfig {
}
