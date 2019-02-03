package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import br.arthur.accessibilitylint.model.BorderSpace;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;


public class BorderComponentDetector extends ResourceXmlDetector {
    private static final String DESCRIPTION = "The spacing, of attribute %s, between the clickable element and the edges of the screen should be at least 8dp";
    private static final String GENERIC_DESCRIPTION = "The spacing between the clickable element and the edges of the screen should be at least 8dp";

    static final Issue ISSUE_BORDER_COMPONENT_DETECTOR = Issue.create(
            "BorderComponentIssue",
            GENERIC_DESCRIPTION,
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(BorderComponentDetector.class, RESOURCE_FILE_SCOPE));


    private static final int MIN_MARGIN = 8;

    private List<Element> lstElementsVisited;
    private Map<String, Node> report;

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(SdkConstants.ATTR_ON_CLICK, SdkConstants.ATTR_CLICKABLE);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(SdkConstants.BUTTON,
                SdkConstants.IMAGE_BUTTON,
                SdkConstants.IMAGE_VIEW);
    }

    @Override
    public void beforeCheckFile(@NotNull Context context) {
        lstElementsVisited = new ArrayList<>();
        report = new HashMap<>();
    }

    @Override
    public void afterCheckFile(@NotNull Context context) {
        lstElementsVisited = null;
        report = null;
    }

    @Override
    public void visitElement(@NotNull XmlContext context, @NotNull Element element) {
        if (!lstElementsVisited.contains(element)) {
            checkBorderSpace(context, element);
            lstElementsVisited.add(element);
        }
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        if (ATTR_CLICKABLE.equals(attribute.getLocalName()) && "false".equals(attribute.getValue()))
            return;

        Element element = attribute.getOwnerElement();
        if (!lstElementsVisited.contains(element)) {
            checkBorderSpace(context, element);
            lstElementsVisited.add(element);
        }
    }

    private void checkBorderSpace(XmlContext context, Element element) {
        BorderSpace borderSpace = new BorderSpace(element);

        checkIssue(borderSpace.getBorderSpaceTop(), element, ATTR_LAYOUT_MARGIN_TOP);
        checkIssue(borderSpace.getBorderSpaceBottom(), element, ATTR_LAYOUT_MARGIN_BOTTOM);
        checkIssue(borderSpace.getBorderSpaceStart(), element, ATTR_LAYOUT_MARGIN_START);
        checkIssue(borderSpace.getBorderSpaceEnd(), element, ATTR_LAYOUT_MARGIN_END);

        reportIssuesFounded(context);
    }

    private void reportIssuesFounded(XmlContext context) {
        Set<String> keySet = report.keySet();
        for (String description : keySet) {
            Node node = report.get(description);

            context.report(ISSUE_BORDER_COMPONENT_DETECTOR,
                    node,
                    context.getLocation(node),
                    description);
        }

        report.clear();
    }

    private void checkIssue(int value, Element element, String attrLocalName) {
        if (value < MIN_MARGIN) {
            Attr attr = element.getAttributeNodeNS(ANDROID_URI, attrLocalName);

            if (attr == null) {
                attr = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN);

                if (attr == null) {
                    report.put(GENERIC_DESCRIPTION, element);
                    return;
                }
            }

            String description = String.format(Locale.US, DESCRIPTION, attr.getName());
            report.put(description, attr);
        }
    }
}
