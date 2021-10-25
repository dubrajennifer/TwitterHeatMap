package com.udc.riws.ri.controller;

import com.udc.riws.ri.service.core.dto.TweetDto;
import com.udc.riws.ri.service.core.elasticsearch.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
public class TweetSearcherController {
    private final Logger LOGGER = LoggerFactory.getLogger(TweetSearcherController.class);

    private final ElasticSearchService defaultElasticSearchService;

    @Autowired
    public TweetSearcherController(ElasticSearchService defaultElasticSearchService) {
        this.defaultElasticSearchService = defaultElasticSearchService;
    }

    @RequestMapping("/")
    public ModelAndView tweeterHome(Model model){
        return new ModelAndView("search-by-query");
    }


    @RequestMapping("/chios/buscar")
    public ModelAndView getTweets(Model model, @QueryParam("query") String query, @QueryParam("user") String user){
        List<TweetDto> tweetDtos;
        tweetDtos = defaultElasticSearchService.searchByUserAndQuery(user, query);
        model.addAttribute("tweets", tweetDtos);
        model.addAttribute("queryStr", query);
        model.addAttribute("userStr", user);
        return new ModelAndView("search-by-query");
    }

    @RequestMapping("/chios")
    public ModelAndView getMain(){
        return new ModelAndView("search-by-query");
    }


}
