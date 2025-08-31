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

import org.slashdown.elem.Element;
import org.slashdown.elem.Paragraph;
import org.slashdown.lexer.*;
import org.slashdown.token.Token;
import org.slashdown.token.TokenIterator;
import org.slashdown.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class Interpreter {

    private TokenIterator tokenIterator;

    private List<Element> blocks = new ArrayList<>();
    private Element currentBlock = new Paragraph();

    Interpreter(TokenIterator tokenIterator) {
        this.tokenIterator = tokenIterator;
    }

    public void interpret() {

        while (tokenIterator.hasNext()) {
            Token token = tokenIterator.next();

            if(token.type() == TokenType.COMMAND) {
                Command command = Commands.commandFromToken(token);
                if (command.getType() == CommandType.BLOCK) {
                    AbstractBlockCommand block = (AbstractBlockCommand) command;
                    blocks.add(currentBlock);
                    currentBlock = block.generateElement();
                }
            }

            while (tokenIterator.hasNext()) {
                if(!currentBlock.offerToken(token)) {
                    blocks.add(currentBlock);
                    currentBlock = new Paragraph();
                    currentBlock.offerToken(token);
                    break;
                }
            }
        }
    }
}
