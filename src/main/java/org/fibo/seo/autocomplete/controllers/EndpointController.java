package org.fibo.seo.autocomplete.controllers;

import org.fibo.seo.autocomplete.config.GlobalConfig;
import org.fibo.seo.autocomplete.dto.ScoredKeywordDto;
import org.fibo.seo.autocomplete.services.remote.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
public class EndpointController {

    @Autowired
    AnalyzerService analyzerService;

    @Autowired
    GlobalConfig globalConfig;

    @GetMapping("/estimate")
    @ResponseBody
    public Mono<ScoredKeywordDto> estimate(@RequestParam(name="keyword") String keyword) {
        return  Mono.fromCallable(() ->
                new ScoredKeywordDto(keyword, analyzerService.score(keyword)))
                .timeout(Duration.ofSeconds(globalConfig.timeoutSec));
    }
}
