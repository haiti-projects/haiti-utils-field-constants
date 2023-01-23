package dev.struchkov.haiti.utils.fieldconstants.creator;

import dev.struchkov.haiti.utils.Checker;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.ClassTableDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinElemCollectionDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinFieldDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinTableContainer;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.SimpleTableFieldDto;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import static dev.struchkov.haiti.utils.Checker.checkNotEmpty;
import static dev.struchkov.haiti.utils.Exceptions.utilityClass;
import static java.text.MessageFormat.format;

/**
 * // TODO: 20.06.2021 Добавить описание.
 *
 * @author upagge 20.06.2021
 */
public final class CreatorClassTableMode {

    private static final String TABLE_NAME_DEFAULT = "TABLE_NAME";
    private static final String TABLE_NAME_DB = "DB_TABLE_NAME";

    private CreatorClassTableMode() {
        utilityClass();
    }

    public static void record(ClassTableDto classDto, ProcessingEnvironment environment) {
        JavaFileObject builderFile = null;
        try {
            builderFile = environment.getFiler().createSourceFile(classDto.getClassPackage() +"." + classDto.getClassName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            out.println("package " + classDto.getClassPackage() + ";");
            out.println();
            generateImports(classDto.getJoinFields(), classDto.getJoinElemCollections(), out);
            out.println();
            out.print(format("public class {0} '{'", classDto.getClassName()));
            out.println();
            out.println();
            generateTableName(classDto.getTableName(), classDto.getSimpleFields(), out);
            generateSimpleNames(classDto.getSimpleFields(), out);
            generateJoinNames(classDto.getJoinFields(), out);
            generateElementCollectionNames(classDto.getJoinElemCollections(), out);
            out.println("}");
            out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateImports(List<JoinFieldDto> joinFields, List<JoinElemCollectionDto> joinElemCollections, PrintWriter out) {
        if ((joinFields != null && !joinFields.isEmpty()) || (joinElemCollections != null && !joinElemCollections.isEmpty())) {
            out.println("import dev.struchkov.haiti.filter.jooq.join.JoinTable;");
        }
    }

    private static void generateTableName(String tableName, Collection<SimpleTableFieldDto> fields, PrintWriter out) {
        if (tableName != null) {
            final boolean isTableNameField = fields.stream().anyMatch(simpleFieldTableDto -> simpleFieldTableDto.getFieldStringName().equalsIgnoreCase(TABLE_NAME_DEFAULT));
            out.println(format("    public static final String {0} = \"{1}\";", isTableNameField ? TABLE_NAME_DB : TABLE_NAME_DEFAULT, tableName));
            out.println();
        }
    }

    private static void generateSimpleNames(Collection<SimpleTableFieldDto> fields, PrintWriter out) {
        for (SimpleTableFieldDto field : fields) {
            out.println(format("    public static final String {0} = \"{1}\";", field.getFieldStringName(), field.getFieldName()));
        }
        if (checkNotEmpty(fields)) {
            out.println();
        }
    }

    private static void generateJoinNames(List<JoinFieldDto> joinFields, PrintWriter out) {
        for (JoinFieldDto joinField : joinFields) {
            final JoinTableContainer container = joinField.getContainer();
            out.println(
                    format(
                            "    public static final JoinTable {0} = JoinTable.ofLeft(\"{1}\", \"{2}\", \"{3}\");",
                            joinField.getFieldName(), container.getTable(), container.getBaseId(), container.getReference()
                    )
            );
        }
        if (checkNotEmpty(joinFields)) {
            out.println();
        }
    }

    private static void generateElementCollectionNames(List<JoinElemCollectionDto> joinElemCollections, PrintWriter out) {
        for (JoinElemCollectionDto element : joinElemCollections) {
            final String fieldName = element.getFieldName();
            final JoinTableContainer firstContainer = element.getFirstContainer();
            final JoinTableContainer secondContainer = element.getSecondContainer();
            out.println(format("    public static final JoinTable[] {0} = '{'", fieldName));
            out.println(format("            JoinTable.ofLeft(\"{0}\", \"{1}\", \"{2}\"),", firstContainer.getTable(), firstContainer.getBaseId(), firstContainer.getReference()));
            out.println(format("            JoinTable.ofLeft(\"{0}\", \"{1}\", \"{2}\")", secondContainer.getTable(), secondContainer.getBaseId(), secondContainer.getReference()));
            out.println("    };");
        }
        if (checkNotEmpty(joinElemCollections)) {
            out.println();
        }
    }

}
