package org.fibo.seo.autocomplete.services.remote;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.stream.IntStream;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { UserAgentService.class })
public class UserAgentServiceTest {

    @Autowired
    private UserAgentService userAgentService;

    @Test
    public void circularTraverseAgents() {
        String value = userAgentService.next();
        long foundRepetitions = IntStream.range(1, 100)
                .filter(index -> userAgentService.next().equals(value))
                .count();
        assertTrue(foundRepetitions > 0);
    }
}
