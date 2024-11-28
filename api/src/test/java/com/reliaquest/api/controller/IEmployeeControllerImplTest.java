package com.reliaquest.api.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.After;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Functional/Integration are incomplete because of jetty dependency issue on my PC
 * Could not get time to fix it
 */
@SpringBootTest
public class IEmployeeControllerImplTest {

    private WireMockServer server;

    /**
     * Wiremock silently adds /__files to the end of this path
     */
    private static final String RESPONSE_STUB_ROOT_DIR = "src/test/resources/stubs/response";

    private final String CONTENT_TYPE = "Content-Type";
    private final String APPLICATION_JSON = MediaType.APPLICATION_JSON_VALUE;

    private MockMvc mockMvc;

    @Autowired
    IEmployeeController iEmployeeController;

    @BeforeEach
    public void setup() {
        server = new WireMockServer(WireMockConfiguration.wireMockConfig()
                .port(8112)
                .fileSource(new ClasspathFileSource(RESPONSE_STUB_ROOT_DIR)));
        mockMvc = MockMvcBuilders.standaloneSetup(iEmployeeController).build();
        server.start();
        WireMock.configureFor("localhost", server.port());
    }

    @After
    public void afterAll() {
        server.stop();
        server.shutdown();
    }

    //    @Test
    //    public void getAllEmployeeTest() throws Exception {
    //        server.stubFor(WireMock.get(WireMock.urlPathEqualTo("/api/v1/employee"))
    //                .willReturn(okWithJsonFile("/get-all-employees-200.json")));
    //
    //        var response = mockMvc.perform(MockMvcRequestBuilders.get("")).andReturn();
    //
    //        Assertions.assertEquals(200, response.getResponse().getStatus());
    //    }

    private ResponseDefinitionBuilder okWithJsonFile(String fileName) {
        return WireMock.ok().withBodyFile(fileName).withHeader(CONTENT_TYPE, APPLICATION_JSON);
    }
}
