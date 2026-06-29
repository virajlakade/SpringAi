package com.viraj.springaicode;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.stringtemplate.v4.ST;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@RestController
public class OpenAIController {

    private final ChatClient chatClient;
    @Autowired
    private EmbeddingModel embeddingModel;
    @Autowired
    private VectorStore vectorStore;

    public OpenAIController(ChatModel chatModel) {
        this.chatClient = ChatClient.create(chatModel);
    }

    @GetMapping("/api/{message}")
    public String answer(@PathVariable String message) {
        ChatResponse chatResponse = chatClient
                .prompt(message)
                .call()
                .chatResponse();
        System.out.println(chatResponse.getMetadata().getModel());
        String response = chatResponse
                .getResult()
                .getOutput()
                .getText();

        return response;


    }

    @PostMapping("/api/recommend")
    public String recommend(@RequestParam String type, @RequestParam String year, @RequestParam String lang) {
        String tempt = """
                I want to watch a {type} movie tonight with good rating ,
                looking for mpvies around this year{year}
                the language i am lookin for{lang}
                suggest one specific movie tell me also the length and cast of movie
                """;
        PromptTemplate promptTemplate = new PromptTemplate(tempt);
        Prompt prompt = promptTemplate.create(Map.of("type", type, "year", year, "lang", lang));


        String response = chatClient
                .prompt(prompt)
                .call()
                .content();
        return response;
    }

    @PostMapping("/api/embedding")
    public float[] embedding(@RequestParam String text) {
        return embeddingModel.embed(text);


    }

    @PostMapping("/api/similarity")
    public double similarity(@RequestParam String text1, @RequestParam String text2
    ) {
        float[] embedding1 = embeddingModel.embed(text1);
        float[] embedding2 = embeddingModel.embed(text2);
        double dotProduct = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        for (int i = 0; i < embedding1.length; i++) {
            dotProduct += embedding1[i] * embedding2[i];
            norm1 += Math.pow(embedding1[i], 2);
            norm2 += Math.pow(embedding2[i], 2);
        }

        return dotProduct * 100 / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }

    @GetMapping("/api/product")
    public List<Document> getproducts(@RequestParam String text) {

        return vectorStore.similaritySearch(SearchRequest.builder().query(text).topK(2).build());

    }

    @PostMapping("/api/ask")
    public String answerrag(@RequestParam String query) {
        return chatClient
                .prompt(query)
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();

    }
}