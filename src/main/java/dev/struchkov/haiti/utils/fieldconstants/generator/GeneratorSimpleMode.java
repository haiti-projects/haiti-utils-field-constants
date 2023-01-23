package dev.struchkov.haiti.utils.fieldconstants.generator;

import dev.struchkov.haiti.utils.fieldconstants.creator.CreatorClassSimpleMode;
import dev.struchkov.haiti.utils.fieldconstants.domain.Mode;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple.ClassSimpleDto;
import dev.struchkov.haiti.utils.fieldconstants.domain.mode.simple.SimpleFieldDto;
import dev.struchkov.haiti.utils.fieldconstants.util.Generator;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public class GeneratorSimpleMode {

    public static void generate(ProcessingEnvironment processingEnv, Element annotatedElement) {
        final String annotatedElementName = annotatedElement.getSimpleName().toString();

        final String newClassName = annotatedElementName + Mode.SIMPLE.getDefaultPostfix();

        final List<? extends Element> allFields = annotatedElement.getEnclosedElements().stream()
                .filter(Generator::isField)
                .filter(Generator::isNotIgnoreField)
                .toList();

        final List<SimpleFieldDto> simpleFields = getSimpleFields(allFields);

        final ClassSimpleDto newClass = new ClassSimpleDto();
        newClass.setClassName(newClassName);
        newClass.setSimpleFields(simpleFields);
        newClass.setClassPackage(Generator.getPackage(annotatedElement));

        CreatorClassSimpleMode.record(newClass, processingEnv);
    }

    private static List<SimpleFieldDto> getSimpleFields(List<? extends Element> allFields) {
        final List<SimpleFieldDto> resultList = new ArrayList<>();
        for (Element field : allFields) {
            final String fieldName = field.getSimpleName().toString();
            resultList.add(SimpleFieldDto.of(fieldName));
        }
        return resultList;
    }

}