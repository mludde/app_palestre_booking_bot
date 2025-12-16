package com.apppalestrebookingbot.model.login;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Dati {
  private String id;
  private String id_sede;
  private String tipo_cliente;
  private String nome;
  private String cognome;
  private String telefono;
  private String mail;
  private String login_trashed;
  private String last_update;
  private String token_fcm;
  private String app_device;
  private int status_user;
  private String token_orario;
  private String url_profile_image;
  private String url_profile_thumb;
  private String id_factotum;
  private String mia_preferenza_notif;
  private int canale_arrivo;
  private String preferito_app;

  // Getters e setters...
}
