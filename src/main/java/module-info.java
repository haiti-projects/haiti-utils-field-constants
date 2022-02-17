module haiti.utils.fieldconstants {
    exports dev.struchkov.haiti.utils.fieldconstants.annotation;
    exports dev.struchkov.haiti.utils.fieldconstants.annotation.field;
    exports dev.struchkov.haiti.utils.fieldconstants.annotation.setting;
    exports dev.struchkov.haiti.utils.fieldconstants.domain;

    requires java.compiler;
    requires com.google.auto.service;
    requires java.persistence;
    requires haiti.utils;
}