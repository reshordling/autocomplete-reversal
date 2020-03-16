package org.fibo.seo.autocomplete.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalConfig {

    // TODO move to properties
    // default number of keywords. NB the provider offers 11 results, not 10
    // as we pretend to be a human being, then use the default value
    public Integer searchLimit = 11;
    // no more than sec per request
    public Integer timeoutSec = 9;
    // no more than N steps in a analysis
    public Integer maxDepth = 7;
}
