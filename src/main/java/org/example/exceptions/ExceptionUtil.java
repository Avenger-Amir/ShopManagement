package org.example.exceptions;


import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
public class ExceptionUtil {

  /**
   * Helper method to return the Web Application Exception
   *
   * @param errorMsg the error message
   * @param status enumerated status value
   * @return ShopMicroserviceWebApplicationException
   */
  public static ShopMicroserviceWebApplicationException error(
      final String errorMsg, final Response.Status status) {
    return new ShopMicroserviceWebApplicationException(
        status, new WebApplicationError(String.valueOf(status.getStatusCode()), errorMsg));
  }

  /**
   * Returns CompositeRowWebApplicationException with an error message and error code.
   *
   * @param errorMsg the error message
   * @param httpResponseErrorCode an HttpResponseErrorCode for the exception
   * @return CompositeRowWebApplicationException
   */
  public static ShopMicroserviceWebApplicationException error(
      final String errorMsg, final String httpResponseErrorCode) {
    return new ShopMicroserviceWebApplicationException(
        Response.Status.BAD_REQUEST,
        new WebApplicationError(httpResponseErrorCode, errorMsg));
  }
}
