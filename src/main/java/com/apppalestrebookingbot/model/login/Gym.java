package com.apppalestrebookingbot.model.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Gym {
  private String nome;
  private String id_sede;
}
