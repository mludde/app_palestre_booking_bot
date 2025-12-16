package com.apppalestrebookingbot.model.timeline;

import com.apppalestrebookingbot.model.booking.Booking;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TimelineDetails {
  @JsonProperty("id_orario_palinsesto")
  private String idOrarioPalinsesto;
  @JsonProperty("orario_inizio")
  private String orarioInizio;
  @JsonProperty("nome_corso")
  private String nomeCorso;
  @JsonProperty("prenotazioni")
  private Booking prenotazioni;
}
