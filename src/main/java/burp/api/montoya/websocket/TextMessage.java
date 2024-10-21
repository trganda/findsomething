/*
 * Copyright (c) 2022-2023. PortSwigger Ltd. All rights reserved.
 *
 * This code may be used to extend the functionality of Burp Suite Community Edition
 * and Burp Suite Professional, provided that this usage does not violate the
 * license terms for those products.
 */

package burp.api.montoya.websocket;

public interface TextMessage {
    /**
     * @return Text based WebSocket payload.
     */
    String payload();

    /**
     * @return The direction of the message.
     */
    Direction direction();
}
