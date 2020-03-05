package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.demo.model.Client;
import com.example.demo.model.RiskLevel;
import com.example.demo.service.ClientRepository;
import com.example.demo.service.ClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ClientServiceTest {

  @Autowired
  private ClientService clientService;
  @MockBean
  private ClientRepository clientRepository;

  @Autowired
  private ObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    Client c1 = new Client();
    c1.setId(1l);
    c1.setRiskProfile(RiskLevel.LOW.getName());

    Client c2 = new Client();
    c2.setId(2l);
    c2.setRiskProfile(RiskLevel.NORMAL.getName());

    when(clientRepository.findAll()).thenReturn(List.of(c1, c2));
    when(clientRepository.findById(anyLong())).thenReturn(Optional.of(c1));
    when(clientRepository.save(any(Client.class))).thenReturn(c2);
    doAnswer(invocation -> HttpStatus.NO_CONTENT).when(clientRepository).deleteById(anyLong());

  }

  @Test
  public void test_getAllClients() {

    List<Client> all = clientService.getAll();
    assertEquals(2, all.size());
    assertEquals(RiskLevel.LOW.getName(), all.get(0).getRiskProfile());
  }

  @Test
  public void test_getOneClient() {

    Client byId = clientService.getById(1L);
    assertEquals(RiskLevel.LOW.getName(), byId.getRiskProfile());
  }

  @Test
  public void test_deleteClient() {

    clientService.deleteById(2L);
    verify(clientRepository, times(1)).deleteById(anyLong());
  }

  @Test
  public void test_findMaxRiskLevel() {
    Client client1 = new Client();
    client1.setId(1L);
    client1.setRiskProfile(RiskLevel.LOW.getName());
    Client client2 = new Client();
    client2.setId(2L);
    client2.setRiskProfile(RiskLevel.NORMAL.getName());
    Client client3 = new Client();
    client3.setId(3L);
    client3.setRiskProfile(RiskLevel.LOW.getName());
    List<Client> clients = List.of(client1, client2, client3);

    RiskLevel maxRisk = clientService.findMaxRisk(clients);
    assertEquals(RiskLevel.NORMAL, maxRisk);

    client1.setRiskProfile(RiskLevel.HIGH.getName());

    RiskLevel newMaxRisk = clientService.findMaxRisk(clients);
    assertEquals(RiskLevel.HIGH, newMaxRisk);
  }

  @Test
  public void test_MergeClients() {
    Client client1 = new Client();
    client1.setId(1L);
    client1.setRiskProfile(RiskLevel.HIGH.getName());
    Client client2 = new Client();
    client2.setId(2L);
    client2.setRiskProfile(RiskLevel.NORMAL.getName());
    Client client3 = new Client();
    client3.setId(3L);
    client3.setRiskProfile(RiskLevel.LOW.getName());
    List<Client> clients = List.of(client1, client2, client3);

    when(clientRepository.save(any(Client.class))).thenReturn(client1);

    Client client = clientService.mergeClients(clients);

    assertEquals(RiskLevel.HIGH.getName(), client.getRiskProfile());
    verify(clientRepository, times(1)).deleteAll(any());
    verify(clientRepository, times(1)).save(any(Client.class));
  }

  @Test
  public void testDeserialization() throws JsonProcessingException {

    String json = "{\"id\":1,\"riskProfile\":\"LOW\"}";

    Client cli = new Client(RiskLevel.HIGH.getName());
    cli.setId(3L);

    Client fromJson = mapper.readerFor(Client.class).readValue(json);
    assertEquals(RiskLevel.LOW.getName(), fromJson.getRiskProfile());
  }

  @Test
  public void test_UpdateClient() {

    Client newCli = new Client(RiskLevel.HIGH.getName());
    Client oldCli = new Client(RiskLevel.LOW.getName());
    newCli.setId(3L);
    when(clientRepository.findById(anyLong())).thenReturn(Optional.of(oldCli));
    Client updated = clientService.update(newCli.getId(), newCli);
    assertEquals(RiskLevel.HIGH.getName(), updated.getRiskProfile());
  }
}
