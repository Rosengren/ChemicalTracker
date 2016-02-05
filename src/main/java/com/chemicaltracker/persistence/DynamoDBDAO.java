package com.chemicaltracker.persistence;


// TODO: remove all unused imports
import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;

import java.util.List;
import java.util.ArrayList;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.DeleteItemRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteItemResult;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.Item;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;

public abstract class DynamoDBDAO<T> implements GenericDAO<T> {

	protected final Logger logger = Logger.getLogger(this.getClass());

    protected DynamoDBMapper mapper;
    protected AmazonDynamoDBClient amazonDynamoDBClient;

 	protected DynamoDBDAO() {

		try {
            initializeDBConnection();
        } catch (Exception e) {
            logger.error("Error occured while initializing DB Connection", e);
        }
    }


    public void initializeDBConnection() throws AmazonClientException {

        AWSCredentials credentials = null;

        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Could not load credentials", e);
        }

        try {
            amazonDynamoDBClient = new AmazonDynamoDBClient(credentials);
        } catch (Exception e) {
            throw new AmazonClientException("The provided credentials were not valid", e);
        }

        amazonDynamoDBClient.setRegion(Region.getRegion(Regions.US_WEST_2));
        mapper = new DynamoDBMapper(amazonDynamoDBClient);
    }

    public abstract Class getObjectClass();

    @Override
    public T create(final T t) {
    	mapper.save(t);
        return t;
    }

    @Override
    public T update(final T t) {
    	mapper.save(t);
        return t;
    }

    @Override
    public T find(final Object id) {
    	return (T) mapper.load(getObjectClass(), id);
    }

    @Override
    public T find(final Object hash, final Object range) {
        return (T) mapper.load(getObjectClass(), hash, range);
    }

    @Override
    public void delete(final T t) {
    	mapper.delete(t);
    }
}