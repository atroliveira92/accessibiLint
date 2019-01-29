package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 26/01/2019.
 *
 */

public class DuplicateTextsInLayoutDetector extends ResourceXmlDetector {

    public static final Issue ISSUE_DUPLICATE_TEXTS_IN_LAYOUT = Issue.create(
            "DuplicateTextInLayout",
            "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(DuplicateTextsInLayoutDetector.class, RESOURCE_FILE_SCOPE));

    private List<String> titles;

    @Override
    public void beforeCheckFile(Context context) {
        titles = new ArrayList<>();
    }

    @Override
    public void afterCheckFile(Context context) {
        titles = null;
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }


    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(SdkConstants.ATTR_CONTENT_DESCRIPTION, SdkConstants.ATTR_TITLE);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        String value = attribute.getValue();
        if(titles.contains(value)) {
            context.report(ISSUE_DUPLICATE_TEXTS_IN_LAYOUT, attribute, context.getValueLocation(attribute), "Avoid use same texts on layout. If need, provide a diferent contentDescription to differ the element");
        } else {
            titles.add(value);
        }
    }
}
