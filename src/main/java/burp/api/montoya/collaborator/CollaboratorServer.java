/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package burp.api.montoya.collaborator;

/**
 * Provides details of the Collaborator server associated with
 * this client.
 */
public interface CollaboratorServer {
  /**
   * Address of the Collaborator server.
   *
   * @return The hostname or IP address of the Collaborator server.
   */
  String address();

  /**
   * Indicates whether the server address is an IP address.
   *
   * @return {@code true} if the address is an IP address; {@code false}
   * otherwise.
   */
  boolean isLiteralAddress();
}
