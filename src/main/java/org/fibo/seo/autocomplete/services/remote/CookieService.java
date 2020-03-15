package org.fibo.seo.autocomplete.services.remote;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

import java.io.IOException;
import java.util.Map;

/**
 * Scrap cookies from the website
 */
@Service
public class CookieService {

    private static final String SCRAPPED_URL = "https://amazon.com";

    private final UserAgentService userAgentService;

    @Inject
    public CookieService(UserAgentService userAgentService) {
        this.userAgentService = userAgentService;
    }

    public Map<String, String> retrieve() throws IOException {
        return Jsoup.connect(SCRAPPED_URL)
                .timeout(0)
                .userAgent(userAgentService.next())
                .followRedirects(true)
                .method(Connection.Method.GET)
                .execute()
                .cookies();
    }

}
