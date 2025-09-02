package org.example.exceptions;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

//import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ShopMicroserviceWebApplicationException extends WebApplicationException {

  @NotNull private final List<WebApplicationError> errors = new ArrayList<>();

  public ShopMicroserviceWebApplicationException(
      final Response.Status status, final WebApplicationError error) {
    super(status);
    initializeErrors(error);
  }

  private void initializeErrors(final WebApplicationError error) {
    initializeErrors(List.of(error));
  }

  private void initializeErrors(final List<WebApplicationError> errors) {
    this.errors.addAll(errors);
  }
}
