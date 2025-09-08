package org.slashdown.token;

public class StandardScanner {

    private StandardScanner() {

    }

    private static class Holder {
        private static final StandardScanner INSTANCE = new StandardScanner();
    }

    // Global access point
    public static StandardScanner getInstance() {
        return Holder.INSTANCE;
    }
}
