package dev.struchkov.haiti.utils.fieldconstants;

import dev.struchkov.haiti.utils.fieldconstants.domain.ClassDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.FieldDto;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import static dev.struchkov.haiti.utils.Exceptions.utilityClass;
import static java.text.MessageFormat.format;

/**
 * // TODO: 20.06.2021 Добавить описание.
 *
 * @author upagge 20.06.2021
 */
public final class ClassCreator {

    private static final String TABLE_NAME_DEFAULT = "TABLE_NAME";
    private static final String TABLE_NAME_DB = "DB_TABLE_NAME";

    private ClassCreator() {
        utilityClass();
    }

    public static void record(ClassDto classDto, ProcessingEnvironment environment) {
        JavaFileObject builderFile = null;
        try {
            builderFile = environment.getFiler().createSourceFile(classDto.getClassName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + classDto.getClassPackage() + ";");
            out.println();
            out.print("public class " + classDto.getClassName() + " {");
            out.println();
            out.println();
            generateTableName(classDto.getTableName(), classDto.getFields(), out);
            generateNames(classDto.getFields(), out);
            out.println();
            out.println("}");
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateTableName(String tableName, Set<FieldDto> fields, PrintWriter out) {
        if (tableName != null) {
            final boolean isTableNameField = fields.stream().anyMatch(fieldDto -> fieldDto.getFieldStringName().equalsIgnoreCase(TABLE_NAME_DEFAULT));
            out.println(format("   public static final String {0} = \"{1}\";", isTableNameField ? TABLE_NAME_DB : TABLE_NAME_DEFAULT, tableName));
        }
    }

    private static void generateNames(Set<FieldDto> fields, PrintWriter out) {
        for (FieldDto field : fields) {
            out.println(format("   public static final String {0} = \"{1}\";", field.getFieldStringName(), field.getFieldName()));
        }
    }

}
