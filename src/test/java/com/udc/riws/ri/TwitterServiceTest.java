package com.udc.riws.ri;

import com.udc.riws.ri.service.core.elasticsearch.DefaultElasticPersistService;
import com.udc.riws.ri.service.core.elasticsearch.ElasticPersistService;
import com.udc.riws.ri.service.core.twitter.ExternalApiTwitterService;
import com.udc.riws.ri.service.core.twitter.TwitterService;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterServiceTest {
    private final ElasticPersistService elasticsearchPersistService = new DefaultElasticPersistService(new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"))));
    @Test
    public void httpGetTweetsTest() throws Exception {
        final String CK = "3WYg6seG0Eq6bNsFR4C1sRSTW";
        final String CS = "2XhXfcBYNo4HsGbxPhN5FerhPIsRs6pdFYl0LfV16W5hmyk4ls";
        final String AT = "1317193597616349184-xnYujgJI9uUlNdXbe8PuHDNeM1hfX5";
        final String ATS = "GiIYHZ5t4M5sqPJnpEqAl4jM85dXy1kX324mD5v15Jujx";

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(CK)
                .setOAuthConsumerSecret(CS)
                .setOAuthAccessToken(AT)
                .setOAuthAccessTokenSecret(ATS)
                .setJSONStoreEnabled(true);
        final TwitterService twitterService = new ExternalApiTwitterService(new TwitterStreamFactory(configurationBuilder.build()).getInstance(), elasticsearchPersistService);
        twitterService.retrieveAndIndexTweets();
        //DESCOMENTAR PARA PROBAR EN TIEMPO REAL
        /*while (true){

        }*/
    }

}
