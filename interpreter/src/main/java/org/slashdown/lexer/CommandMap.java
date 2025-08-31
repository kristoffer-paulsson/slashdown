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

import java.util.Hashtable;


public class CommandMap {
    public static final Hashtable<String, Command> COMMANDS = new Hashtable<>();

    public static final Hashtable<String, String> TAGS = new Hashtable<>();

    static {
        registerCommand(new BackslashCommand());
        registerCommand(new SofthyphenCommand());
        registerCommand(new NonbreakingCommand());
        registerCommand(new NewlineCommand());
        registerCommand(new EllipsisCommand());
        registerCommand(new EndashCommand());
        registerCommand(new EmdashCommand());

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
        registerCommand(new ParagraphCommand());
            //new LineBreakCommand(),
        registerCommand(new BoldCommand());
        registerCommand(new ItalicCommand());
        registerCommand(new UnderlineCommand());
        registerCommand(new StrikethroughCommand());
        registerCommand(new SingleQuoteCommand());
        registerCommand(new DoubleQuoteCommand());
        registerCommand(new SuperscriptCommand());
            /*new InlineCodeCommand(),
            new LinkCommand(),
            new ImageCommand(),
            new EscapeCommand()*/;
    }

    public static void registerCommand(Command command) {
        Commands.isBlock(command, (c) -> {
            COMMANDS.put(c.getName(), c);
            TAGS.put(c.getTag(), c.getName());
        });
        Commands.isInline(command, (c) -> {
            COMMANDS.put(c.getName(), c);
            TAGS.put(c.getTag(), c.getName());
            TAGS.put(c.getClosingTag(), c.getName());
        });
        Commands.isSimple(command, (c) -> {
            COMMANDS.put(c.getName(), c);
            TAGS.put(c.getTag(), c.getName());
            try {
                TAGS.put(c.getTag(), c.getName());
            } catch (IllegalStateException e) {
                // Pass
            }
        });
    }
}
