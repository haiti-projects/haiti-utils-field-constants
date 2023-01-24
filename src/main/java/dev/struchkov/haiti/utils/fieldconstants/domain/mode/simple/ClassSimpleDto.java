package dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple;

import java.util.List;

public class ClassSimpleDto {

    private String classPackage;
    private String newClassName;
    private String oldClassName;
    private List<SimpleFieldDto> simpleFields;

    public String getClassPackage() {
        return classPackage;
    }

    public void setClassPackage(String classPackage) {
        this.classPackage = classPackage;
    }

    public String getNewClassName() {
        return newClassName;
    }

    public void setNewClassName(String newClassName) {
        this.newClassName = newClassName;
    }

    public String getOldClassName() {
        return oldClassName;
    }

    public void setOldClassName(String oldClassName) {
        this.oldClassName = oldClassName;
    }

    public List<SimpleFieldDto> getSimpleFields() {
        return simpleFields;
    }

    public void setSimpleFields(List<SimpleFieldDto> simpleFields) {
        this.simpleFields = simpleFields;
    }

}
