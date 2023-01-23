package dev.struchkov.haiti.utils.fieldconstants.domain.mode.table;

import dev.struchkov.haiti.utils.Inspector;

public class JoinFieldDto {

    private String fieldName;
    private JoinTableContainer container;

    private JoinFieldDto(String fieldName, JoinTableContainer container) {
        this.fieldName = fieldName;
        this.container = container;
    }

    public static JoinFieldDto of(String fieldName, JoinTableContainer container) {
        Inspector.isNotNull(fieldName, container);
        return new JoinFieldDto(fieldName, container);
    }

    public String getFieldName() {
        return fieldName;
    }

    public JoinTableContainer getContainer() {
        return container;
    }

}
