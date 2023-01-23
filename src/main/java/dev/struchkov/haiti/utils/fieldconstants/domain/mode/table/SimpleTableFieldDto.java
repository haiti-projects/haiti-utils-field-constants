package dev.struchkov.haiti.utils.fieldconstants.domain.mode.table;

/**
 * // TODO: 07.06.2021 Добавить описание.
 *
 * @author upagge 07.06.2021
 */
public class SimpleTableFieldDto {

    private final String fieldStringName;
    private final String fieldName;

    private SimpleTableFieldDto(String fieldStringName, String fieldName) {
        this.fieldStringName = fieldStringName;
        this.fieldName = fieldName;
    }

    public static SimpleTableFieldDto of(String fieldStringName, String fieldName) {
        return new SimpleTableFieldDto(fieldStringName, fieldName);
    }

    public String getFieldStringName() {
        return fieldStringName;
    }

    public String getFieldName() {
        return fieldName;
    }

}
