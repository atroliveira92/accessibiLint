package br.arthur.accessibilitylint.all;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.checks.TooManyViewsDetector;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import br.arthur.accessibilitylint.text.TextDetectorRule;

import static com.android.resources.ResourceFolderType.LAYOUT;

/**
 * Created by arthu on 02/02/2019.
 */

public class AllLayoutDetector extends ResourceXmlDetector {

    private List<AllLayoutDetectorRule> rules;
    public AllLayoutDetector() {
        rules = Arrays.asList(new TooManyInteractableViewsDetector(), new ViewGroupInteractionDetector());
    }


    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }


    @Override
    public Collection<String> getApplicableElements() {
        return ALL;
    }

    @Override
    public void visitElement(@NotNull XmlContext context, @NotNull Element element) {
        for (AllLayoutDetectorRule r : rules)
            r.visitElement(context, element);
    }
}
