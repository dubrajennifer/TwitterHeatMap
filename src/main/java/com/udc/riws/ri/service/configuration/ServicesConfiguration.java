package com.udc.riws.ri.service.configuration;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
@ComponentScan(basePackages = "com.udc.riws.ri.service")
public class ServicesConfiguration {

    @Bean(name="elasticSearchClient")
    public RestHighLevelClient elasticSearchClient() {

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("es", 9200, "http")));
    }
    @Bean(name="twitterStream")
    public TwitterStream twitterStream() {
        final String CK = "XXXXX";
        final String CS = "XXXXX";
        final String AT = "XXXXX";
        final String ATS = "XXXXX";

        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(CK)
                .setOAuthConsumerSecret(CS)
                .setOAuthAccessToken(AT)
                .setOAuthAccessTokenSecret(ATS)
                .setJSONStoreEnabled(true);
        return (new TwitterStreamFactory(configurationBuilder.build())).getInstance();
    }
}
