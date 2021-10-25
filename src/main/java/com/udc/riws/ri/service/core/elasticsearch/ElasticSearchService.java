package com.udc.riws.ri.service.core.elasticsearch;

import com.udc.riws.ri.service.core.dto.GeoLocationDto;
import com.udc.riws.ri.service.core.dto.TweetDto;

import java.util.List;

public interface ElasticSearchService {

    List<TweetDto> searchAll();

    List<TweetDto> searchByUserAndQuery(String user, String query);

    List<TweetDto> searchByCountry(String country, String query);

    List<TweetDto> searchByGeolocation(GeoLocationDto northEast, GeoLocationDto southWest);

    List<TweetDto> searchByGeolocationAndQuery(String query, String user, GeoLocationDto northEast, GeoLocationDto southWest);
}
