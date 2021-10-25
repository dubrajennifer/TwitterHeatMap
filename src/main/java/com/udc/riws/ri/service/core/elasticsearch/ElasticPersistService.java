package com.udc.riws.ri.service.core.elasticsearch;

import com.udc.riws.ri.service.core.dto.TweetDto;

import java.util.List;

public interface ElasticPersistService {

    Boolean bulkLastTweets();

    void indexTweets(List<TweetDto> tweets);

    void addTweetToBulkRequest(TweetDto tweet);

    Boolean indexTweet(TweetDto tweet);
}
