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
    private static final Hashtable<String, Command> COMMANDS = new Hashtable<>();

    private static final Hashtable<String, String> TAGS = new Hashtable<>();

    static {
        registerCommand(new UnicodeCommand());

        // Special escape characters
        registerCommand(new EscapeBackslashCommand());
        registerCommand(new EscapeTildeCommand());
        registerCommand(new EscapeAsteriskCommand());
        registerCommand(new EscapeSemicolonCommand());

        // Special formatting commands
        registerCommand(new SoftHyphenCommand());
        registerCommand(new NonBreakingSpaceCommand());
        registerCommand(new BreakingCommand());
        registerCommand(new EllipsisCommand());
        registerCommand(new EnDashCommand());
        registerCommand(new EmDashCommand());

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
        registerCommand(new SuperScriptCommand());
            /*new InlineCodeCommand(),
            new LinkCommand(),
            new ImageCommand(),
            new EscapeCommand()*/;
    }

    private static void setTag(String key, String value) {
        if(TAGS.containsKey(key)) {
            throw new IllegalStateException("Tag " + key + " is already taken!");
        }
        TAGS.put(key, value);
    }

    private static void setCommand(String key, Command command) {
        if(COMMANDS.containsKey(key)) {
            throw new IllegalStateException("Command " + key + " is already taken!");
        }
        COMMANDS.put(key, command);
    }

    public static void registerCommand(Command command) {
        Commands.isBlock(command, (c) -> {
            setCommand(c.getName(), c);
            setTag(c.getTag(), c.getName());
        });
        Commands.isInline(command, (c) -> {
            setCommand(c.getName(), c);
            setTag(c.getTag(), c.getName());
            setTag(c.getClosingTag(), c.getName());
        });
        Commands.isSimple(command, (c) -> {
            setCommand(c.getName(), c);
            setTag(c.getTag(), c.getName());
            String symbol = c.getSymbolTag();
            if(!symbol.isBlank()) {
                setTag(c.getSymbolTag(), c.getName());
            }
        });
    }

    public static Command getCommand(String tag) {
        return CommandMap.COMMANDS.get(CommandMap.TAGS.getOrDefault(tag, ""));
    }
}
