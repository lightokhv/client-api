package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonView;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Client {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @JsonView(UpdatableJsonView.class)
  @Column(name = "risk_profile", nullable = false, length = 50)
  private String riskProfile;

  public Client() {
  }

  public Client(String riskProfile) {
    this.riskProfile = riskProfile;
  }
}
