package com.example.demo;

import static com.example.demo.service.validator.ClientValidator.validate;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.demo.model.Client;
import com.example.demo.service.exception.InvalidDataException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClientValidatorTest {

  @Autowired
  private ObjectMapper om;

  @Test
  public void test_validateFailed() throws JsonProcessingException {
    String json = "{\"id\":1,\"riskProfile\":\"ALOW\"}";
    Client cli = om.readerFor(Client.class).readValue(json);
    InvalidDataException exception = assertThrows(InvalidDataException.class,
        () -> validate(cli));
    assertTrue(exception.getMessage().contains("Invalid risk profile name ALOW"));
  }

  @Test
  public void test_validateSuccess() throws JsonProcessingException {
    String json = "{\"id\":1,\"riskProfile\":\"LOW\"}";
    Client cli = om.readerFor(Client.class).readValue(json);
    validate(cli);
  }
}
