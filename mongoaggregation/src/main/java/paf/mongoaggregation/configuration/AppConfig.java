package paf.mongoaggregation.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import static paf.mongoaggregation.model.Constants.*;

@Configuration
public class AppConfig {

    //Mongo connection string
    @Value ("${mongo.url}")
    private String mongoUrl;

    //Create and initialize Mongo Template
    @Bean
    public MongoTemplate createMongoTemplate(){

        //Create a MongoClient with the mongo connection string
        MongoClient client = MongoClients.create(mongoUrl);
        return new MongoTemplate(client, DATABASE);

    } 
    
}