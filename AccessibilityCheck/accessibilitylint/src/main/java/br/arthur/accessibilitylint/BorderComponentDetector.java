package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.TextFormat;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;
import static com.android.tools.lint.detector.api.Severity.max;

/**
 * Created by arthu on 28/01/2019.
 */

public class BorderComponentDetector extends ResourceXmlDetector {

    private int MIN_MARGIN = 8;

    static final Issue ISSUE_BORDER_COMPONENT_DETECTOR = Issue.create(
            "BorderComponentIssue",
            "It seams this possible clicable object is to close to other object or the screen border, provide at least 8 dp margin",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(BorderComponentDetector.class, RESOURCE_FILE_SCOPE));

    private List<Element> lstElementsVisited;

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
                SdkConstants.IMAGE_VIEW,
                SdkConstants.TEXT_VIEW);
    }

    @Override
    public void beforeCheckFile(Context context) {
        lstElementsVisited = new ArrayList<>();
    }

    @Override
    public void afterCheckFile(Context context) {
        lstElementsVisited = null;
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        if(!lstElementsVisited.contains(element)) {
            checkMargin(context, element);
            lstElementsVisited.add(element);
        }
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        Element element = attribute.getOwnerElement();
        if(!lstElementsVisited.contains(element)) {
            checkMargin(context, element);
            lstElementsVisited.add(element);
        }
    }

    private void checkMargin(XmlContext context, Element element) {
        Attr attrMargin = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN);
        if(attrMargin != null) {
            //Check the component margin
            String value = attrMargin.getValue();
            int margin = 0;

            if (!value.startsWith("@") && value.endsWith("dp")) {
                int index = value.indexOf("d");
                value = value.substring(0, index);
                margin = Integer.parseInt(value);

                int parentMargin = getParentMarginOrPadding(element, margin);
                if(parentMargin < MIN_MARGIN) {
                    context.report(ISSUE_BORDER_COMPONENT_DETECTOR,
                            element,
                            context.getValueLocation(attrMargin),
                            ISSUE_BORDER_COMPONENT_DETECTOR.getBriefDescription(TextFormat.RAW));
                }
            }
        } else {

            int value = getParentMarginOrPadding(element, 0);
            if (value < MIN_MARGIN) {
                context.report(ISSUE_BORDER_COMPONENT_DETECTOR,
                        element,
                        context.getLocation(element),
                        ISSUE_BORDER_COMPONENT_DETECTOR.getBriefDescription(TextFormat.RAW));
            }
        }
    }

    private int getParentMarginOrPadding(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;
        Attr attrMargin = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN);
        int vMargin = 0;
        int vPadding = 0;
        if(attrMargin != null) {
             String value = attrMargin.getValue();

             if(!value.startsWith("@") && value.endsWith("dp")) {
                 int index = value.indexOf("d");
                 value = value.substring(0, index);
                 vMargin = Integer.parseInt(value);
             }
        }

        Attr attrPadding = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING);
        if(attrPadding != null) {
            String value = attrPadding.getValue();

            if(!value.startsWith("@") && value.endsWith("dp")) {
                int index = value.indexOf("d");
                value = value.substring(0, index);
                vPadding = Integer.parseInt(value);
            }
        }

        return getParentMarginOrPadding(parentElement,  marginValue + vMargin + vPadding);
    }
}
