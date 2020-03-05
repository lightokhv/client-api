package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import com.example.demo.model.Client;
import com.example.demo.model.RiskLevel;
import com.example.demo.service.ClientRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
class ClientRepositoryTest {

  @Autowired
  private TestEntityManager entityManager;
  @Autowired
  private ClientRepository repository;

  @DisplayName("repo test")
  @Test
  void shouldReturnAllClients() {
    entityManager.persist(new Client(RiskLevel.LOW.getName()));
    List<Client> all = repository.findAll();
    assertEquals(7, all.size());
    assertThat(all).extracting(Client::getRiskProfile).contains("LOW");

    Client client = repository.findById(3L).get();
    assertEquals(RiskLevel.HIGH.getName(), client.getRiskProfile());

    repository.deleteById(3L);

    List<Client> allAfter = repository.findAll();
    assertEquals(6, allAfter.size());
    assertThat(allAfter).extracting(Client::getRiskProfile).isNotSameAs(all);
  }
}
