package org.slashdown;

import java.io.IOException;
import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Tokenizer
        String exampleInput = "Hello, World!\nThis \\b\\i is a test.";
        InputStream inputStream = new java.io.ByteArrayInputStream(exampleInput.getBytes());
        Tokenizer tokenizer = new Tokenizer(inputStream);
        try {
            tokenizer.tokenize();
            // Print tokens for demonstration
            for (Token token : tokenizer.getTokens()) {
                System.out.println(token);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}