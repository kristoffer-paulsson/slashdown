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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class UnicodeLoader {

    private boolean loaded = false;

    private final Map<UnicodeBlock, UnicodeConfiguration> configMap = new HashMap<>();
    private final Set<UnicodeBlock> configSet;


    public UnicodeLoader(Map<UnicodeBlock, UnicodeConfiguration> configMap) {
        this.configMap.putAll(configMap);
        this.configSet = configMap.keySet();
    }

    public void loadUnicode() {
        if(loaded) {
            throw new IllegalStateException("Unicode already loaded");
        }

        try(var parser = new UnicodeDataParser(UnicodeDataParser.fromResource("UnicodeData.txt"))) {
            parser.forEachRemaining((u) -> {
                UnicodeBlock b = UnicodeBlock.fromCodePoint(u.getCodePoint());
                if(this.configSet.contains(b)) {
                    this.configMap.get(b).config(u);
                }
            });
        } catch (IOException ignored) {
            throw new RuntimeException();
        }

        this.configMap.forEach((b, c) -> {
            c.setConfigured();
        });
        this.loaded = true;
    }
}
