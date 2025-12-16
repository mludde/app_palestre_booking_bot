package com.apppalestrebookingbot.model.timeline;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TimelineDay {
  @JsonProperty("orari_giorno")
  private ArrayList<TimelineDetails> giornoDetails;
}
