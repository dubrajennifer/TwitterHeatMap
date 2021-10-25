package com.udc.riws.ri.service.core.twitter;

import com.udc.riws.ri.service.core.dto.GeoLocationDto;
import com.udc.riws.ri.service.core.dto.PlaceDto;
import com.udc.riws.ri.service.core.dto.TweetDto;
import com.udc.riws.ri.service.core.dto.UserDto;
import com.udc.riws.ri.service.core.elasticsearch.ElasticPersistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import twitter4j.*;

@RestController
public class ExternalApiTwitterService implements TwitterService {
    private final TwitterStream twitterStream;

    private final ElasticPersistService elasticPersistService;

    @Autowired
    public ExternalApiTwitterService(TwitterStream twitterStream, ElasticPersistService elasticPersistService) {
        this.twitterStream = twitterStream;
        this.elasticPersistService = elasticPersistService;
    }

    @RequestMapping("/start")
    public Boolean start(){
        this.retrieveAndIndexTweets();
        return true;
    }


    @Override
    public void retrieveAndIndexTweets() {
        createStateListener();
        twitterStream.sample();
    }

    private void createStateListener(){
        StatusListener listener = new StatusListener() {
            @Override
            public void onStatus(Status status) {

                GeoLocationDto geoLocationDto = null;
                PlaceDto place = null;

                if (status.getPlace() != null){
                    GeoLocationDto placeGeoLocationDto = null;
                    if (status.getPlace().getBoundingBoxCoordinates() != null){
                        placeGeoLocationDto = getMiddlePointGeoLocation(status.getPlace().getBoundingBoxCoordinates());
                    }
                    place = new PlaceDto(status.getPlace().getId(), status.getPlace().getName(), placeGeoLocationDto, status.getPlace().getStreetAddress(), status.getPlace().getCountryCode(), status.getPlace().getCountry(), status.getPlace().getPlaceType(), status.getPlace().getFullName());
                }

                if (status.getGeoLocation() != null){
                    geoLocationDto = new GeoLocationDto(status.getGeoLocation().getLatitude(), status.getGeoLocation().getLongitude());
                }

                UserDto user = new UserDto(status.getUser().getId(), status.getUser().getName(), status.getUser().getEmail(), status.getUser().getLang(), status.getUser().getLocation());
                TweetDto tweet = new TweetDto(status.getId(), status.getText(), user, status.getCreatedAt(), status.getLang(), geoLocationDto, place, status.getFavoriteCount(), status.getRetweetCount());
                if (elasticPersistService.indexTweet(tweet)){
                    System.out.println("Tweet "+tweet.getId()+" indexed");
                }
            }
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }
            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }
            @Override
            public void onScrubGeo(long userId, long upToStatusId) {
            }
            @Override
            public void onStallWarning(StallWarning warning) {
            }
            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
    }

    private GeoLocationDto getMiddlePointGeoLocation(GeoLocation[][] boundingBox){
        double lat1 = boundingBox[0][0].getLatitude();
        double long1 = boundingBox[0][0].getLongitude();
        double lat2 = boundingBox[0][2].getLatitude();
        double long2 = boundingBox[0][2].getLongitude();
        double middleLat = (Double.max(lat1,lat2) - Double.min(lat1,lat2))/2;
        double middleLong = (Double.max(long1,long2) - Double.min(long1,long2))/2;
        return new GeoLocationDto(Double.min(lat1,lat2)+middleLat, Double.min(long1,long2) + middleLong);
    }

}
