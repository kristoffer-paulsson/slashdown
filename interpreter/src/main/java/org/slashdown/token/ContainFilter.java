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
package org.slashdown.token;

import java.util.Objects;

public interface ContainFilter extends Filter {

    public boolean isInitial(char c);

    public boolean isMedial(char c);

    public boolean isFinal(char c);

    public default int scanUntil(String line, int start) {
        Subject term = new Subject(line, start);
        enforce(mandatorySingle(term, this::isInitial));
        optionalMulti(term, this::isMedial);
        enforce(mandatorySingle(term, this::isFinal));
        return term.position();
    }

    public static final class Subject {
        private String line;
        private int start;
        private int offset;

        public Subject(String line, int start) {
            reset(line, start);
        }

        public String line() {
            return line;
        }

        public int start() {
            return start;
        }

        public int offset() {
            return offset;
        }

        public void increase() {
            offset++;
        }

        public int position() {
            return start + offset;
        }

        public char currentChar() {
            return line.charAt(position());
        }

        public boolean hasRemaining() {
            return position() < line.length();
        }

        public String advance() {
            String chunk = line.substring(start, start + offset);
            start = position();
            return chunk;
        }

        public void reset(String line) {
            reset(line, 0);
        }

        public void reset(String line, int start) {
            this.line = line;
            this.start = start;
            offset = 0;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (Subject) obj;
            return Objects.equals(this.line, that.line) &&
                    this.start == that.start &&
                    this.offset == that.offset;
        }

        @Override
        public int hashCode() {
            return Objects.hash(line, start, offset);
        }

        @Override
        public String toString() {
            return "Subject[" +
                    "line=" + line + ", " +
                    "start=" + start + ", " +
                    "offset=" + offset + ']';
        }
    }

    public interface Evaluator {
        boolean validation(char c);
    }

    public default boolean optionalSingle(Subject term, Evaluator expr) {
        if(term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return true;
    }

    public default boolean optionalMulti(Subject term, Evaluator expr) {
        while (term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return true;
    }

    public default boolean mandatorySingle(Subject term, Evaluator expr) {
        if(term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return term.offset() > 0;
    }

    public default boolean mandatoryMulti(Subject term, Evaluator expr) {
        while (term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return term.offset() > 0;
    }

    public default void enforce(boolean correct) {
        enforce(correct, "Token scanner internal Syntax Error");
    }

    public default void enforce(boolean correct, String message) {
        if(!correct) {
            throw new IllegalStateException(message);
        }
    }
}
