package org.fibo.seo.autocomplete.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class JsonResource {

    public static final String RESPONSE_FILE = "response.json";
    public static final String EMPTY_RESPONSE_FILE = "empty.json";

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode loadJson(String resourceName) throws URISyntaxException, IOException {
        URL resourceUri = JsonResource.class
                .getClassLoader()
                .getResource(resourceName)
                .toURI().toURL();
        return mapper.readTree(resourceUri);
    }
}
