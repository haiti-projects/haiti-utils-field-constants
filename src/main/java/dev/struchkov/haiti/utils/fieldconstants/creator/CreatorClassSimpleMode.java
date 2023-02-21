package dev.struchkov.haiti.utils.fieldconstants.creator;

import dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple.ClassSimpleDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple.SimpleFieldDto;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import static dev.struchkov.haiti.utils.Exceptions.utilityClass;
import static java.text.MessageFormat.format;

/**
 * // TODO: 20.06.2021 Добавить описание.
 *
 * @author upagge 20.06.2021
 */
public final class CreatorClassSimpleMode {

    private CreatorClassSimpleMode() {
        utilityClass();
    }

    public static void record(ClassSimpleDto classDto, ProcessingEnvironment environment) {
        JavaFileObject builderFile = null;
        try {
            builderFile = environment.getFiler().createSourceFile(classDto.getNewClassName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + classDto.getClassPackage() + ";");
            out.println();
            out.println();
            out.println(format("public class {0} '{'", classDto.getNewClassName()));
            out.println();
            out.println(format("    public static final String CLASS_NAME = \"{0}\";", classDto.getOldClassName()));
            out.println(format("    public static final String PACKAGE_AND_CLASS_NAME = \"{0}\";", classDto + classDto.getClassPackage() + "." + classDto.getOldClassName()));
            out.println();
            generateSimpleNames(classDto.getSimpleFields(), out);
            out.println("}");
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void generateSimpleNames(Collection<SimpleFieldDto> fields, PrintWriter out) {
        for (SimpleFieldDto field : fields) {
            out.println(format("    public static final String {0} = \"{1}\";", field.getFieldName(), field.getFieldName()));
        }
        out.println();
    }

}
