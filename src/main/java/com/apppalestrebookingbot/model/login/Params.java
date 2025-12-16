package com.apppalestrebookingbot.model.login;

import com.apppalestrebookingbot.model.timeline.Timeline;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Params {
  private Session sessione;
  private ArrayList<Gym> sedi_collegate;
  @JsonProperty("lista_risultati")
  private ArrayList<Timeline> lista_risultati;
}
