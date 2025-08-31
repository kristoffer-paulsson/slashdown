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
import org.slashdown.lexer.Commands;
import org.slashdown.token.Token;
import org.slashdown.token.TokenType;

public class Headline extends Element{

    private boolean eolReached = false;

    private final int level;

    public Headline(int level) {
        this.level = level;
        System.out.println("NEW HEADLINE " + level);
    }

    public int getLevel() {
        return level;
    }

    /**
     *
     * */
    @Override
    public boolean offerTokenImpl(Token token) {
        if(token.type() == TokenType.EOL) {
            // Close headline block on first EOL
            close();
        } else if(token.type() == TokenType.COMMAND) {
            // Accept the initial block command, but no later block commands.
            Commands.isBlock(Commands.commandFromToken(token), (c) -> {
                if(!tokens.isEmpty()) {
                    SyntaxError.raise("Illegal block command", token);
                }
            });
        }

        tokens.add(token);
        return true;
    }
}
