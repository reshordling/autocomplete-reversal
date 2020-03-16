package org.fibo.seo.autocomplete.mappers;

import com.fasterxml.jackson.databind.JsonNode;

import org.fibo.seo.autocomplete.dto.AutocompleteDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Transform JSON into format for analysis
 */
public class DtoMapper {

    public static List<AutocompleteDto> map(JsonNode node) {
        String prefix = node.at("/prefix").asText();
        List<AutocompleteDto> suggestions = new ArrayList<>();
        node.at("/suggestions")
                .elements()
                .forEachRemaining(suggestion ->
                        suggestions.add(to(suggestion, prefix)));
        return suggestions;
    }

    private static AutocompleteDto to(JsonNode suggestion, String prefix) {
        AutocompleteDto dto = new AutocompleteDto();
        dto.value = suggestion.at("/value").asText("");
        dto.isIdentical = dto.value.equals(prefix);
        suggestion.at("/scopes")
                .elements()
                .forEachRemaining(feature -> dto.isAlias |= "ALIAS".equals(feature.at("/type").asText()));
        return dto;
    }
}
