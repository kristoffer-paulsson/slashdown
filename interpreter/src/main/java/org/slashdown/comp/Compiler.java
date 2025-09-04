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
package org.slashdown.comp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class Compiler implements AutoCloseable {

    protected File outputFile;
    protected FileOutputStream outputStream;

    public void setOutputFile(Path outputFile) {
        try {
            Files.createDirectories(outputFile.getParent());
            this.outputFile = Files.createFile(outputFile).toFile();
            this.outputStream = new FileOutputStream(this.outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    abstract public String getFileSuffix();

    @Override
    public void close() {
        try {
            closeImpl();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void writeString(String out) throws IOException {
        outputStream.write(out.getBytes(StandardCharsets.UTF_8));
    }

    protected abstract void closeImpl() throws IOException;

    public void start() throws IOException{
        startImpl();
    }

    protected abstract void startImpl() throws IOException;

    public void finish() throws IOException{
        finishImpl();
    }

    protected abstract void finishImpl() throws IOException;

}
