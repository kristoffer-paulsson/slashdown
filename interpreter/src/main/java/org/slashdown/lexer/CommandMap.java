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
package org.slashdown.lexer;

import org.slashdown.SyntaxError;
import org.slashdown.token.Token;

import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

public class CommandMap {
    public static final Hashtable<String, Command> COMMANDS = new Hashtable<>();

    static {
        registerCommand(new Heading1Command());
        registerCommand(new Heading2Command());
        registerCommand(new Heading3Command());
        registerCommand(new Heading4Command());
        registerCommand(new Heading5Command());
        registerCommand(new Heading6Command());
            /*new HorizontalRuleCommand(),
            new BlockQuoteCommand(),
            new OrderedListCommand(),
            new UnorderedListCommand(),
            new FencedCodeBlockCommand(),
            new IndentedCodeBlockCommand(),
            new TableCommand(),*/
        registerCommand(new ParagraphCommand())
            /*new LineBreakCommand(),
            new BoldCommand(),
            new ItalicCommand(),
            new StrikethroughCommand(),
            new InlineCodeCommand(),
            new LinkCommand(),
            new ImageCommand(),
            new EscapeCommand()*/;
    }

    public static void registerCommand(Command command) {
        COMMANDS.put(command.getName(), command);
    }

    public static Command getCommand(String name) {
        return COMMANDS.get(name);
    }

    public static Command commandFromToken(Token token){
        Command command = getCommand(token.value());
        if(Objects.isNull(command)) {
            SyntaxError.raise("Invalid command", token);
        }
        return command;
    }

    public static Command blockCommandFromToken(Token token){
        Command command = commandFromToken(token);
        if(command.getType() != CommandType.BLOCK) {
            SyntaxError.raise("Not a block command", token);
        }
        return command;
    }
}
