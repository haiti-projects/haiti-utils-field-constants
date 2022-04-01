package dev.struchkov.haiti.utils.fieldconstants.processor;

import com.google.auto.service.AutoService;
import dev.struchkov.haiti.utils.fieldconstants.CreatorClassTableMode;
import dev.struchkov.haiti.utils.fieldconstants.annotation.FieldNames;
import dev.struchkov.haiti.utils.fieldconstants.annotation.field.ElementCollectionField;
import dev.struchkov.haiti.utils.fieldconstants.annotation.field.IgnoreField;
import dev.struchkov.haiti.utils.fieldconstants.annotation.field.JoinField;
import dev.struchkov.haiti.utils.fieldconstants.annotation.setting.TableModeSettings;
import dev.struchkov.haiti.utils.fieldconstants.domain.Mode;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.ClassTableDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinElemCollectionDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinFieldDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.JoinTableContainer;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.table.SimpleFieldTableDto;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SupportedAnnotationTypes("dev.struchkov.haiti.utils.fieldconstants.annotation.FieldNames")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class FieldNameProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (TypeElement annotation : set) {
            Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                final TypeMirror mirror = annotatedElement.asType();
                final String annotatedElementName = annotatedElement.getSimpleName().toString();
                final FieldNames settings = annotatedElement.getAnnotation(FieldNames.class);

                final Set<Mode> modes = Arrays.stream(settings.mode()).collect(Collectors.toUnmodifiableSet());
                if (modes.contains(Mode.TABLE)) {
                    final TableModeSettings tableSettings = settings.tableSettings();
                    generateTableMode(tableSettings, annotatedElement, mirror, annotatedElementName);
                }
            }
        }
        return true;
    }

    private void generateTableMode(TableModeSettings tableSettings, Element annotatedElement, TypeMirror mirror, String annotatedElementName) {
        final Table anTable = annotatedElement.getAnnotation(Table.class);
        final String newClassName = annotatedElementName + Mode.TABLE.getDefaultPostfix();

        final List<? extends Element> allFields = annotatedElement.getEnclosedElements().stream()
                .filter(this::isField)
                .filter(this::isNotIgnoreField)
                .collect(Collectors.toList());

        final List<SimpleFieldTableDto> simpleFields = getSimpleFields(allFields, anTable, tableSettings);
        final List<JoinFieldDto> joinFields = getJoinFields(allFields);
        final List<JoinElemCollectionDto> elementCollectionFields = getElementCollectionsFields(anTable.name(), allFields);

        final ClassTableDto newClass = new ClassTableDto();
        newClass.setClassName(newClassName);
        newClass.setSimpleFields(simpleFields);
        newClass.setJoinFields(joinFields);
        newClass.setJoinElemCollections(elementCollectionFields);
        newClass.setClassPackage(getPackage(mirror));
        newClass.setTableName(anTable.name());
        CreatorClassTableMode.record(newClass, processingEnv);
    }

    private List<JoinElemCollectionDto> getElementCollectionsFields(String tableName, List<? extends Element> allFields) {
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

    private List<JoinFieldDto> getJoinFields(List<? extends Element> allFields) {
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

    private List<SimpleFieldTableDto> getSimpleFields(List<? extends Element> allFields, Table anTable, TableModeSettings tableSettings) {
        final boolean prefixTableForColumn = tableSettings.prefixTableForColumn();
        final List<SimpleFieldTableDto> resultList = new ArrayList<>();
        allFields.stream()
                .filter(
                        field -> field.getAnnotation(Column.class) != null &&
                                field.getAnnotation(ElementCollection.class) == null
                )
                .forEach(
                        field -> {
                            final String fieldName = field.getSimpleName().toString();
                            final String columnName = field.getAnnotation(Column.class).name();
                            if (prefixTableForColumn) {
                                final String tableNameAndColumnName = anTable.name() + "." + columnName;
                                resultList.add(SimpleFieldTableDto.of("t_" + fieldName, tableNameAndColumnName));
                            }
                            resultList.add(SimpleFieldTableDto.of(fieldName, columnName));
                        }
                );
        return resultList;
    }

    private boolean isNotIgnoreField(Element element) {
        return element.getAnnotation(IgnoreField.class) == null;
    }

    public boolean isField(Element element) {
        return element != null && element.getKind().isField();
    }

    public static String getPackage(TypeMirror typeMirror) {
        final String[] split = typeMirror.toString().split("\\.");
        return String.join(".", Arrays.copyOf(split, split.length - 1));
    }

}
