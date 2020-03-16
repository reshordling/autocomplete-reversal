package org.fibo.seo.autocomplete.services.remote;

import static java.lang.Math.pow;
import static java.lang.Math.round;
import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.JsonNode;

import org.fibo.seo.autocomplete.config.GlobalConfig;
import org.fibo.seo.autocomplete.dto.KeywordWeightDto;
import org.fibo.seo.autocomplete.dto.AutocompleteDto;
import org.fibo.seo.autocomplete.mappers.DtoMapper;

import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

/**
 * The main idea - autocomplete works with balanced N-tree, where all
 * elements on level M are of equal popularity and the popularity of
 * elements on level M+1 is smaller than on level M.
 *
 * The service crawls the tree in depth - only one route, as the tree
 * is balanced.
 *
 * Scoring is based on tree height. There is also heuristics based on
 * the number of found autocomplete elements - e.g. result of 2 elements
 * is scored higher than the result of 1 element.
 *
 * Unfortunately, autocomplete result contains some noise - eg. ware
 * categories (if the same object can be found in different subcategories),
 * similar words, typo autoreplacement etc - usually less than 50% of
 * result.
 */
public class AnalyzerService {

    private final AutocompleteService autocompleteService;
    private final GlobalConfig globalConfig;

    private final KeywordWeightDto EMPTY = new KeywordWeightDto("", 0);

    private final int BASE = 3;

    @Inject
    public AnalyzerService(AutocompleteService autocompleteService, GlobalConfig globalConfig) {
        this.autocompleteService = autocompleteService;
        this.globalConfig = globalConfig;
    }

    public long score(String keyword) {
        int value = crawl(wrap(keyword, 0));
        return calculate100Score(value);
    }

    long calculate100Score(int value) {
        return round(100. * value / pow(BASE, globalConfig.maxDepth));
    }

    private int crawl(Optional<KeywordWeightDto> keywordWeight) {
        int score = 0;
        for (int depth = 0;
             keywordWeight.isPresent() && depth <= globalConfig.maxDepth ;
             depth++) {
            keywordWeight = handleAutocompleteResult(keywordWeight);
            score = score * BASE +
                    keywordWeight.orElse(EMPTY).weight ;
        }
        return score;
    }

    private Optional<KeywordWeightDto> handleAutocompleteResult(
            Optional<KeywordWeightDto> keywordWeight) {
        JsonNode autocompleteJson = autocompleteService.tryGettingTwice(
                keywordWeight.orElse(EMPTY).value);
        List<AutocompleteDto> equallyWeightedVolumes = DtoMapper.map(autocompleteJson);
        return selectLessPopular(equallyWeightedVolumes);
    }

    private Optional<KeywordWeightDto> selectLessPopular(
            List<AutocompleteDto> equallyWeightedVolumes) {
        List<AutocompleteDto> lessPopular = collectLessPopular(equallyWeightedVolumes);

        if (lessPopular.isEmpty()) {
            return Optional.empty();
        }

        String firstKeyword = lessPopular.get(0).value;
        return wrap(firstKeyword, lessPopular.size());
    }

    private List<AutocompleteDto> collectLessPopular(
            List<AutocompleteDto> equallyWeightedVolumes) {
        return equallyWeightedVolumes.stream()
                // usually self or typo correction eg. "pihone" to "phone"
                .skip(1)
                // skip self to avoid loops
                .filter(volume -> !volume.isIdentical)
                // aliases are related to marketing structure,
                // not to momentum popularity
                .filter(volume -> !volume.isAlias)
                .collect(toList());
    }

    private Optional<KeywordWeightDto> wrap(String keyword, int weight) {
        int tunedWeight = tuneWeight(weight);
        return keyword == null ?
                Optional.empty() :
                Optional.of(new KeywordWeightDto(keyword, tunedWeight));
    }

    // should be more sophisticated approach here, eg trained tensorflow
    private int tuneWeight(int weight) {
        return weight >= globalConfig.searchLimit / 2 ?
                // adjust results to searchLimit because there were
                // "Alias" and "Self" in the result
                BASE - 1 : BASE - 2;
    }
}
