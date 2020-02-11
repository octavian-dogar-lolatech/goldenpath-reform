//package com.lola.goldenpath;
//
//import com.fasterxml.jackson.core.JsonParseException;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonMappingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Before;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.io.IOException;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = GoldenPathApplication.class)
//@WebAppConfiguration
//public abstract class AbstractTest {
//
//    protected MockMvc mvc;
//    @Autowired
//    WebApplicationContext webApplicationContext;
//
//    @Before
//    public void setUp() {
//        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }
//
//    protected String mapToJson(final Object obj) throws JsonProcessingException {
//        final ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.writeValueAsString(obj);
//    }
//
//    protected <T> T mapFromJson(final String json, final Class<T> clazz)
//            throws JsonParseException, JsonMappingException, IOException {
//
//        final ObjectMapper objectMapper = new ObjectMapper();
//        return objectMapper.readValue(json, clazz);
//    }
//
//}
