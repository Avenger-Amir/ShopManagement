package org.example.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

// Todo(mushtqu): Once we are hitting the entity relationship service endpoint using kafka, move
// this file to com.schrodinger.compositerowmicroservice.services.models.errors
@JsonRootName(value = "error")
@Getter
@Setter
@ToString
public class WebApplicationError {

  @JsonProperty("error_code")
  private String errorCode;

  @JsonProperty("message")
  private String message;

  public WebApplicationError() {
    this("", "");
  }

  public WebApplicationError(final String message) {
    this("", message);
  }

  public WebApplicationError(final String errorCode, final String message) {
    this.errorCode = errorCode;
    this.message = message;
  }
}
