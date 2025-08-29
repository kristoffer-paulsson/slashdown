package org.slashdown;

import org.slashdown.token.Token;
import org.slashdown.token.TokenIterator;
import org.slashdown.token.Tokenizer;

import java.io.InputStream;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Tokenizer
        String exampleInput = "Hello, World!\nThis \\b\\i is a test.";
        InputStream inputStream = new java.io.ByteArrayInputStream(exampleInput.getBytes());
        TokenIterator tokenIterator = new TokenIterator(new Tokenizer(inputStream));
        try {
            while (tokenIterator.hasNext()) {
                Token token = tokenIterator.next();
                System.out.println(token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}