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
package org.slashdown.elem;

import org.slashdown.SyntaxError;
import org.slashdown.lexer.AbstractInlineCommand;
import org.slashdown.lexer.Commands;
import org.slashdown.token.Token;
import org.slashdown.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public abstract class Element {

    protected List<AbstractInlineCommand> inlineCommands = new ArrayList<>();

    protected List<Token> tokens = new ArrayList<>();

    protected void openInline(Token token) {

    }

    protected void closeInline(Token token) {

    }

    protected boolean closed = false;

    public boolean isOpen() {
        return !closed;
    }

    public void close() {
        closed = true;
    }

    public abstract boolean offerTokenImpl(Token token);

    public boolean offerToken(Token token) {
        if(!isOpen()) {
            return false;
        }

        boolean result = offerTokenImpl(token);
        if(result) {
            tokens.add(token);
            if(Commands.distinguishInline(token)) {
                // Open or removing inline command.
                Commands.isInline(Commands.inlineCommandFromToken(token), (c) -> {
                    if(inlineCommands.contains(c)) {
                        // Inline commands already given, cannot nest, unnecessary.
                        SyntaxError.raise("Same inline command invoked, can not be nested", token);
                    }
                    if(c.isClosing(token)) {
                        // Checking if it is a closing inline command.
                        if (inlineCommands.getLast() == c) {
                            // Given closing command matches current opening command.
                            inlineCommands.removeLast();
                        } else {
                            SyntaxError.raise("Closing inline command mismatch", token);
                        }
                    } else {
                        // Adding opening inline command.
                        inlineCommands.add(c);
                    }
                });
            }
        }
        return result;
    }

    public boolean isVisible() {
        boolean visible = false;
        for (Token token: tokens) {
            if(token.type() == TokenType.COMMAND || token.type() == TokenType.WORD || token.type() == TokenType.SYMBOL) {
                visible = true;
            }
        }
        return visible;
    }
}
