package com.viraj.springaicode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.web.bind.annotation.GetMapping;
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
    @GetMapping("image/{Query}")
public  String genImage(@PathVariable String Query){
    ImagePrompt imagePrompt=new ImagePrompt(Query);
    ImageResponse imageResponse=openAiImageMode.call(imagePrompt);
    return imageResponse.getResult().getOutput().getUrl();

}

}
