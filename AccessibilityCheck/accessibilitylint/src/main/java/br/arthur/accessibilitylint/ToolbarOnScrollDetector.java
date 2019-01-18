package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
/**
 * Created by arthu on 17/01/2019.
 *
 */

public class ToolbarOnScrollDetector extends ResourceXmlDetector {

    private static final String TOOLBAR = "android.support.v7.widget.Toolbar";
    private static final String APPBARLAYOUT = "android.support.design.widget.AppBarLayout";

    public static final Issue ISSUE_TOOLBAR_SCROLL_VIEW = Issue.create(
            "ToolbarIssueScrollView",
            "Color contrast ratio bad accessibility",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(ToolbarOnScrollDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(TOOLBAR);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        if(searchForScrollView(element)) {
            context.report(ISSUE_TOOLBAR_SCROLL_VIEW, element, context.getLocation(element), "Avoid use toolbar wrap in scrollView");
        }
    }

    private boolean searchForScrollView(Node element) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return false;
        else if(parentNode.getNodeName().contains("ScrollView")) {
            return true;
        }
        return searchForScrollView(parentNode);
    }
}
