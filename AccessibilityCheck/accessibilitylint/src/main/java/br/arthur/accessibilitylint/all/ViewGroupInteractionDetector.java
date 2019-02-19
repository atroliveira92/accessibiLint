package br.arthur.accessibilitylint.all;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.TextFormat;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.SdkConstants.ATTR_FOCUSABLE;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.ATTR_TEXT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static org.w3c.dom.Node.ELEMENT_NODE;


public class ViewGroupInteractionDetector implements AllLayoutDetectorRule {

    public static final Issue ISSUE_VIEWGROUP_CLICKABLE =  Issue.create(
            "ViewGroupClickableIssue",
            "It seams this clickable view has no child with text. Please provide at least a contentDescription for this view",
            "Using too many views in a single layout is bad for "
                    + "performance. Consider using compound drawables or other tricks for "
                    + "reducing the number of views in this layout.\n"
                    + "\n"
                    + "The maximum view count defaults to 10 but can be configured with the "
                    + "environment variable `ANDROID_LINT_MAX_VIEW_COUNT`.",
            A11Y,
            6,
            WARNING,
            new Implementation(AllLayoutDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public void visitElement(XmlContext context, Element element) {
        if(element.hasChildNodes()) {
            Attr attrOnClick = element.getAttributeNodeNS(ANDROID_URI, ATTR_ON_CLICK);
            Attr attrClickable = element.getAttributeNodeNS(ANDROID_URI, ATTR_CLICKABLE);
            Attr attrFocusable = element.getAttributeNodeNS(ANDROID_URI, ATTR_FOCUSABLE);

            if(attrOnClick != null || (attrClickable != null && "true".equals(attrClickable.getValue()))
                    || (attrFocusable != null && "true".equals(attrFocusable.getValue()))) {

                boolean hasChildWithText = false;
                NodeList nodeList = element.getChildNodes();
                for(int i = 0; i < nodeList.getLength(); i++) {
                    Node node = nodeList.item(i);
                    if(node.getNodeType() == ELEMENT_NODE) {
                        Element childElement = (Element) node;
                        Attr attrContentDescription = childElement.getAttributeNodeNS(ANDROID_URI, ATTR_CONTENT_DESCRIPTION);
                        if(attrContentDescription != null) {
                            hasChildWithText = true;
                            break;
                        } else {
                            Attr attrText = childElement.getAttributeNodeNS(ANDROID_URI, ATTR_TEXT);
                            if(attrText != null) {
                                hasChildWithText = true;
                                break;
                            }
                        }
                    }
                }

                Attr attrContentDescription = element.getAttributeNodeNS(ANDROID_URI,ATTR_CONTENT_DESCRIPTION);
                if(!hasChildWithText && attrContentDescription == null) {
                    context.report(ISSUE_VIEWGROUP_CLICKABLE, element, context.getLocation(element), ISSUE_VIEWGROUP_CLICKABLE.getBriefDescription(TextFormat.RAW));
                }
            }
        }
    }
}
