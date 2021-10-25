package com.udc.riws.ri.controller;

import com.udc.riws.ri.service.core.dto.GeoLocationDto;
import com.udc.riws.ri.service.core.dto.TweetDto;
import com.udc.riws.ri.service.core.elasticsearch.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TweetMapController {
    private final Logger LOGGER = LoggerFactory.getLogger(TweetMapController.class);

    private final ElasticSearchService defaultElasticSearchService;
    private List<TweetDto> tweets;
    private String query;
    private String user;

    @Autowired
    public TweetMapController(ElasticSearchService defaultElasticSearchService) {
        this.defaultElasticSearchService = defaultElasticSearchService;
    }


    @RequestMapping(value="/chios/geolocalizar", method = RequestMethod.GET)
    public ModelAndView getRefreshedTweetMap(Model model, @QueryParam("neLat") String neLat, @QueryParam("neLon") String neLon, @QueryParam("soLat") String soLat, @QueryParam("soLon") String soLon){
        tweets = defaultElasticSearchService.searchByGeolocation(new GeoLocationDto(Double.parseDouble(neLat),Double.parseDouble(neLon)),  new GeoLocationDto(Double.parseDouble(soLat), Double.parseDouble(soLon)));
        model.addAttribute("tweets", tweets);
        return new ModelAndView("tweets :: tweets-fragment");
    }

    @RequestMapping(value="/chios/geolocalizar/buscar", method = RequestMethod.GET)
    public ModelAndView getRefreshedTweetMapByQuery(Model model, @QueryParam("neLat") String neLat, @QueryParam("neLon") String neLon, @QueryParam("soLat") String soLat, @QueryParam("soLon") String soLon){
        tweets = defaultElasticSearchService.searchByGeolocationAndQuery(query, user, new GeoLocationDto(Double.parseDouble(neLat),Double.parseDouble(neLon)),  new GeoLocationDto(Double.parseDouble(soLat), Double.parseDouble(soLon)));
        model.addAttribute("tweets", tweets);
        return new ModelAndView("tweets :: tweets-fragment");
    }

    @GetMapping("/chios/mapa")
    public ModelAndView getTweetMap(Model model){
        tweets = defaultElasticSearchService.searchAll();
        model.addAttribute("points", getGeolocationPoints(tweets));
        return new ModelAndView("map-and-tweets");
    }

    @GetMapping("/chios/mapa/buscar")
    public ModelAndView getTweetMapByQuery(Model model, @QueryParam("query") String query, @QueryParam("user") String user){
        this.query = query;
        this.user = user;
        tweets = defaultElasticSearchService.searchByUserAndQuery(user, this.query);
        model.addAttribute("points", getGeolocationPoints(tweets));
        return new ModelAndView("map-and-tweets-query");
    }

    private List<GeoLocationDto> getGeolocationPoints(List<TweetDto> tweets){
        List<GeoLocationDto> geoLocations = new ArrayList<>();
        if (tweets != null && !tweets.isEmpty()) {
            tweets.forEach(tweet -> geoLocations.add(tweet.getPlace().getGeoLocation()));
        }
        return geoLocations;
    }

}
