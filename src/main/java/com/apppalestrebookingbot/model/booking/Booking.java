package com.apppalestrebookingbot.model.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Booking {
  @JsonProperty("prenotazioni")
  private BookingDetails prenotazioni;
}
