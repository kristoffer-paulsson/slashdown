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

import org.slashdown.comp.Compiler;
import org.slashdown.token.TokenIterator;
import org.slashdown.token.Tokenizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Context {
    private final Path workingDir;
    private final File currentFile; // Current slashdown file
    private final Tokenizer tokenizer;
    private final Interpreter interpreter;
    private final Compiler backend;

    Context(Path slashdown, Compiler backend) {
        if(!Files.isRegularFile(slashdown)) {
            throw new IllegalArgumentException("Must be a file.");
        }
        this.currentFile = slashdown.toFile();
        System.out.println(slashdown);
        this.workingDir = slashdown.toAbsolutePath().getParent();

        try {
            this.tokenizer = new Tokenizer(new FileInputStream(currentFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.backend = backend;
        String filename = currentFile.getName();
        filename = filename.substring(0, filename.lastIndexOf('.'));
        backend.setOutputFile(workingDir.resolve(Path.of("out", String.format("%s.%s", filename, backend.getFileSuffix()))));
        this.interpreter = new Interpreter(new TokenIterator(this.tokenizer));
    }

    Context(String slashdown, Compiler backend) {
        this(Paths.get(slashdown), backend);
    }

    public void interpret() {
        try {
            this.backend.start();
            this.interpreter.interpret();
            this.backend.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
