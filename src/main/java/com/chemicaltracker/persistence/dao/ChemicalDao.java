package com.chemicaltracker.persistence.dao;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.chemicaltracker.persistence.model.Chemical;
import io.searchbox.core.Bulk;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class ChemicalDao extends ElasticSearchDao<Chemical> {

    private static final String CHEMICALS_INDEX = "chemicals";
    private static final String CHEMICALS_TYPE = "chemical";
    private static final String CHEMICAL_NAME_FIELD = "Name";

    private static volatile ChemicalDao instance;

    public static ChemicalDao getInstance() {

        if (instance == null) {
            synchronized (ChemicalDao.class) {
                if (instance == null) {
                    instance = new ChemicalDao();
                }
            }
        }

        return instance;
    }

    public List<Chemical> searchPartialChemicalName(final List<String> partialNames) {

        final List<Chemical> chemicals = new ArrayList<>();
        if (partialNames == null || partialNames.isEmpty()) {
            return chemicals;
        }

        // This should be replaced with a single multi-query
        // Bulk search is not supported as of this writing.
        for (String partial : partialNames) {
            chemicals.addAll(searchPartialChemicalName(partial));
        }

        return chemicals;
    }

    public List<Chemical> searchPartialChemicalName(final String partialName) {

        final List<Chemical> chemicals = new ArrayList<>();
        if (partialName == null || partialName.isEmpty()) {
            return chemicals;
        }


        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.wildcardQuery(CHEMICAL_NAME_FIELD, "*" + partialName + "*"));

        final Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(CHEMICALS_INDEX)
                .addType(CHEMICALS_TYPE)
                .build();

        try {
            SearchResult result = client.execute(search);
            return parseChemicals(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return chemicals;
    }

    public List<Chemical> findByNames(final List<String> names) {

        final List<Chemical> chemicals = new ArrayList<>();
        if (names == null || names.isEmpty()) {
            return chemicals;
        }

        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        try {
            Search search;
            for (String name : names) {
                searchSourceBuilder.query(QueryBuilders.matchPhraseQuery(CHEMICAL_NAME_FIELD, name));
                search = new Search.Builder(searchSourceBuilder.toString())
                        .addIndex(CHEMICALS_INDEX)
                        .addType(CHEMICALS_TYPE)
                        .build();

                SearchResult result = client.execute(search);
                chemicals.add(result.getFirstHit(Chemical.class).source);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return chemicals;
    }

    public Chemical find(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }

        final SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(CHEMICAL_NAME_FIELD, name));

        final Search search = new Search.Builder(searchSourceBuilder.toString())
                .addIndex(CHEMICALS_INDEX)
                .addType(CHEMICALS_TYPE)
                .build();

        try {
            SearchResult result = client.execute(search);
            return result.getFirstHit(Chemical.class).source;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<Chemical> parseChemicals(SearchResult result) {
        return result.getHits(Chemical.class)
                .stream()
                .map(hit -> hit.source)
                .collect(Collectors.toList());
    }
}
