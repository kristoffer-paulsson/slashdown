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

public abstract class AbstractScanner implements Filter {

    public abstract int scanUntil(Subject term);

    public interface Evaluator {
        boolean validation(char c);
    }

    protected boolean optionalSingle(Subject term, Evaluator expr) {
        if(term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return true;
    }

    protected boolean optionalMulti(Subject term, Evaluator expr) {
        while (term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return true;
    }

    protected boolean mandatorySingle(Subject term, Evaluator expr) {
        if(term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return term.offset() > 0;
    }

    protected boolean mandatoryMulti(Subject term, Evaluator expr) {
        while (term.hasRemaining() && expr.validation(term.currentChar())) {
            term.increase();
        }
        return term.offset() > 0;
    }

    protected void enforce(boolean correct) {
        enforce(correct, "Token scanner internal Syntax Error");
    }

    protected void enforce(boolean correct, String message) {
        if(!correct) {
            throw new IllegalStateException(message);
        }
    }
}
