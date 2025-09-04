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

import org.slashdown.lexer.AbstractBlockCommand;
import org.slashdown.lexer.AbstractInlineCommand;
import org.slashdown.token.Token;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;

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

    protected abstract void closeImpl() throws IOException;

    protected void writeString(String out) throws IOException {
        outputStream.write(out.getBytes(StandardCharsets.UTF_8));
    }

    public void startDocument() throws IOException {
        startDocImpl();
    }

    protected abstract void startDocImpl() throws IOException;

    public void finishDocument() throws IOException{
        finishDocImpl();
    }

    protected abstract void finishDocImpl() throws IOException;

    public void startBlock(Token token, AbstractBlockCommand<?> command) throws IOException {
        startBlkImpl(token, command);
    }

    protected abstract void startBlkImpl(Token token, AbstractBlockCommand<?> command) throws IOException;

    public void finishBlock() throws IOException {
        finishBlkImpl();
    }

    protected abstract void finishBlkImpl() throws IOException;

    public void startInline(Token token, AbstractInlineCommand command) throws IOException {
        startInlImpl(token, command);
    }

    protected abstract void startInlImpl(Token token, AbstractInlineCommand command) throws IOException;

    public void finishInline(Token token, AbstractInlineCommand command) throws IOException {
        finishInlImpl(token, command);
    }

    protected abstract void finishInlImpl(Token token, AbstractInlineCommand command) throws IOException;

    public void whenSimple(Token token, AbstractInlineCommand command) throws IOException {
        whenSimpleImpl(token, command);
    }

    protected abstract void whenSimpleImpl(Token token, AbstractInlineCommand command) throws IOException;

}
