package dev.struchkov.haiti.utils.fieldconstants.util;

import dev.struchkov.haiti.utils.fieldconstants.annotation.field.IgnoreField;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class Generator {

    public static boolean isNotIgnoreField(Element element) {
        return element.getAnnotation(IgnoreField.class) == null;
    }

    public static boolean isField(Element element) {
        return element != null && element.getKind().isField();
    }

    public static String getPackage(Element element) {
        Element packageElem = element.getEnclosingElement();
        while (!ElementKind.PACKAGE.equals(packageElem.getKind())) {
            packageElem = packageElem.getEnclosingElement();
        }
        return packageElem.asType().toString();
    }

}
