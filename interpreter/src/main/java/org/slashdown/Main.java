package org.slashdown;

import org.slashdown.token.TokenIterator;
import org.slashdown.token.Tokenizer;

import java.io.InputStream;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Tokenizer
        // open resource file as InputStream
        /*InputStream resourceStream = Main.class.getResourceAsStream("/corpus.txt");

        TokenIterator tokenIterator = new TokenIterator(new Tokenizer(resourceStream));
        Interpreter interpreter = new Interpreter(tokenIterator);

        interpreter.interpret();*/

        new Context("corpus.txt").interpret();
    }
}