package org.fibo.seo.autocomplete.services.remote;

import static org.fibo.seo.autocomplete.json.JsonResource.EMPTY_RESPONSE_FILE;
import static org.fibo.seo.autocomplete.json.JsonResource.RESPONSE_FILE;
import static org.fibo.seo.autocomplete.json.JsonResource.loadJson;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.fibo.seo.autocomplete.config.GlobalConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GlobalConfig.class, AnalyzerService.class })
public class AnalyzerServiceTest {

    @MockBean
    private AutocompleteService autocompleteService;

    @Autowired
    private AnalyzerService analyzerService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void bestEver() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(100, score);
    }

    @Test
    public void topCharts() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(33, score);
    }

    @Test
    public void veryPopular() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(11, score);
    }

    @Test
    public void manyOccurrences() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(4, score);
    }

    @Test
    public void someOccurrences() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(RESPONSE_FILE),
                        loadJson(RESPONSE_FILE),
                        loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(1, score);
    }

    @Test
    public void zero() throws Exception {
        when(autocompleteService.tryGettingTwice(anyString()))
                .thenReturn(loadJson(EMPTY_RESPONSE_FILE));

        long score = analyzerService.score("some keyword");
        assertEquals(0, score);
    }

    @Test
    public void checkPopularityMapping() {
        assertEquals(0, analyzerService.calculate100Score(0));
        assertEquals(1, analyzerService.calculate100Score(23));
        assertEquals(6, analyzerService.calculate100Score(123));
        assertEquals(11, analyzerService.calculate100Score(233));
        assertEquals(15, analyzerService.calculate100Score(333));
        assertEquals(27, analyzerService.calculate100Score(589));
        assertEquals(32, analyzerService.calculate100Score(699));
        assertEquals(33, analyzerService.calculate100Score(729));
        assertEquals(69, analyzerService.calculate100Score(1500));
        assertEquals(100, analyzerService.calculate100Score(2187));
    }
}
