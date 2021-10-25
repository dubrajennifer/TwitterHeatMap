package com.udc.riws.ri.service.core.elasticsearch;

import com.udc.riws.ri.service.core.dto.GeoLocationDto;
import com.udc.riws.ri.service.core.dto.PlaceDto;
import com.udc.riws.ri.service.core.dto.TweetDto;
import com.udc.riws.ri.service.core.dto.UserDto;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RestController
public class DefaultElasticPersistService implements ElasticPersistService {
    private final RestHighLevelClient elasticsearchClient;
    private final String INDEX_NAME = "chiador";
    private BulkRequest bulkRequest = new BulkRequest();


    @Autowired
    public DefaultElasticPersistService(@Qualifier("elasticSearchClient") RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    @RequestMapping("/bulk/last")
    public Boolean bulkLastTweets() {
        boolean successful = sendRequestToElastic(bulkRequest);
        if (successful){
            bulkRequest = new BulkRequest();
            return true;
        }
        return false;
    }

    @Override
    public void indexTweets(List<TweetDto> tweets) {
         tweets.forEach(this::indexTweet);
    }

    @Override
    public void addTweetToBulkRequest(TweetDto tweet) {
            bulkRequest.add(getTweetIndexRequest(tweet));
    }

    @Override
    public Boolean indexTweet(TweetDto tweet) {
        if (tweet.getPlace() != null) {
            return sendRequestToElastic(new BulkRequest().add(getTweetIndexRequest(tweet)));
        }
        return false;
    }

    private Boolean sendRequestToElastic(BulkRequest request){
        try {
            BulkResponse response = elasticsearchClient.bulk(request, RequestOptions.DEFAULT);
            return !response.hasFailures();
        } catch (IOException e) {
            System.out.println("ERROR: Failure indexing tweets");
            e.printStackTrace();
            return false;
        }
    }


    private IndexRequest getTweetIndexRequest(TweetDto tweet){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("id", tweet.getId());
        jsonMap.put("content", tweet.getContent());
        jsonMap.put("language", tweet.getLang());
        jsonMap.put("user", getUserMap(tweet.getUser()));
        jsonMap.put("createTime", tweet.getCreatedAt().getTime());
        jsonMap.put("favourites", tweet.getFavorites());
        jsonMap.put("retweets", tweet.getRetweets());
        jsonMap.put("place", getPlaceMap(tweet.getPlace()));
        jsonMap.put("geolocation", getGeolocationMap(tweet.getGeolocation()));
        return new IndexRequest(INDEX_NAME).id(tweet.getId().toString()).source(jsonMap);

    }

    private Map<String, Object> getUserMap(UserDto user){
        if (user != null) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("id", user.getId());
            jsonMap.put("name", user.getName());
            jsonMap.put("email", user.getEmail());
            jsonMap.put("language", user.getLang());
            jsonMap.put("location", user.getLocation());
            return jsonMap;
        }else{
            return null;
        }
    }


    private Map<String, Object> getPlaceMap(PlaceDto place){
        if (place != null) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("id", place.getId());
            jsonMap.put("name", place.getName());
            jsonMap.put("geoLocation", getGeolocationMap(place.getGeoLocation()));
            jsonMap.put("fullname", place.getFullName());
            jsonMap.put("streetAddress", place.getStreetAddress());
            jsonMap.put("country", place.getCountry());
            jsonMap.put("placeType", place.getPlaceType());
            jsonMap.put("countryCode", place.getCountryCode());
            return jsonMap;
        }else{
            return null;
        }
    }

    private Map<String, Object> getGeolocationMap(GeoLocationDto geolocation){
        if (geolocation != null){
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("lat", geolocation.getLatitude());
        jsonMap.put("lon", geolocation.getLongitude());
        return jsonMap;
        }else{
            return null;
        }
    }
}
