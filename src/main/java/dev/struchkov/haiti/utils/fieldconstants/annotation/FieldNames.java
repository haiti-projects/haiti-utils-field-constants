package dev.struchkov.haiti.utils.fieldconstants.annotation;

import dev.struchkov.haiti.utils.fieldconstants.annotation.setting.TableModeSettings;
import dev.struchkov.haiti.utils.fieldconstants.domain.Mode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface FieldNames {

    Mode[] mode() default {Mode.SIMPLE};

    TableModeSettings tableSettings() default @TableModeSettings(prefixTableForColumn = false);

}
