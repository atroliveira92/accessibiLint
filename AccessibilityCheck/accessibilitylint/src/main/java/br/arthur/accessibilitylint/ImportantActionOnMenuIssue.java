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

import static com.android.SdkConstants.ATTR_SHOW_AS_ACTION;
import static com.android.SdkConstants.ATTR_TITLE;
import static com.android.resources.ResourceFolderType.MENU;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class ImportantActionOnMenuIssue extends ResourceXmlDetector {

    public static final Issue ISSUE_IMPORTANT_ACTION_ON_MENU = Issue.create(
            "TestActionOnMenu",
            "Important action must be on the corner and easy accessible",
            "According to Siebra et al. (2017), interface components containing important actions must be located near the corners of the screen and easily accessible to the visually impaired user, without entering more than two touches to access",
            A11Y,
            4,
            WARNING,
            new Implementation(
                    ImportantActionOnMenuIssue.class,
                    RESOURCE_FILE_SCOPE));

    private String action;
    private String title;

    @Override
    public boolean appliesTo(@NonNull final ResourceFolderType folderType) {
        return EnumSet.of(MENU).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(ATTR_SHOW_AS_ACTION);
        arrayList.add(ATTR_TITLE);
        return arrayList;
    }

    @Override
    public void visitAttribute(@NonNull final XmlContext context, @NonNull final Attr attribute) {
        if (attribute.getName().equals("app:" + ATTR_SHOW_AS_ACTION))
            action = LintUtils.stripIdPrefix(attribute.getValue());
        else {
            title = LintUtils.stripIdPrefix(attribute.getValue());
        }

        if (("Save".equalsIgnoreCase(title) || "Delete".equalsIgnoreCase(title))
                && "never".equalsIgnoreCase(action)) {
            context.report(ISSUE_IMPORTANT_ACTION_ON_MENU, attribute, context.getValueLocation(attribute), "Avoid use app:showAsAction=\"never\" when actions such as Save or Delete");
        }
    }
}

