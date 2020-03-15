package org.fibo.seo.autocomplete.services.remote;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class UserAgentService {
    // Top user agents from https://developers.whatismybrowser.com/useragents/explore/software_type_specific/web-browser/
    private static final Iterator<String> FAKE_USER_AGENTS =
            new ConcurrentLinkedQueue<>(Arrays.asList(
                    "Mozilla/5.0 CK={} (Windows NT 6.1; WOW64; Trident/7.0; rv:11.0) like Gecko",
                    "Mozilla/5.0 (iPhone; CPU iPhone OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36",
                    "Mozilla/5.0 (iPad; CPU OS 12_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 1.1.4322)",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)"))
                    .iterator();

    public String next() {
        return FAKE_USER_AGENTS.next();
    }
}
