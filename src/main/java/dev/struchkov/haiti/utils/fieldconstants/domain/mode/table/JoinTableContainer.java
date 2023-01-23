package dev.struchkov.haiti.utils.fieldconstants.domain.mode.table;

import dev.struchkov.haiti.utils.Inspector;

public class JoinTableContainer {

    private String table;
    private String baseId;
    private String reference;

    private JoinTableContainer(String table, String baseId, String reference) {
        this.table = table;
        this.baseId = baseId;
        this.reference = reference;
    }

    public static JoinTableContainer of(String table, String baseId, String reference) {
        Inspector.isNotNull(table, baseId, reference);
        return new JoinTableContainer(table, baseId, reference);
    }

    public String getTable() {
        return table;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getReference() {
        return reference;
    }

}
