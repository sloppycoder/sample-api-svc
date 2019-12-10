package net.vino9.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MessageResource {
    private MessageRepository repo;

    @Autowired
    public MessageResource(MessageRepository repo) {
        this.repo = repo;
    }

    @Cacheable("default")
    public String getMessageById(String id) {
        // let's pretend it's a slow DB call..
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            //shouldn't happen
        }

        if (id != null && !id.isEmpty()) {
            Optional<Message> message = repo.findById(id);
            if (message.isPresent()) {
                return message.get().getContent();
            }
        }
        return null;
    }
}
