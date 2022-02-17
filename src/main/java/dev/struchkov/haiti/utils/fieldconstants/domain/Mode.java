package dev.struchkov.haiti.utils.fieldconstants.domain;

public enum Mode {

    SIMPLE("Fields"), TABLE("Columns");

    private final String defaultPostfix;

    Mode(String defaultPostfix) {
        this.defaultPostfix = defaultPostfix;
    }

    public String getDefaultPostfix() {
        return defaultPostfix;
    }

}
