package br.arthur.accessibilitylint;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class LinkMissingExplanationDetector extends ResourceXmlDetector {
    private static final String EXPLANATION_MESSAGE = "LALO According to Siebra et al. (2017) and Gomes (2018)," +
            " when there are inappropriately labeled links, such as example, " +
            "\"click here\", \"access here\" ou \"link\", a visual impairment user may not be able" +
            " to understand the meaning of these badly labeled links without accessing" +
            " all of the releated information around them. Therefore, please provide more information if you are creating links with TextView";

    static final Issue LINK_WITHOUT_RIGHT_EXPLANATION = Issue.create(
            "ImportantActionOnMenu",
            "Links improperly labeled",
            EXPLANATION_MESSAGE,
            A11Y,
            4,
            WARNING,
            new Implementation(
                    LinkMissingExplanationDetector.class,
                    RESOURCE_FILE_SCOPE));

    private String text;
    private String onClick;

    @Override
    public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(ATTR_TEXT);
        arrayList.add(ATTR_ON_CLICK);
        arrayList.add(ATTR_CLICKABLE);
        return arrayList;
    }

    @Override
    public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
        if (attribute.getName().equals("android:" + ATTR_TEXT))
            text = LintUtils.stripIdPrefix(attribute.getValue());
        else if(attribute.getName().equals("android:"+ATTR_ON_CLICK) ||
                (attribute.getName().equals("android:"+ATTR_CLICKABLE) && "true".equalsIgnoreCase(attribute.getValue()))){
            onClick = LintUtils.stripIdPrefix(attribute.getValue());
        }

        if (text != null && onClick != null &&
                ("click here".equalsIgnoreCase(text) || "link".equalsIgnoreCase(text)) || "access here".equalsIgnoreCase(text)
                && !onClick.isEmpty()) {
            context.report(LINK_WITHOUT_RIGHT_EXPLANATION, attribute,
                    context.getValueLocation(attribute),
                    "If you are crating a link, avoid use vague information. Please, provide more information to the user");
        }
    }
}

