package com.example.demo.service.validator;

import com.example.demo.model.Client;
import com.example.demo.model.RiskLevel;

public final class ClientValidator {

  static public void validate(Client client) {
    RiskLevel.fromName(client.getRiskProfile());
  }
}
