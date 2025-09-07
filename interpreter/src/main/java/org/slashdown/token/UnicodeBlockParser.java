/**
 * Copyright (c) 2023-2025 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 * <p>
 * Permission to use, copy, modify, and/or distribute this software for any purpose with
 * or without fee is hereby granted, provided that the above copyright notice and this
 * permission notice appear in all copies.
 * <p>
 * THE SOFTWARE IS PROVIDED “AS IS” AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH REGARD
 * TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN
 * NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, INDIRECT, OR
 * CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR
 * PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION,
 * ARISING OUT OF OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 * <p>
 * https://opensource.org/licenses/ISC
 * <p>
 * SPDX-License-Identifier: ISC
 * <p>
 * Contributors:
 * Kristoffer Paulsson - initial implementation
 */
package org.slashdown.token;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UnicodeBlockParser {
    private final List<Block> blocks = new ArrayList<>();

    public static class Block {
        int start;
        int end;
        String name;

        public Block(int start, int end, String name) {
            this.start = start;
            this.end = end;
            this.name = name;
        }
    }

    public void loadBlocks(BufferedReader reader) throws IOException {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                String[] parts = line.split(";", 2);
                if (parts.length != 2) {
                    continue; // Invalid line
                }
                String range = parts[0].trim();
                String name = parts[1].trim();
                String[] rangeParts = range.split("\\.\\.");
                if (rangeParts.length != 2) {
                    continue; // Invalid range
                }
                int start = Integer.parseInt(rangeParts[0].trim(), 16);
                int end = Integer.parseInt(rangeParts[1].trim(), 16);
                blocks.add(new Block(start, end, name));
            }
    }

    public static UnicodeBlockParser loadFromUrl(URL blocks) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(blocks.openStream()))) {
                UnicodeBlockParser parser = new UnicodeBlockParser();
                parser.loadBlocks(reader);
                return parser;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnicodeBlockParser loadFromPath(Path blocks) {
        try {
            try (BufferedReader reader = (Files.newBufferedReader(blocks))) {
                UnicodeBlockParser parser = new UnicodeBlockParser();
                parser.loadBlocks(reader);
                return parser;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UnicodeBlockParser loadFromString(String blocks) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(blocks.getBytes())))) {
                UnicodeBlockParser parser = new UnicodeBlockParser();
                parser.loadBlocks(reader);
                return parser;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Finds the Unicode block name for a given code point.
     * @param codePoint The Unicode code point to scan.
     * @return The block name or "No_Block" if not found.
     */
    public String findBlock(int codePoint) {
        for (Block block : blocks) {
            if (codePoint >= block.start && codePoint <= block.end) {
                return block.name;
            }
        }
        return "No_Block";
    }

    public static void main(String[] args) {
        try {
            URL url = new URI("https://www.unicode.org/Public/16.0.0/ucd/Blocks.txt").toURL();
            UnicodeBlockParser parser = UnicodeBlockParser.loadFromUrl(url);

            // Test examples
            System.out.println(parser.findBlock(0x0041)); // A - Basic Latin
            System.out.println(parser.findBlock(0x20A0)); // Euro - Currency Symbols
            System.out.println(parser.findBlock(0x1F600)); // Grinning Face - Emoticons
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
