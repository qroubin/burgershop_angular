package com.burgers.burgers.order;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/updateOrderWs")
    @SendTo("/orderWs/value")
    public String updateOrderWs(String newValue) {
        return newValue;
    }
}

