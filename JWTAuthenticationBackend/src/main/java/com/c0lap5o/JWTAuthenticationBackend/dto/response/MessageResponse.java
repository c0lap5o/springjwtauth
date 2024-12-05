package com.c0lap5o.JWTAuthenticationBackend.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Response class for returning a simple message.
 * This class is used to provide a unified way of returning messages in response to various API requests.
 */
@Data
@Builder
public class MessageResponse {

  /**
   * The message to be returned in the response.
   */
  private String message;

  /**
   * Constructor to initialize the MessageResponse object with the given message.
   *
   * @param message The message to be included in the response.
   */
  public MessageResponse(String message) {
    this.message = message;
  }
}