package dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple;

import java.util.List;

public class ClassSimpleDto {

    private String classPackage;
    private String className;
    private String tableName;
    private List<SimpleFieldDto> simpleFields;

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SimpleFieldDto> getSimpleFields() {
        return simpleFields;
    }

    public void setSimpleFields(List<SimpleFieldDto> simpleFields) {
        this.simpleFields = simpleFields;
    }

}
