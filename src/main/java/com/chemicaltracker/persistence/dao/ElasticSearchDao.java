package com.chemicaltracker.persistence.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.springframework.core.GenericTypeResolver;

import org.apache.log4j.Logger;

public abstract class ElasticSearchDao<T> implements GenericDao<T> {

    protected Class<T> clazz;

    protected final Logger LOGGER = Logger.getLogger(getClass());

    protected JestClient client;

    @SuppressWarnings("unchecked")
    protected ElasticSearchDao() {
        this.clazz = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), DynamoDBDao.class);
        initialize();
    }


    public void initialize() {

        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(new HttpClientConfig
                .Builder("https://search-chemicals-dc6hr76bx464tyxrtuwajnoki4.us-west-2.es.amazonaws.com/") // TODO: remove endpoint
                .multiThreaded(true)
                .build());

        client = factory.getObject();
    }

    @Override
    public T create(final T t) {
        LOGGER.error("Create method not implemented");
        return null;
    }

    @Override
    public T update(final T t)  {
        LOGGER.error("Update method not implemented.");
        return null;
    }

    @Override
    public T find(final Object id) {
        LOGGER.error("Find method not implemented");
        return null;
    }

    @Override
    public T find(final Object hash, final Object range) {
        LOGGER.error("Find Hash and Range not implemented");
        return null;
    }

    @Override
    public void delete(final T t) {
        LOGGER.error("Delete method not implemented");
    }
}