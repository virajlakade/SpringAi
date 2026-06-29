package com.viraj.springaicode;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    private final VectorStore vectorStore;

    public DataInitializer(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void initData() {

        try {

            TextReader textReader =
                    new TextReader(new ClassPathResource("product_details.txt"));

            List<Document> documents = textReader.get();

            vectorStore.add(documents);

            System.out.println("Documents loaded successfully into PGVector!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}