package net.vino9.sample;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MessageRepository {
    private Map<String, String> messages = Map.of(
            "10001", "Hello",
            "10002", "how are you",
            "10003", "good bye"
    );

    @Cacheable("default")
    public String getMessageById(String id) {
        // let's pretend it's a slow DB call..
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            //shouldn't happen
        }

        if (id != null && !id.isEmpty() && messages.containsKey(id)) {
            return messages.get(id);
        }
        return null;
    }
}
