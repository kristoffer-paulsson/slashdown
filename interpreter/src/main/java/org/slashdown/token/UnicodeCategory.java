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

/**
 * Java representation of Unicode General_Category values (Table 12, UAX #44).
 * Enum constants use the long names, formatted in uppercase with underscores.
 * Includes convenience is*() methods for grouped categories.
 */
public enum UnicodeCategory {

    UPPERCASE_LETTER("Lu", "an uppercase letter"),
    LOWERCASE_LETTER("Ll", "a lowercase letter"),
    TITLECASE_LETTER("Lt", "a digraphic character, with first part uppercase"),
    CASED_LETTER("LC", "Lu | Ll | Lt"),
    MODIFIER_LETTER("Lm", "a modifier letter"),
    OTHER_LETTER("Lo", "other letters, including syllables and ideographs"),
    LETTER("L", "Lu | Ll | Lt | Lm | Lo"),

    NONSPACING_MARK("Mn", "a nonspacing combining mark (zero advance width)"),
    SPACING_MARK("Mc", "a spacing combining mark (positive advance width)"),
    ENCLOSING_MARK("Me", "an enclosing combining mark"),
    MARK("M", "Mn | Mc | Me"),

    DECIMAL_NUMBER("Nd", "a decimal digit"),
    LETTER_NUMBER("Nl", "a letterlike numeric character"),
    OTHER_NUMBER("No", "a numeric character of other type"),
    NUMBER("N", "Nd | Nl | No"),

    CONNECTOR_PUNCTUATION("Pc", "a connecting punctuation mark, like a tie"),
    DASH_PUNCTUATION("Pd", "a dash or hyphen punctuation mark"),
    OPEN_PUNCTUATION("Ps", "an opening punctuation mark (of a pair)"),
    CLOSE_PUNCTUATION("Pe", "a closing punctuation mark (of a pair)"),
    INITIAL_PUNCTUATION("Pi", "an initial quotation mark"),
    FINAL_PUNCTUATION("Pf", "a final quotation mark"),
    OTHER_PUNCTUATION("Po", "a punctuation mark of other type"),
    PUNCTUATION("P", "Pc | Pd | Ps | Pe | Pi | Pf | Po"),

    MATH_SYMBOL("Sm", "a symbol of mathematical use"),
    CURRENCY_SYMBOL("Sc", "a currency sign"),
    MODIFIER_SYMBOL("Sk", "a non-letterlike modifier symbol"),
    OTHER_SYMBOL("So", "a symbol of other type"),
    SYMBOL("S", "Sm | Sc | Sk | So"),

    SPACE_SEPARATOR("Zs", "a space character (of various non-zero widths)"),
    LINE_SEPARATOR("Zl", "U+2028 LINE SEPARATOR only"),
    PARAGRAPH_SEPARATOR("Zp", "U+2029 PARAGRAPH SEPARATOR only"),
    SEPARATOR("Z", "Zs | Zl | Zp"),

    CONTROL("Cc", "a C0 or C1 control code"),
    FORMAT("Cf", "a format control character"),
    SURROGATE("Cs", "a surrogate code point"),
    PRIVATE_USE("Co", "a private-use character"),
    UNASSIGNED("Cn", "a reserved unassigned code point or a noncharacter"),
    OTHER("C", "Cc | Cf | Cs | Co | Cn");

    private final String abbreviation;
    private final String description;

    UnicodeCategory(String abbreviation, String description) {
        this.abbreviation = abbreviation;
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Look up enum by abbreviation (e.g., "Lu").
     */
    public static UnicodeCategory fromAbbreviation(String abbr) {
        for (UnicodeCategory gc : values()) {
            if (gc.abbreviation.equals(abbr)) {
                return gc;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name() + " (" + abbreviation + ")";
    }

    /* ---------------- Grouped category helpers ---------------- */

    public boolean isLetter() {
        return this == LETTER || this == UPPERCASE_LETTER || this == LOWERCASE_LETTER
                || this == TITLECASE_LETTER || this == CASED_LETTER
                || this == MODIFIER_LETTER || this == OTHER_LETTER;
    }

    public boolean isCasedLetter() {
        return this == CASED_LETTER || this == UPPERCASE_LETTER
                || this == LOWERCASE_LETTER || this == TITLECASE_LETTER;
    }

    public boolean isMark() {
        return this == MARK || this == NONSPACING_MARK
                || this == SPACING_MARK || this == ENCLOSING_MARK;
    }

    public boolean isNumber() {
        return this == NUMBER || this == DECIMAL_NUMBER
                || this == LETTER_NUMBER || this == OTHER_NUMBER;
    }

    public boolean isPunctuation() {
        return this == PUNCTUATION || this == CONNECTOR_PUNCTUATION
                || this == DASH_PUNCTUATION || this == OPEN_PUNCTUATION
                || this == CLOSE_PUNCTUATION || this == INITIAL_PUNCTUATION
                || this == FINAL_PUNCTUATION || this == OTHER_PUNCTUATION;
    }

    public boolean isSymbol() {
        return this == SYMBOL || this == MATH_SYMBOL
                || this == CURRENCY_SYMBOL || this == MODIFIER_SYMBOL
                || this == OTHER_SYMBOL;
    }

    public boolean isSeparator() {
        return this == SEPARATOR || this == SPACE_SEPARATOR
                || this == LINE_SEPARATOR || this == PARAGRAPH_SEPARATOR;
    }

    public boolean isOther() {
        return this == OTHER || this == CONTROL || this == FORMAT
                || this == SURROGATE || this == PRIVATE_USE || this == UNASSIGNED;
    }
}

