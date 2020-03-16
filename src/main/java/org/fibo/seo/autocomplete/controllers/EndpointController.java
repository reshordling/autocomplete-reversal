package org.fibo.seo.autocomplete.controllers;

import org.fibo.seo.autocomplete.dto.ScoredKeywordDto;
import org.fibo.seo.autocomplete.services.remote.AnalyzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {

    @Autowired
    AnalyzerService analyzerService;

    @GetMapping("/estimate")
    @ResponseBody
    public ScoredKeywordDto estimate(@RequestParam(name="keyword") String keyword) {
        long score = analyzerService.score(keyword);
        return new ScoredKeywordDto(keyword, score);
    }
}
