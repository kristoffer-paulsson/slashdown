package org.slashdown;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*public class SlashdownTokenizer {
    // Regex for tags: \ followed by lowercase alphanumeric or underscore
    private static final Pattern TAG_PATTERN = Pattern.compile("\\\\([a-z0-9_]+)");
    // Regex for words: alphanumeric or underscore
    private static final Pattern WORD_PATTERN = Pattern.compile("[a-zA-Z0-9_]+");
    // Regex for punctuation or similar: single non-alphanumeric, non-tag, non-whitespace characters
    private static final Pattern PUNCTUATION_PATTERN = Pattern.compile("[^a-zA-Z0-9_\\s\\\\]");
    // Regex for escaped backslashes
    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\{2,4}");

    // Token types
    public enum TokenType {
        TAG, WORD, PUNCTUATION, DOUBLE_NEWLINE, ESCAPED_BACKSLASH
    }

    // Token class
    public static class Token {
        TokenType type;
        String value;
        int lineNumber;

        Token(TokenType type, String value, int lineNumber) {
            this.type = type;
            this.value = value;
            this.lineNumber = lineNumber;
        }

        @Override
        public String toString() {
            return "Token(type=" + type + ", value='" + value + "', line=" + lineNumber + ")";
        }
    }

    private int lineNumber;

    public List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        lineNumber = 0;
        String[] lines = input.split("\n");
        int i = 0;

        while (i < lines.length) {
            lineNumber++;
            String line = lines[i].trim();
            System.out.println("Line " + lineNumber + ": Processing line: '" + line + "'");

            // Handle empty lines (check for double newline)
            if (line.isEmpty()) {
                System.out.println("Line " + lineNumber + ": Empty line, checking for double newline");
                int emptyCount = 1;
                i++;
                while (i < lines.length && lines[i].trim().isEmpty()) {
                    emptyCount++;
                    lineNumber++;
                    i++;
                    System.out.println("Line " + lineNumber + ": Found empty line, count: " + emptyCount);
                }
                if (emptyCount >= 2) {
                    System.out.println("Line " + (lineNumber - 1) + ": Found " + emptyCount +
                            " empty lines, adding DOUBLE_NEWLINE token");
                    tokens.add(new Token(TokenType.DOUBLE_NEWLINE, "", lineNumber - 1));
                } else {
                    System.out.println("Line " + (lineNumber - 1) + ": Found " + emptyCount +
                            " empty line(s), not enough for DOUBLE_NEWLINE");
                }
                continue;
            }

            // Tokenize the trimmed line
            tokenizeLine(line, tokens);
            i++;
        }

        System.out.println("Tokenization complete. Total tokens: " + tokens.size());
        for (int j = 0; j < tokens.size(); j++) {
            System.out.println("Token " + (j + 1) + ": " + tokens.get(j));
        }
        return tokens;
    }

    private void tokenizeLine(String line, List<Token> tokens) {
        System.out.println("Line " + lineNumber + ": Tokenizing line: '" + line + "'");
        int pos = 0;

        while (pos < line.length()) {
            // Check for escaped backslashes first
            Matcher escapeMatcher = ESCAPE_PATTERN.matcher(line);
            if (pos < line.length() && escapeMatcher.find(pos) && escapeMatcher.start() == pos) {
                String match = escapeMatcher.group();
                String value = match.equals("\\\\") ? "\\" : "\\\\";
                System.out.println("Line " + lineNumber + ": Found escaped backslash '" + match +
                        "' at position " + pos + ", adding ESCAPED_BACKSLASH token: '" + value + "'");
                tokens.add(new Token(TokenType.ESCAPED_BACKSLASH, value, lineNumber));
                pos = escapeMatcher.end();
                continue;
            }

            // Check for tags
            Matcher tagMatcher = TAG_PATTERN.matcher(line);
            if (pos < line.length() && tagMatcher.find(pos) && tagMatcher.start() == pos) {
                String tag = tagMatcher.group(1);
                System.out.println("Line " + lineNumber + ": Found tag '\\" + tag +
                        "' at position " + pos + ", adding TAG token");
                tokens.add(new Token(TokenType.TAG, tag, lineNumber));
                pos = tagMatcher.end();
                continue;
            }

            // Check for punctuation
            Matcher punctMatcher = PUNCTUATION_PATTERN.matcher(line);
            if (pos < line.length() && punctMatcher.find(pos) && punctMatcher.start() == pos) {
                String punct = punctMatcher.group();
                System.out.println("Line " + lineNumber + ": Found punctuation '" + punct +
                        "' at position " + pos + ", adding PUNCTUATION token");
                tokens.add(new Token(TokenType.PUNCTUATION, punct, lineNumber));
                pos = punctMatcher.end();
                continue;
            }

            // Check for words
            Matcher wordMatcher = WORD_PATTERN.matcher(line);
            if (pos < line.length() && wordMatcher.find(pos) && wordMatcher.start() == pos) {
                String word = wordMatcher.group();
                System.out.println("Line " + lineNumber + ": Found word '" + word +
                        "' at position " + pos + ", adding WORD token");
                tokens.add(new Token(TokenType.WORD, word, lineNumber));
                pos = wordMatcher.end();
                continue;
            }

            // Check for whitespace
            if (Character.isWhitespace(line.charAt(pos))) {
                System.out.println("Line " + lineNumber + ": Found whitespace at position " + pos + ", skipping");
                pos++;
                continue;
            }

            // If no match, invalid character
            System.out.println("Line " + lineNumber + ": Error - Invalid character '" +
                    line.charAt(pos) + "' at position " + pos);
            throw new IllegalArgumentException("Line " + lineNumber + ": Invalid character '" +
                    line.charAt(pos) + "' at position " + pos);
        }
    }
}*/
