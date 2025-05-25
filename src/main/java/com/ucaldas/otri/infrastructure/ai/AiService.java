package com.ucaldas.otri.infrastructure.ai;

import com.ucaldas.otri.application.shared.services.IAiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService implements IAiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder){
        chatClient = builder.build();
    }

}
