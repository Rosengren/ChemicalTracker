package com.chemicaltracker.persistence;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.AmazonClientException;

import org.springframework.core.GenericTypeResolver;

import org.apache.log4j.Logger;

public abstract class DynamoDBDAO<T> implements GenericDAO<T> {

    protected Class<T> clazz;

	protected final Logger logger = Logger.getLogger(getClass());

    protected DynamoDBMapper mapper;
    protected AmazonDynamoDBClient amazonDynamoDBClient;

 	protected DynamoDBDAO() {
        this.clazz = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), DynamoDBDAO.class);

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
    	return (T) mapper.load(this.clazz, id);
    }

    @Override
    public T find(final Object hash, final Object range) {
        return (T) mapper.load(this.clazz, hash, range);
    }

    @Override
    public void delete(final T t) {
    	mapper.delete(t);
    }
}