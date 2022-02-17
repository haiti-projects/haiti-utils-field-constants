package dev.struchkov.haiti.utils.fieldconstants.domain.mode.table;

import dev.struchkov.haiti.utils.Assert;

public class JoinElemCollectionDto {

    private String fieldName;
    private JoinTableContainer firstContainer;
    private JoinTableContainer secondContainer;

    private JoinElemCollectionDto(String fieldName, JoinTableContainer firstContainer, JoinTableContainer secondContainer) {
        this.fieldName = fieldName;
        this.firstContainer = firstContainer;
        this.secondContainer = secondContainer;
    }

    public static JoinElemCollectionDto of(String fieldName, JoinTableContainer firstContainer, JoinTableContainer secondContainer) {
        Assert.isNotNull(fieldName, firstContainer, secondContainer);
        return new JoinElemCollectionDto(fieldName, firstContainer, secondContainer);
    }

    public String getFieldName() {
        return fieldName;
    }

    public JoinTableContainer getFirstContainer() {
        return firstContainer;
    }

    public JoinTableContainer getSecondContainer() {
        return secondContainer;
    }

}
