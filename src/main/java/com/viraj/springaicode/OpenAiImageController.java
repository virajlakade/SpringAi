package com.viraj.springaicode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.*;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    ImagePrompt imagePrompt=new ImagePrompt(Query, OpenAiImageOptions.builder()
            .quality("hd")
            .height(1024)
            .width(1024)
            .N(1)
            .style("natural")

            .build());
    ImageResponse imageResponse=openAiImageMode.call(imagePrompt);
    return imageResponse.getResult().getOutput().getUrl();

}
public String describe(@RequestParam String query, @RequestParam MultipartFile file){
    chatClient.prompt()
            .user(us ->us.text(query)
                    .media(MimeTypeUtils.IMAGE_JPEG,file.getResource()))
            .call()
            .content();
    return "";

}}
