package fr.insee.innovation.marathontokubernetes.api.controller;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ApiConverterControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldConvertDrawIO() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .post("/api/convert")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(IOUtils.toString(getClass().getResourceAsStream("/drawio.json"))))
                .andExpect(status().isOk());
    }
}
