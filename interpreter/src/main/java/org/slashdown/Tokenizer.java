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
package org.slashdown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private final BufferedReader reader;

    private List<TokenScanner> scanners = new ArrayList<>();

    private List<Token> tokens = new ArrayList<>();

    public Tokenizer(InputStream input) {
        this.reader = new BufferedReader(new InputStreamReader(input));

        // Initialize scanners
        scanners.add(new TokenWhitespace());
        scanners.add(new TokenWord());
        scanners.add(new TokenSymbol());
        scanners.add(new TokenCommand());
        //scanners.add(new TokenNonWhitespace());
    }

    public void tokenize() throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            processLine(line);
        }
    }

    private void processLine(String line) {
        int index = 0;
        while (index < line.length()) {
            boolean matched = false;
            for (TokenScanner scanner : scanners) {
                if (scanner.initialValid(line.charAt(index))) {
                    StringBuilder tokenValue = new StringBuilder();
                    int stop = scanner.scanUntil(line, index);
                    tokenValue.append(line, index, stop);
                    index = stop;
                    tokens.add(new Token(scanner.getType(), tokenValue.toString()));
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                // If no scanner matched, move to the next character to avoid infinite loop
                index++;
            }
        }
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
