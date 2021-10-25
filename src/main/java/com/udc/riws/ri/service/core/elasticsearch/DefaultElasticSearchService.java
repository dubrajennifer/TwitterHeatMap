package com.udc.riws.ri.service.core.elasticsearch;

import com.udc.riws.ri.service.core.dto.GeoLocationDto;
import com.udc.riws.ri.service.core.dto.PlaceDto;
import com.udc.riws.ri.service.core.dto.TweetDto;
import com.udc.riws.ri.service.core.dto.UserDto;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class DefaultElasticSearchService implements ElasticSearchService {
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultElasticSearchService.class);

    private final String INDEX_NAME = "chiador";
    private final RestHighLevelClient elasticsearchClient;

    @Autowired
    public DefaultElasticSearchService(@Qualifier("elasticSearchClient") RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }
    @Override
    public List<TweetDto> searchAll() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(10000);
        return sendRequestToElastic(searchRequest);
    }


    @Override
    public List<TweetDto> searchByGeolocation(GeoLocationDto northEast, GeoLocationDto southWest) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);

        QueryBuilder qb = QueryBuilders.geoBoundingBoxQuery("place.geoLocation")
                .setCorners(northEast.getLatitude(), southWest.getLongitude(), southWest.getLatitude(), northEast.getLongitude())
                .setValidationMethod(GeoValidationMethod.IGNORE_MALFORMED)
                .type(GeoExecType.INDEXED);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(10000);
        return sendRequestToElastic(searchRequest);
    }

    @Override
    public List<TweetDto> searchByGeolocationAndQuery(String query, String user, GeoLocationDto northEast, GeoLocationDto southWest) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (query != null && !query.equals("")) {
            qb.must(builderSearchByQuery(query));
        }

        if (user != null && !user.equals("")){
            qb.must(QueryBuilders.matchQuery("user.name",user));
        }
        qb.must(QueryBuilders.geoBoundingBoxQuery("place.geoLocation")
                .setCorners(northEast.getLatitude(), southWest.getLongitude(), southWest.getLatitude(), northEast.getLongitude())
                .setValidationMethod(GeoValidationMethod.IGNORE_MALFORMED)
                .type(GeoExecType.INDEXED));

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(qb);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(10000);
        return sendRequestToElastic(searchRequest);
    }

    @Override
    public List<TweetDto> searchByUserAndQuery(String user, String query) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        if (query != null && !query.equals("")) {
            qb.must(builderSearchByQuery(query));
        }

        if (user != null && !user.equals("")){
            qb.must(QueryBuilders.matchQuery("user.name",user));
        }
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(qb);
        searchSourceBuilder.size(10000);

        searchRequest.source(searchSourceBuilder);
        return sendRequestToElastic(searchRequest);
    }

    @Override
    public List<TweetDto> searchByCountry(String countryCode, String query) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(INDEX_NAME);

        QueryBuilder qb = QueryBuilders
                .boolQuery()
                .must(QueryBuilders.matchQuery("place.countryCode",countryCode))
                .must(builderSearchByQuery(query));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(qb);
        searchSourceBuilder.size(10000);
        searchRequest.source(searchSourceBuilder);

        return sendRequestToElastic(searchRequest);
    }

    private List<TweetDto> sendRequestToElastic(SearchRequest searchRequest){
        try {
            SearchResponse response = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            return convertHitsToTweets(hits);
        } catch (IOException | ParseException e) {
            System.out.println("Couldn't search query");
            e.printStackTrace();
        }
        return null;
    }

    private QueryBuilder builderSearchByQuery(String query){
        return QueryBuilders.matchQuery("content",query).operator(Operator.AND).fuzzyTranspositions(false).autoGenerateSynonymsPhraseQuery(false);
    }


    private List<TweetDto> convertHitsToTweets(SearchHit[] hits) throws ParseException {
        List<TweetDto> tweets = new ArrayList<>();
        for (SearchHit hit: hits){
            tweets.add(getTweetDto(hit.getSourceAsMap()));
        }
        LOGGER.info("Tweets {}", tweets.size());

        return tweets;
    }



    private TweetDto getTweetDto(Map<String, Object> tweet) throws ParseException {
        TweetDto tweetDto = new TweetDto();
        tweetDto.setId((Long)tweet.get("id"));
        tweetDto.setContent((String) tweet.get("content"));
        tweetDto.setLang((String) tweet.get("language"));
        tweetDto.setUser(getUserDto((Map<String, Object>) tweet.get("user")));
        tweetDto.setCreatedAt(new Date((long) tweet.get("createTime")));
        tweetDto.setFavorites((Integer) tweet.get("favourites"));
        tweetDto.setRetweets((Integer) tweet.get("retweets"));
        tweetDto.setPlace(getPlaceDto((Map<String, Object>) tweet.get("place")));
        return tweetDto;

    }

    private UserDto getUserDto(Map<String, Object> user){
        if (user != null) {
            UserDto userDto = new UserDto();
            userDto.setId(Long.parseLong(String.valueOf(user.get("id"))));
            userDto.setName((String) user.get("name"));
            userDto.setEmail((String) user.get("email"));
            userDto.setLang((String) user.get("language"));
            userDto.setLocation((String) user.get("location"));
            return userDto;
        }else{
            return null;
        }
    }


    private PlaceDto getPlaceDto(Map<String, Object> place){
        if (place != null) {
            PlaceDto placeDto = new PlaceDto();
            placeDto.setId((String) place.get("id"));
            placeDto.setName((String) place.get("name"));
            placeDto.setGeoLocation(getGeolocationDto((Map<String, Object>) place.get("geoLocation")));
            placeDto.setFullName((String) place.get("fullname"));
            placeDto.setStreetAddress((String) place.get("streetAddress"));
            placeDto.setCountry((String) place.get("country"));
            placeDto.setPlaceType((String) place.get("placeType"));
            placeDto.setCountryCode((String) place.get("countryCode"));
            return placeDto;
        }else{
            return null;
        }
    }

    private GeoLocationDto getGeolocationDto(Map<String, Object> geolocation){
        if (geolocation != null){
            GeoLocationDto geoLocationDto = new GeoLocationDto();
            geoLocationDto.setLatitude((Double)  geolocation.get("lat"));
            geoLocationDto.setLongitude((Double)  geolocation.get("lon"));
            return geoLocationDto;
        }else{
            return null;
        }
    }


}
