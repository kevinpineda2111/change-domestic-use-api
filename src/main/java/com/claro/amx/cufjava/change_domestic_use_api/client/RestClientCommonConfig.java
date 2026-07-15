package com.claro.amx.cufjava.change_domestic_use_api.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestClientCommonConfig {
    @Bean
    public CommonRestClient getLineInfo(@Value("${api.common-cuf.url}") String url) {
        return new CommonRestClient(url);
    }
}
