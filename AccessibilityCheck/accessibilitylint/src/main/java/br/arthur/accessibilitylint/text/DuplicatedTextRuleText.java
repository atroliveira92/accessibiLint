package br.arthur.accessibilitylint.text;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class DuplicatedTextRuleText implements TextDetectorRule {
    public static final Issue ISSUE_DUPLICATE_TEXTS_IN_LAYOUT = Issue.create(
            "DuplicateTextInLayout",
            "This view has duplicate texts on text and contentDescription components",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(TextDetector.class, RESOURCE_FILE_SCOPE));

    private final List<String> texts;
    private final List<String> contentDescriptions;

    DuplicatedTextRuleText() {
        this.texts = new ArrayList<>();
        this.contentDescriptions = new ArrayList<>();
    }

    @Override
    public void beforeCheckFile(@NotNull Context context) {
        //Do nothing
    }

    @Override
    public void afterCheckFile(@NotNull Context context) {
        texts.clear();
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        String value = attribute.getValue();
        if(attribute.getLocalName().equals(ATTR_TEXT)) {
            Element element = attribute.getOwnerElement();
            Attr attrContentDescription = element.getAttributeNodeNS(ANDROID_URI, ATTR_CONTENT_DESCRIPTION);
            String contentDescriptionValue = attrContentDescription != null ? attrContentDescription.getValue() : null;
            if(contentDescriptionValue == null || contentDescriptionValue.equals(value)) {
                if (texts.contains(value)) {
                    context.report(ISSUE_DUPLICATE_TEXTS_IN_LAYOUT,
                            attribute,
                            context.getValueLocation(attribute),
                            "Avoid use same texts on layout. If need, provide a diferent contentDescription to differ the element");
                } else {
                    texts.add(value);
                }
            }
        } else if(attribute.getLocalName().equals(ATTR_CONTENT_DESCRIPTION)) {
            if(contentDescriptions.contains(value)) {
                context.report(ISSUE_DUPLICATE_TEXTS_IN_LAYOUT,
                        attribute,
                        context.getValueLocation(attribute),
                        "Avoid use same contentDescription on layout. It might confuse blind users");
            } else {
                contentDescriptions.add(value);
            }
        }
    }
}
