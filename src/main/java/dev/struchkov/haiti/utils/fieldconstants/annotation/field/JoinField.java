package dev.struchkov.haiti.utils.fieldconstants.annotation.field;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface JoinField {

    String table();
    String baseId();
    String reference();

}
