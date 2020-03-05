package com.example.demo.model;

import com.example.demo.service.exception.InvalidDataException;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RiskLevel {
  LOW(1, "LOW"),
  NORMAL(3, "NORMAL"),
  HIGH(5, "HIGH");

  private static final Map<String, RiskLevel> NAMES = new HashMap<>();

  static {
    for (RiskLevel value : values()) {
      NAMES.put(value.name, value);
    }
  }

  @Getter
  private final Integer level;
  @JsonValue
  @Getter
  private final String name;

  public static RiskLevel fromName(final String name) {
    if (name != null && !NAMES.containsKey(name)) {
      throw new InvalidDataException("Invalid risk profile name " + name);
    }
    return NAMES.get(name);
  }
}
