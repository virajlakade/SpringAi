package com.viraj.springaicode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.openai.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.plaf.PanelUI;

@RestController
public class OpenAiImageController {
    private ChatClient chatClient;


    private OpenAiImageModel openAiImageMode;
    public OpenAiImageController(OpenAiImageModel openAiImageModel,ChatClient.Builder builder){
        this.openAiImageMode=openAiImageModel;
        chatClient=builder.build();
    }
public  String genImage(@PathVariable String Query){

}

}
