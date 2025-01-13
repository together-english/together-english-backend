package com.together_english.deiz.chat.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@Configuration
@EnableMongoRepositories(basePackages = ["com.together_english.deiz.chat.message.repository"])
class MongoConfig(
    private val mongoProperties: MongoProperties,
) {
    @Bean
    fun mongoClient(): MongoClient {
        return MongoClients.create(mongoProperties.uri)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), mongoProperties.database)
    }
}