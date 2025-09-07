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

import java.io.BufferedReader;
import java.io.IOException;

public class UnicodeDataParser extends AbstractUnicodeDataParser<UnicodeDataParser.UnicodeData>{

    public UnicodeDataParser(BufferedReader reader) throws IOException {
        super(reader);
    }

    @Override
    public UnicodeData processRow(String[] parts) {
        //System.out.println(parts.length);
        /*if (parts.length >= 11) {
            throw new IllegalStateException("Must be exactly eleven fields");
        }*/
        int codePoint = Integer.parseInt(parts[0].trim(), 16);
        String name = parts[1].trim();
        UnicodeCategory uc = UnicodeCategory.fromAbbreviation(parts[2].trim());
        return new UnicodeData(codePoint, name, uc);
    }

    public static class UnicodeData {
        int codePoint;
        String name;
        UnicodeCategory category;

        public UnicodeData(int codePoint, String name, UnicodeCategory category) {
            this.codePoint = codePoint;
            this.name = name;
            this.category = category;
        }

        public int getCodePoint() {
            return this.codePoint;
        }

        public String getName() {
            return this.name;
        }

        public UnicodeCategory getCategory() {
            return this.category;
        }

        public String getTag() {
            return this.name.toUpperCase().replace('-', '_').replace(' ', '_');
        }

        public String toString() {
            return String.format("%s, %s, %s", codePoint, name, category.getAbbreviation());
        }
    }

    public static void main(String[] args) {
        try(var parser = new UnicodeDataParser(UnicodeDataParser.fromResource("UnicodeData.txt"))) {
            parser.forEachRemaining((u) -> {
                if(u.category.isNumber()) {
                    System.out.println(u);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
