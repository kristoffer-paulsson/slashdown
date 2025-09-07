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
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public abstract class AbstractUnicodeDataParser<E> implements Iterator<E>, AutoCloseable {
    private BufferedReader reader;
    private String nextLine;

    public AbstractUnicodeDataParser(URL url) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        initialize(reader);
    }

    public AbstractUnicodeDataParser(Path file) throws IOException {
        BufferedReader reader = Files.newBufferedReader(file);
        initialize(reader);
    }

    public AbstractUnicodeDataParser(String str) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(str.getBytes())));
        initialize(reader);
    }

    public AbstractUnicodeDataParser(BufferedReader reader) throws IOException {
        initialize(reader);
    }

    protected void initialize(BufferedReader reader) throws IOException {
        this.reader = reader;
        advance(); // Read the first line
    }

    private void advance() {
        try {
            nextLine = null;
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                nextLine = line;
                break;
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return nextLine != null;
    }

    @Override
    public E next() {
        if (nextLine == null) {
            throw new NoSuchElementException();
        }

        String[] parts = nextLine.split(";");
        E product = processRow(parts);
        advance();
        return product;
    }

    public abstract E processRow(String[] parts);

    @Override
    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
