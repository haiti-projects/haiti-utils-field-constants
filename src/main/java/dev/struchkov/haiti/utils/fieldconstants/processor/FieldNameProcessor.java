package dev.struchkov.haiti.utils.fieldconstants.processor;

import com.google.auto.service.AutoService;
import dev.struchkov.haiti.utils.fieldconstants.annotation.FieldNames;
import dev.struchkov.haiti.utils.fieldconstants.annotation.setting.TableModeSettings;
import dev.struchkov.haiti.utils.fieldconstants.domain.Mode;
import dev.struchkov.haiti.utils.fieldconstants.generator.GeneratorSimpleMode;
import dev.struchkov.haiti.utils.fieldconstants.generator.GeneratorTableMode;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("dev.struchkov.haiti.utils.fieldconstants.annotation.FieldNames")
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@AutoService(Processor.class)
public class FieldNameProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        for (TypeElement annotation : set) {
            Set<? extends Element> annotatedElements = roundEnvironment.getElementsAnnotatedWith(annotation);
            for (Element annotatedElement : annotatedElements) {
                final FieldNames settings = annotatedElement.getAnnotation(FieldNames.class);

                final Set<Mode> modes = Set.of(settings.mode());
                if (modes.contains(Mode.TABLE)) {
                    final TableModeSettings tableSettings = settings.tableSettings();
                    GeneratorTableMode.generate(processingEnv, tableSettings, annotatedElement);
                }
                if (modes.contains(Mode.SIMPLE)) {
                    GeneratorSimpleMode.generate(processingEnv, annotatedElement);
                }
            }
        }
        return true;
    }


}
