package org.fibo.seo.autocomplete.services.remote;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.fibo.seo.autocomplete.config.GlobalConfig;
import org.fibo.seo.autocomplete.exceptions.RemoteException;
import org.jsoup.Jsoup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Get volumes payload as Json for the given keyword as prefix
 */
@Service
public class AutocompleteService {

    // Lombok is not friendly enough for Intellij
    private final Logger log = LoggerFactory.getLogger(AutocompleteService.class);

    // Can it be location-specific? Amazon UI does not come to
    // https://completion.amazon.com/search/complete
    private final String AUTOCOMPLETE_URL =
            "https://completion.amazon.com/api/2017/suggestions";
    private final String PREFIX = "prefix";
    private final String LIMIT = "limit";

    private final UserAgentService userAgentService;
    private final CookieService cookieService;
    private final GlobalConfig globalConfig;

    private final AtomicReference<Map<String, String>> cookiesRef =
            new AtomicReference<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    public AutocompleteService(UserAgentService userAgentService,
                               CookieService cookieService,
                               GlobalConfig globalConfig) {
        this.userAgentService = userAgentService;
        this.cookieService = cookieService;
        this.globalConfig = globalConfig;
    }

    // Search autocomplete results, change cookies on failure
    public JsonNode tryGettingTwice(String keywordPrefix) throws RemoteException {
        try {
            return get0(keywordPrefix);
        } catch (IOException eFirst) {
            log.debug("First request failed", eFirst);
            try {
                refreshCookies();
                return get0(keywordPrefix);
            } catch (IOException eSecond) {
                log.warn("Second request failed", eSecond);
                throw new RemoteException("Unable to get volumes");
            }
        }
    }

    private JsonNode get0(String keyword) throws IOException {
        Map<String, String> cookies = getCookies();
        if (cookies == null) {
            throw new RemoteException("Unable to get cookies");
        }
        String json = getJson(keyword, cookies);
        return objectMapper.readTree(json);
    }

    // Getting JSON with fake user agent and cookies
    private String getJson(String keyword, Map<String, String> cookies)
            throws IOException {
        String userAgent = userAgentService.next();
        return Jsoup.connect(AUTOCOMPLETE_URL)
                .data(PREFIX, keyword)
                .followRedirects(true)
                .data(cookies)
                .header("Accept", "application/json, text/javascript, */*; q=0.01")
                .header("Accept-Language", "en-US,en;q=0.5")
                .header("Origin", "https://www.amazon.com")
                .header("DNT", "1")
                .referrer("https://www.amazon.com/s?k=kphone+11&language=en_GB&crid=14B36PRPVBHMI&sprefix=kphone%2Caps%2C367&ref=nb_sb_ss_i_2_6")
                .data(LIMIT, globalConfig.searchLimit.toString())
                // market id, can be scrapped from the code
                .data("mid", "ATVPDKIKX0DER")
                // good place for random params
                // it would be also nice to send events simulating keypress
                // at some point in the future or replace by a commercial
                // scrapping lib
                .data("customer-id", "")
                .data("page-type", "Gateway")
                .data("lop", "en_US")
                .data("site-variant", "desktop")
                .data("client-info", "amazon-search-ui")
                .data("alias", "aps")
                .data("b2b", "0")
                .data("fresh", "0")
                .data("event", "onKeyPress")
                .data("suggestion-type", "KEYWORD")
                .data("suggestion-type", "WIDGET")
                .userAgent(userAgent)
                .timeout(0)
                .ignoreContentType(true)
                .get()
                .body()
                .text();
    }

    private Map<String, String> getCookies() throws IOException {
        cookiesRef.compareAndSet(null, cookieService.retrieve());
        return cookiesRef.get();
    }

    private void refreshCookies() throws IOException {
        cookiesRef.set(cookieService.retrieve());
    }
}
