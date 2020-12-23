package com.paulovfe.agendavoting.config;

import com.paulovfe.agendavoting.repository.MongoRepositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackageClasses = MongoRepositories.class)
@EnableMongoAuditing
public class MongoConfiguration {
}
