package net.vino9.sample.test;

import net.vino9.sample.MessageRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ResourceServerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageRepository repo;

    @Autowired
    private CacheManager cacheManager;

    @Test
    public void context_loads() {
    }

    @Test
    public void actuator_works() throws Exception {
        mockMvc.perform(get("/actuator/info")).andExpect(status().is(200));
    }

    @Test
    public void api_not_allowed() throws Exception {
        mockMvc.perform(get("/whoami")).andExpect(status().is(401));
    }

    @Test
    public void cache_populated_after_calling_repository() {
        String msg = repo.getMessageById("10001");
        Assert.assertEquals(msg, "Hello");

        Cache.ValueWrapper cacheValue = cacheManager.getCache("default").get("10001");
        Assert.assertNotNull(cacheValue.get().toString() );
        Assert.assertEquals("Hello", cacheValue.get().toString());
    }
}
