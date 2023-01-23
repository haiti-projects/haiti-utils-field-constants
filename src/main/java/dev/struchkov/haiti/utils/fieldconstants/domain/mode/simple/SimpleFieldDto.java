package dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple;

/**
 * // TODO: 07.06.2021 Добавить описание.
 *
 * @author upagge 07.06.2021
 */
public class SimpleFieldDto {

    private final String fieldName;

    private SimpleFieldDto(String fieldName) {
        this.fieldName = fieldName;
    }

    public static SimpleFieldDto of(String fieldName) {
        return new SimpleFieldDto(fieldName);
    }

    public String getFieldName() {
        return fieldName;
    }

}
