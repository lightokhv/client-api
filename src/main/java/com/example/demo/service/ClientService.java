package com.example.demo.service;

import static com.example.demo.service.validator.ClientValidator.validate;

import com.example.demo.model.Client;
import com.example.demo.model.RiskLevel;
import com.example.demo.model.UpdatableJsonView;
import com.example.demo.service.exception.BusinessException;
import com.example.demo.service.exception.InvalidDataException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {

  @Autowired
  private ClientRepository repository;

  @Autowired
  private ObjectMapper mapper;

  /**
   * get all clients
   */
  @Transactional(readOnly = true)
  public List<Client> getAll() {
    return repository.findAll();
  }

  /**
   * get client by id or throw exception
   */
  @Transactional(readOnly = true)
  public Client getById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.getReasonPhrase()));
  }

  /**
   * create new client
   */
  @Transactional
  public Client create(Client client) {
    validate(client);
    return repository.save(client);
  }

  /**
   * update only one field or throw exception
   */
  @Transactional
  public Client update(Long id, Client client) {
    validate(client);
    Client cli = repository.findById(id)
        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.getReasonPhrase()));
    try {
      String json = mapper.writeValueAsString(client);
      mapper.readerForUpdating(cli)
          .withView(UpdatableJsonView.class)
          .readValue(json);
    } catch (JsonProcessingException e) {
      throw new InvalidDataException(e);
    }
    return cli;
  }

  /**
   * delete client by id or throw exception
   */
  @Transactional
  public void deleteById(Long id) {
    repository.findById(id)
        .orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND.getReasonPhrase()));
    repository.deleteById(id);
  }

  /**
   * get max risk from list of clients
   */
  public RiskLevel findMaxRisk(List<Client> list) {
    return list.stream()
        .map(it -> RiskLevel.fromName(it.getRiskProfile()))
        .max(Comparator.comparing(RiskLevel::getLevel))
        .orElse(RiskLevel.LOW);
  }

  /**
   * remove all list from repo and create new client with highest risk
   */
  @Transactional
  public Client mergeClients(List<Client> list) {
    RiskLevel maxRisk = findMaxRisk(list);
    repository.deleteAll(list);
    return repository.save(new Client(maxRisk.getName()));
  }
}
