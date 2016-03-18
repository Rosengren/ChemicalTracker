package com.chemicaltracker.persistence.dao;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import org.apache.commons.lang.NotImplementedException;
import org.springframework.core.GenericTypeResolver;

import org.apache.log4j.Logger;

public abstract class ElasticSearchDao<T> implements GenericDao<T> {

    protected Class<T> clazz;

    protected final Logger logger = Logger.getLogger(getClass());

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
        throw new NotImplementedException("Create method not implemented");
    }

    @Override
    public T update(final T t)  {
        throw new NotImplementedException("Update method not implemented");
    }

    @Override
    public T find(final Object id) {
        throw new NotImplementedException("Find method not implemented");
    }

    @Override
    public T find(final Object hash, final Object range) {
        throw new NotImplementedException("Find Hash and Range not implemented");
    }

    @Override
    public void delete(final T t) {
        throw new NotImplementedException("Delete method not implemented");
    }
}