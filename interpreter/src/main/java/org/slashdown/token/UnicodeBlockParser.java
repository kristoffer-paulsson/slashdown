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

public class UnicodeBlockParser extends AbstractUnicodeDataParser<UnicodeBlockParser.Block>{

    public UnicodeBlockParser(URL url) throws IOException {
        super(url);
    }

    @Override
    public Block processRow(String[] parts) {
        if (parts.length != 2) {
            throw new IllegalStateException("Must be exactly two fields");
        }
        String range = parts[0].trim();
        String name = parts[1].trim();
        String[] rangeParts = range.split("\\.\\.");
        if (rangeParts.length != 2) {
            throw new IllegalStateException("Failed to parser range");
        }
        int start = Integer.parseInt(rangeParts[0].trim(), 16);
        int end = Integer.parseInt(rangeParts[1].trim(), 16);
        return new Block(start, end, name);
    }

    public static class Block {
        int start;
        int end;
        String name;

        public Block(int start, int end, String name) {
            this.start = start;
            this.end = end;
            this.name = name;
        }

        public String getTag() {
            return this.name.toUpperCase().replace('-', '_').replace(' ', '_');
        }

        public String toString() {
            return String.format("%s - %s: %s", start, end, getTag());
        }
    }

    public static void main(String[] args) {
        try(var parser = new UnicodeBlockParser(new URI("https://www.unicode.org/Public/16.0.0/ucd/Blocks.txt").toURL())) {
            parser.forEachRemaining(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
