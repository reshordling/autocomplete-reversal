package org.fibo.seo.autocomplete.mappers;

import static org.fibo.seo.autocomplete.json.JsonResource.RESPONSE_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.JsonNode;

import org.fibo.seo.autocomplete.json.JsonResource;
import org.fibo.seo.autocomplete.config.GlobalConfig;
import org.fibo.seo.autocomplete.dto.AutocompleteDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { GlobalConfig.class })
public class DtoMapperTest {

    @Autowired
    private GlobalConfig config;

    @Test
    public void jsonMapped() throws Exception {
        int aliasPosition = 1;

        JsonNode jsonNode = JsonResource.loadJson(RESPONSE_FILE);
        List<AutocompleteDto> results = DtoMapper.map(jsonNode);
        assertNotNull(results);
        assertEquals(config.searchLimit, results.size());
        assertTrue(results.get(aliasPosition).isAlias);
        assertTrue(results.get(aliasPosition).isIdentical);
        assertEquals("iphone charger", results.get(aliasPosition).value);
    }
}
