package dev.struchkov.haiti.utils.fieldconstants.domain.mode.table;

import java.util.List;

public class ClassTableDto {

    private String classPackage;
    private String className;
    private String tableName;
    private String tableSchema;
    private List<SimpleTableFieldDto> simpleFields;
    private List<JoinFieldDto> joinFields;
    private List<JoinElemCollectionDto> joinElemCollections;

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

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public List<SimpleTableFieldDto> getSimpleFields() {
        return simpleFields;
    }

    public void setSimpleFields(List<SimpleTableFieldDto> simpleFields) {
        this.simpleFields = simpleFields;
    }

    public List<JoinFieldDto> getJoinFields() {
        return joinFields;
    }

    public void setJoinFields(List<JoinFieldDto> joinFields) {
        this.joinFields = joinFields;
    }

    public List<JoinElemCollectionDto> getJoinElemCollections() {
        return joinElemCollections;
    }

    public void setJoinElemCollections(List<JoinElemCollectionDto> joinElemCollections) {
        this.joinElemCollections = joinElemCollections;
    }

}
