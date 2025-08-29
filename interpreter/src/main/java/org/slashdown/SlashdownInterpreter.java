package org.slashdown;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SlashdownInterpreter {
    // Define valid block and inline commands
    private static final String[] BLOCK_COMMANDS = {"h1", "h2", "h3", "h4", "h5", "h6"};
    private static final String[] INLINE_COMMANDS = {"b", "i", "u"};

    // Data structures to store parsed document
    private static class DocumentElement {
        String type; // "heading" or "paragraph"
        String command; // e.g., "h1" for heading, null for paragraph
        String content; // Reconstructed content with inline commands and escapes
        int level; // Heading level (1-6) or 0 for paragraph

        DocumentElement(String type, String command, String content, int level) {
            this.type = type;
            this.command = command;
            this.content = content;
            this.level = level;
        }
    }

    private List<DocumentElement> document = new ArrayList<>();
    private int currentLine = 0;

    public void interpret(String input) {
        // Tokenize input
        System.out.println("Starting tokenization of Slashdown input");
        SlashdownTokenizer tokenizer = new SlashdownTokenizer();
        List<SlashdownTokenizer.Token> tokens = tokenizer.tokenize(input);
        System.out.println("Starting interpretation of tokens");

        int i = 0;
        while (i < tokens.size()) {
            SlashdownTokenizer.Token token = tokens.get(i);
            currentLine = token.lineNumber;
            System.out.println("Processing token: " + token);

            // Handle double newline (paragraph separation)
            if (token.type == SlashdownTokenizer.TokenType.DOUBLE_NEWLINE) {
                System.out.println("Token at line " + currentLine + ": DOUBLE_NEWLINE, marking paragraph separation");
                i++;
                continue;
            }

            // Check if token starts a block command
            boolean isBlockCommand = false;
            if (token.type == SlashdownTokenizer.TokenType.TAG && Arrays.asList(BLOCK_COMMANDS).contains(token.value)) {
                String cmd = token.value;
                System.out.println("Token at line " + currentLine + ": Detected block command '" + cmd + "'");
                // Collect content tokens until end of line or another block command/double newline
                StringBuilder content = new StringBuilder();
                i++;
                boolean contentStarted = false;
                while (i < tokens.size() && tokens.get(i).lineNumber == currentLine &&
                        tokens.get(i).type != SlashdownTokenizer.TokenType.DOUBLE_NEWLINE &&
                        (tokens.get(i).type != SlashdownTokenizer.TokenType.TAG ||
                                !Arrays.asList(BLOCK_COMMANDS).contains(tokens.get(i).value))) {
                    SlashdownTokenizer.Token contentToken = tokens.get(i);
                    System.out.println("Token at line " + contentToken.lineNumber + ": Adding to '" + cmd +
                            "' content: " + contentToken);
                    if (!contentStarted && contentToken.type != SlashdownTokenizer.TokenType.WHITESPACE) {
                        contentStarted = true;
                    }
                    if (contentStarted) {
                        if (contentToken.type == SlashdownTokenizer.TokenType.TAG) {
                            content.append("\\").append(contentToken.value);
                        } else {
                            content.append(contentToken.value);
                        }
                    }
                    i++;
                }
                String contentStr = content.toString().trim();
                if (contentStr.isEmpty()) {
                    System.out.println("Token at line " + currentLine + ": Error - Empty content after '" + cmd + "'");
                    throw new IllegalArgumentException("Line " + currentLine + ": Heading '" + cmd + "' has no content");
                }
                // Validate no inline commands in heading content
                if (containsInlineCommands(contentStr)) {
                    System.out.println("Token at line " + currentLine +
                            ": Error - Inline commands not allowed in '" + cmd + "' content");
                    throw new IllegalArgumentException("Line " + currentLine +
                            ": Inline commands not allowed in '" + cmd + "' content");
                }
                System.out.println("Token at line " + currentLine + ": Parsed heading content: '" + contentStr + "'");
                int level = Integer.parseInt(cmd.substring(1));
                document.add(new DocumentElement("heading", cmd, contentStr, level));
                System.out.println("Token at line " + currentLine + ": Added heading '" + cmd +
                        "' with content: '" + contentStr + "'");
                continue;
            }

            // Treat as paragraph start
            System.out.println("Token at line " + currentLine + ": Not a block command, starting paragraph");
            StringBuilder paragraph = new StringBuilder();
            boolean paragraphEnded = false;
            while (i < tokens.size() && !paragraphEnded) {
                token = tokens.get(i);
                currentLine = token.lineNumber;
                System.out.println("Processing token for paragraph: " + token);
                if (token.type == SlashdownTokenizer.TokenType.DOUBLE_NEWLINE) {
                    System.out.println("Token at line " + currentLine + ": DOUBLE_NEWLINE, ending paragraph");
                    paragraphEnded = true;
                    i++;
                } else if (token.type == SlashdownTokenizer.TokenType.TAG &&
                        Arrays.asList(BLOCK_COMMANDS).contains(token.value)) {
                    System.out.println("Token at line " + currentLine +
                            ": Block command '" + token.value + "', ending paragraph");
                    paragraphEnded = true;
                } else {
                    // Add token to paragraph
                    if (token.type == SlashdownTokenizer.TokenType.TAG) {
                        paragraph.append("\\").append(token.value);
                    } else {
                        paragraph.append(token.value);
                    }
                    // Check if next token is on a new line
                    if (i + 1 < tokens.size() && tokens.get(i + 1).lineNumber > currentLine) {
                        paragraph.append("\n");
                        System.out.println("Token at line " + currentLine + ": New line detected, adding '\\n' to paragraph");
                    }
                    i++;
                }
            }
            String content = paragraph.toString().trim();
            if (content.isEmpty()) {
                System.out.println("Token at line " + currentLine + ": Error - Empty paragraph content");
                throw new IllegalArgumentException("Line " + currentLine + ": Paragraph has no content");
            }
            validateInlineCommands(content);
            System.out.println("Token at line " + currentLine + ": Parsed paragraph content: '" + content + "'");
            document.add(new DocumentElement("paragraph", null, content, 0));
            System.out.println("Token at line " + currentLine + ": Added paragraph with content: '" + content + "'");
        }

        // Final validation: Log document structure
        System.out.println("Final validation: Checking document structure");
        for (int k = 0; k < document.size(); k++) {
            DocumentElement elem = document.get(k);
            System.out.println("Element " + (k + 1) + ": Type=" + elem.type +
                    ", Command=" + (elem.command != null ? elem.command : "none") +
                    ", Content='" + elem.content + "', Level=" + elem.level);
        }
        System.out.println("Interpretation complete. Document contains " + document.size() + " elements.");
    }

    // Validate inline commands in paragraph content
    private void validateInlineCommands(String content) {
        System.out.println("Validating inline commands in content: '" + content + "'");
        Matcher matcher = Pattern.compile("\\\\([a-z0-9_]+)").matcher(content);
        int lastEnd = 0;
        while (matcher.find()) {
            String cmd = matcher.group(1);
            boolean valid = false;
            for (String inlineCmd : INLINE_COMMANDS) {
                if (cmd.equals(inlineCmd)) {
                    valid = true;
                    System.out.println("Found valid inline command: '\\" + cmd + "' at position " + matcher.start());
                    break;
                }
            }
            if (!valid) {
                System.out.println("Error: Invalid command '\\" + cmd + "' in paragraph content");
                throw new IllegalArgumentException("Line " + currentLine + ": Invalid command '\\" + cmd + "' in paragraph");
            }
            int endIndex = matcher.end();
            if (endIndex < content.length() && content.charAt(endIndex) != '\\' &&
                    !Character.isWhitespace(content.charAt(endIndex))) {
                System.out.println("Error: No space after command '\\" + cmd + "' at position " + matcher.start());
                throw new IllegalArgumentException("Line " + currentLine + ": No space after command '\\" + cmd + "' at position " + matcher.start());
            }
            System.out.println("Validated command '\\" + cmd + "' at position " + matcher.start());
            lastEnd = endIndex;
        }
        System.out.println("Inline command validation complete");
    }

    // Check if content contains inline commands
    private boolean containsInlineCommands(String content) {
        System.out.println("Checking for inline commands in: '" + content + "'");
        Matcher matcher = Pattern.compile("\\\\([a-z0-9_]+)").matcher(content);
        while (matcher.find()) {
            String cmd = matcher.group(1);
            for (String inlineCmd : INLINE_COMMANDS) {
                if (cmd.equals(inlineCmd)) {
                    System.out.println("Found inline command '\\" + cmd + "' in content");
                    return true;
                }
            }
        }
        System.out.println("No inline commands found in content");
        return false;
    }

    public static void main(String[] args) {
        // Example Slashdown input
        String input = "\\h1 project report 2025\n" +
                "\n" +
                "this report outlines the progress of \\b project_x. key milestones include:\n" +
                "\n" +
                "\\h2 milestones\n" +
                "\n" +
                "\\b planning: completed in q1.\n" +
                "\n" +
                "\\b\\i development: ongoing, 70% complete.\n" +
                "\n" +
                "example with backslash: c:\\\\users\\\\john\\\\project_x\n" +
                "\n" +
                "chained formatting: \\b\\i bold_italic and \\u underlined text";

        SlashdownInterpreter interpreter = new SlashdownInterpreter();
        try {
            System.out.println("Starting interpretation of Slashdown input");
            interpreter.interpret(input);
            System.out.println("Interpretation successful");
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}