package com.apppalestrebookingbot.model.login;

import com.apppalestrebookingbot.model.timeline.Timeline;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class LoginResponse {
  private int status;
  private String messaggio;
  @JsonProperty("parametri")
  private Params parametri;
}
