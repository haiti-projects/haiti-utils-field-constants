package dev.struchkov.haiti.utils.fieldconstants.domain;

import java.util.Set;

public class ClassDto {

    private String classPackage;
    private String className;
    private String tableName;
    private Set<FieldDto> fields;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Set<FieldDto> getFields() {
        return fields;
    }

    public void setFields(Set<FieldDto> fields) {
        this.fields = fields;
    }

}
