package com.example.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demo.service.ClientService;
import com.example.demo.web.ClientController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ClientService clientService;

  @Test
  public void shouldReturnOk() throws Exception {
    this.mockMvc.perform(get("/api/client"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void shouldReturnNotFound() throws Exception {
    this.mockMvc.perform(get("/api/client/test/123"))
        .andDo(print())
        .andExpect(status().isNotFound());
  }

  @Test
  public void shouldCreateObjectFromJson() throws Exception {
    String json = "{\"riskProfile\":\"HIGH\"}";
    this.mockMvc.perform(post("/api/client")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content(json))
        .andDo(print())
        .andExpect(status().isCreated());
  }

  @Test
  public void shouldUpdateObject() throws Exception {
    String json = "{\"riskProfile\":\"HIGH\"}";
    this.mockMvc.perform(patch("/api/client/1")
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .accept(MediaType.APPLICATION_JSON)
        .content(json))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void shouldDeleteObject() throws Exception {
    this.mockMvc.perform(delete("/api/client/1"))
        .andDo(print())
        .andExpect(status().isNoContent());
  }
}
