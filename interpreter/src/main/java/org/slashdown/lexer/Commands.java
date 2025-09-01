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
import org.slashdown.token.Tokens;

import java.util.Objects;
import java.util.function.Consumer;

public class Commands {
    public static Command getCommand(String tag) {
        return CommandMap.COMMANDS.get(CommandMap.TAGS.getOrDefault(tag, ""));
    }

    public static Command commandFromToken(Token token){
        String value = token.value();
        Command command;

        boolean variable = hasVariable(token);
        if(variable) {
            command = getCommand(extractTag(token));
        } else {
            command = getCommand(value);
        }

        if(Objects.isNull(command)) {
            SyntaxError.raise("Invalid command", token);
        } else if(command.variableSupport() == Variable.MANDATORY && !variable) {
            SyntaxError.raise("Variable is mandatory", token);
        } else if(command.variableSupport() == Variable.PROHIBITED && variable) {
            SyntaxError.raise("Variable is prohibited", token);
        }

        return command;
    }

    public static AbstractBlockCommand<?> blockCommandFromToken(Token token){
        Command command = commandFromToken(token);
        if(command.getType() != CommandType.BLOCK) {
            SyntaxError.raise("Not a block command", token);
        }
        return (AbstractBlockCommand<?>) command;
    }

    public static AbstractInlineCommand inlineCommandFromToken(Token token){
        Command command = commandFromToken(token);
        if(command.getType() != CommandType.INLINE) {
            SyntaxError.raise("Not an inline command", token);
        }
        return (AbstractInlineCommand) command;
    }

    public static AbstractSimpleCommand simpleCommandFromToken(Token token){
        Command command = commandFromToken(token);
        if(command.getType() != CommandType.SIMPLE) {
            SyntaxError.raise("Not a simple command", token);
        }
        return (AbstractSimpleCommand) command;
    }

    public static void isBlock(Command command, Consumer<AbstractBlockCommand<?>> action) {
        if(Objects.nonNull(command) && command.getType() == CommandType.BLOCK) {
            action.accept((AbstractBlockCommand<?>) command);
        }
    }

    public static void isInline(Command command, Consumer<AbstractInlineCommand> action) {
        if(Objects.nonNull(command) && command.getType() == CommandType.INLINE) {
            action.accept((AbstractInlineCommand) command);
        }
    }

    public static void isSimple(Command command, Consumer<AbstractSimpleCommand> action) {
        if(Objects.nonNull(command) && command.getType() == CommandType.SIMPLE) {
            action.accept((AbstractSimpleCommand) command);
        }
    }

    public static boolean distinguishBlock(Token token) {
        Command command = getCommand(token.value());
        return Objects.nonNull(command) && command.getType() == CommandType.BLOCK;
    }

    public static boolean distinguishInline(Token token) {
        Command command = getCommand(token.value());
        return Objects.nonNull(command) && command.getType() == CommandType.INLINE;
    }

    public static boolean distinguishSimple(Token token) {
        Command command = getCommand(token.value());
        return Objects.nonNull(command) && command.getType() == CommandType.SIMPLE;
    }

    public static boolean hasVariable(Token token) {
        String value = token.value();
        return value.contains(":") || value.endsWith(";");
    }

    public static void examineVariableCommand(Token token) {
        String value = token.value();

        if(!value.startsWith("\\") || !value.contains(":") || !value.endsWith(";")) {
            SyntaxError.raise("Command tag and variable not properly delimited", token);
        }
    }

    public static String extractVariable(Token token) {
        examineVariableCommand(token);
        String value = token.value();

        String variable = value.substring(value.indexOf(':')+1, value.indexOf(';'));

        if(!Tokens.isWordCompliant(variable)) {
            SyntaxError.raise("Command variable is not word compliant", token);
        }
        return variable;
    }

    public static String extractTag(Token token) {
        examineVariableCommand(token);
        String value = token.value();

        return value.substring(0, value.indexOf(':'));
    }
}
