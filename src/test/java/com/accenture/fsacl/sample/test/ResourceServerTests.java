package com.accenture.fsacl.sample.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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

}
