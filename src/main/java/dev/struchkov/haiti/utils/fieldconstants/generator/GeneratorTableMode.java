package dev.struchkov.haiti.utils.fieldconstants.generator;

import dev.struchkov.haiti.utils.fieldconstants.annotation.field.ElementCollectionField;
import dev.struchkov.haiti.utils.fieldconstants.annotation.field.JoinField;
import dev.struchkov.haiti.utils.fieldconstants.annotation.setting.TableModeSettings;
import dev.struchkov.haiti.utils.fieldconstants.creator.CreatorClassTableMode;
import dev.struchkov.haiti.utils.fieldconstants.domain.Mode;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.ClassTableDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinElemCollectionDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinFieldDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinTableContainer;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.SimpleTableFieldDto;
import dev.struchkov.haiti.utils.fieldconstants.util.Generator;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Table;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static dev.struchkov.haiti.utils.Checker.checkNotNull;

public class GeneratorTableMode {

    public static void generate(ProcessingEnvironment processingEnv, TableModeSettings tableSettings, Element annotatedElement) {
        final String annotatedElementName = annotatedElement.getSimpleName().toString();

        final Table anTable = annotatedElement.getAnnotation(Table.class);
        final String newClassName = annotatedElementName + Mode.TABLE.getDefaultPostfix();

        final List<? extends Element> allFields = annotatedElement.getEnclosedElements().stream()
                .filter(Generator::isField)
                .filter(Generator::isNotIgnoreField)
                .collect(Collectors.toList());

        final List<SimpleTableFieldDto> simpleFields = getSimpleFields(allFields, anTable, tableSettings);
        final List<JoinFieldDto> joinFields = getJoinFields(allFields);
        final List<JoinElemCollectionDto> elementCollectionFields = getElementCollectionsFields(anTable, allFields);

        final ClassTableDto newClass = new ClassTableDto();
        newClass.setClassName(newClassName);
        newClass.setSimpleFields(simpleFields);
        newClass.setJoinFields(joinFields);
        newClass.setJoinElemCollections(elementCollectionFields);
        newClass.setClassPackage(Generator.getPackage(annotatedElement));
        newClass.setTableName(checkNotNull(anTable) ? anTable.name() : null);
        newClass.setTableSchema(checkNotNull(anTable) ? anTable.schema() : null);
        CreatorClassTableMode.record(newClass, processingEnv);
    }

    private static List<JoinElemCollectionDto> getElementCollectionsFields(Table tableName, List<? extends Element> allFields) {
        if (checkNotNull(tableName)) {
            return allFields.stream()
                    .filter(
                            field -> field.getAnnotation(ElementCollectionField.class) != null &&
                                     field.getAnnotation(CollectionTable.class) != null &&
                                     field.getAnnotation(Column.class) != null
                    )
                    .map(field -> {
                        final String fieldName = field.getSimpleName().toString();
                        final ElementCollectionField elementCollectionField = field.getAnnotation(ElementCollectionField.class);
                        final CollectionTable collectionTable = field.getAnnotation(CollectionTable.class);
                        final Column column = field.getAnnotation(Column.class);

                        final JoinTableContainer firstContainer = JoinTableContainer.of(collectionTable.name(), tableName + "." + elementCollectionField.parentId(), collectionTable.joinColumns()[0].name());
                        final JoinTableContainer secondContainer = JoinTableContainer.of(elementCollectionField.childTable(), column.name(), elementCollectionField.childReference());
                        return JoinElemCollectionDto.of(fieldName, firstContainer, secondContainer);
                    }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private static List<JoinFieldDto> getJoinFields(List<? extends Element> allFields) {
        return allFields.stream()
                .filter(
                        field -> field.getAnnotation(JoinField.class) != null &&
                                 field.getAnnotation(ElementCollection.class) == null
                )
                .map(field -> {
                    final String fieldName = field.getSimpleName().toString();
                    final JoinField joinField = field.getAnnotation(JoinField.class);
                    final JoinTableContainer joinContainer = JoinTableContainer.of(joinField.table(), joinField.baseId(), joinField.reference());
                    return JoinFieldDto.of(fieldName, joinContainer);
                })
                .collect(Collectors.toList());
    }

    private static List<SimpleTableFieldDto> getSimpleFields(List<? extends Element> allFields, Table anTable, TableModeSettings tableSettings) {
        final boolean prefixTableForColumn = tableSettings.prefixTableForColumn();
        final List<SimpleTableFieldDto> resultList = new ArrayList<>();
        allFields.stream()
                .filter(
                        field -> field.getAnnotation(Column.class) != null &&
                                 field.getAnnotation(ElementCollection.class) == null
                )
                .forEach(
                        field -> {
                            final String fieldName = field.getSimpleName().toString();
                            final String columnName = field.getAnnotation(Column.class).name();
                            if (prefixTableForColumn && checkNotNull(anTable)) {
                                final String tableNameAndColumnName = anTable.name() + "." + columnName;
                                resultList.add(SimpleTableFieldDto.of("t_" + fieldName, tableNameAndColumnName));
                            }
                            resultList.add(SimpleTableFieldDto.of(fieldName, columnName));
                        }
                );
        return resultList;
    }

}
