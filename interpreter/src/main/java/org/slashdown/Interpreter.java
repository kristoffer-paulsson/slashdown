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

import org.slashdown.el.BlockElement;
import org.slashdown.token.Token;
import org.slashdown.token.TokenIterator;
import org.slashdown.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private TokenIterator tokenIterator;

    private List<BlockElement> blocks = new ArrayList<>();
    private BlockElement currentBlock;

    Interpreter(TokenIterator tokenIterator) {
        this.tokenIterator = tokenIterator;
    }

    public void interpret() {
        int eolCount = 0;

        while (tokenIterator.hasNext()) {
            Token token = tokenIterator.next();
            if (token.type() == org.slashdown.token.TokenType.EOL) {
                eolCount++;
            } else if(eolCount > 0 && token.type() != TokenType.WHITESPACE) {
                if(eolCount > 1) {
                    System.out.println("PARAGRAPH"); // Print an extra newline for paragraph break
                } else {
                    if(eolCount == 1) {
                        System.out.println("EOL");
                    }
                    System.out.println(token);
                }
                eolCount = 0;
            } else {
                System.out.println(token);
            }
        }
    }
}
