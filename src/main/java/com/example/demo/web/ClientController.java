package com.example.demo.web;

import com.example.demo.model.Client;
import com.example.demo.service.ClientService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/api/client")
public class ClientController {

  @Autowired
  private ClientService clientService;

  /**
   * GET all clients
   */
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Client> getClients() {
    return clientService.getAll();
  }

  /**
   * GET client by id
   */
  @GetMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Client getClient(@PathVariable Long id) {
    return clientService.getById(id);
  }

  /**
   * CREATE new client
   */
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Client createClient(@RequestBody @Valid Client client) {
    return clientService.create(client);
  }

  /**
   * PATCH client (PUT implement don't reasonable for current model)
   */
  @PatchMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Client updateClient(@PathVariable Long id, @RequestBody @Valid Client client) {
    return clientService.update(id, client);
  }

  /**
   * DELETE client
   */
  @DeleteMapping(path = "/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteClient(@PathVariable Long id) {
    clientService.deleteById(id);
  }

  /**
   * Merge clients into one with highest risk
   */
  @PostMapping(path = "/merge")
  @ResponseStatus(HttpStatus.OK)
  public Client merge(@RequestBody @Valid List<Client> list) {
    return clientService.mergeClients(list);
  }
}
