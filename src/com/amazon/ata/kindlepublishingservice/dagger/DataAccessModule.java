package com.amazon.ata.kindlepublishingservice.dagger;

import com.amazon.ata.kindlepublishingservice.dao.CatalogDao;
import com.amazon.ata.kindlepublishingservice.dao.PublishingStatusDao;
import com.amazon.ata.kindlepublishingservice.publishing.BookPublishRequestManager;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import dagger.Module;
import dagger.Provides;
import org.checkerframework.checker.units.qual.C;

import javax.inject.Singleton;

@Module
public class DataAccessModule {

    @Singleton
    @Provides
    public DynamoDBMapper provideDynamoDBMapper() {
        AWSCredentialsProvider provider = new ProfileCredentialsProvider("default");
        AmazonDynamoDB amazonDynamoDBClient = AmazonDynamoDBClientBuilder.standard()
            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
            .withRegion(Regions.US_WEST_2)
            .build();

        return new DynamoDBMapper(amazonDynamoDBClient);
    }

    public CatalogDao provideCatalogDao() {
        return new CatalogDao(provideDynamoDBMapper());
    }

    public PublishingStatusDao providePublishingStatusDao() {
        return new PublishingStatusDao(provideDynamoDBMapper());
    }
}
