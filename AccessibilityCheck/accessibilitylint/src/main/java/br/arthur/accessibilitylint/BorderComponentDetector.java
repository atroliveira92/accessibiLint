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
import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_END;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;
import static com.android.SdkConstants.ATTR_PADDING_START;
import static com.android.SdkConstants.ATTR_PADDING_TOP;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;


public class BorderComponentDetector extends ResourceXmlDetector {

    private static final int MIN_MARGIN = 8;

    static final Issue ISSUE_BORDER_COMPONENT_DETECTOR = Issue.create(
            "BorderComponentIssue",
            "It seams this possible clicable object is to close to other object or the screen border, provide at least 8dp margin",
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
                SdkConstants.IMAGE_VIEW);
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
        if(attribute.getLocalName().equals(ATTR_CLICKABLE) && attribute.getValue().equals("false"))
            return;

        Element element = attribute.getOwnerElement();
        if(!lstElementsVisited.contains(element)) {
            checkMargin(context, element);
            lstElementsVisited.add(element);
        }
    }

    private void checkMargin(XmlContext context, Element element) {
        Attr attrMargin = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN);

        Attr attrMarginLeft = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_LEFT);
        if(attrMarginLeft == null)
            attrMarginLeft = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_START);

        Attr attrMarginRight = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_RIGHT);
        if(attrMarginRight == null)
            attrMarginRight = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_END);

        Attr attrMarginTop = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_TOP);
        Attr attrMarginBottom = element.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_BOTTOM);

        int parentMargin = getParentMarginOrPadding(element, 0);
        int parentMarginTop = getParentMarginOrPaddingTop(element, 0);
        int parentMarginLeft = getParentMarginOrPaddingLeft(element, 0);
        int parentMarginRight = getParentMarginOrPaddingRight(element, 0);
        int parentMarginBottom = getParentMarginOrPaddingBottom(element, 0);

        boolean childMarginFound=false;

        if(attrMargin != null) {
            int margin = loadValueFromAttribute(attrMargin);
            checkAndReport(margin + parentMargin + parentMarginTop + parentMarginRight
                    + parentMarginBottom + parentMarginLeft, context, attrMargin);

            childMarginFound = true;
        }
        if(attrMarginLeft != null){
            int margin = loadValueFromAttribute(attrMarginLeft);
            checkAndReport(margin + parentMargin + parentMarginLeft, context, attrMarginLeft);

            childMarginFound = true;
        }
        if(attrMarginRight != null) {
            int margin = loadValueFromAttribute(attrMarginRight);
            checkAndReport(margin + parentMargin + parentMarginRight, context, attrMarginRight);

            childMarginFound = true;
        }
        if(attrMarginTop != null) {
            int margin = loadValueFromAttribute(attrMarginTop);
            checkAndReport(margin + parentMargin + parentMarginTop, context, attrMarginTop);

            childMarginFound = true;
        }
        if(attrMarginBottom != null) {
            int margin = loadValueFromAttribute(attrMarginBottom);
            checkAndReport(margin + parentMargin + parentMarginBottom, context, attrMarginBottom);

            childMarginFound = true;
        }

        if(!childMarginFound)
            checkAndReport(parentMargin, context, element);
    }

    private void checkAndReport(int value, XmlContext context, Node element) {
        if (value < MIN_MARGIN) {
            context.report(ISSUE_BORDER_COMPONENT_DETECTOR,
                    element,
                    context.getLocation(element),
                    ISSUE_BORDER_COMPONENT_DETECTOR.getBriefDescription(TextFormat.RAW));
        }
    }

    private int getParentMarginOrPadding(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;

        Attr attrMargin = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN);
        int vMargin = loadValueFromAttribute(attrMargin);

        Attr attrPadding = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING);
        int vPadding = loadValueFromAttribute(attrPadding);

        return getParentMarginOrPadding(parentElement,  marginValue + vMargin + vPadding);
    }

    private int getParentMarginOrPaddingLeft(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;

        Attr attrMarginLeft = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_LEFT);
        if(attrMarginLeft == null)
            attrMarginLeft = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_START);

        int vMargin = loadValueFromAttribute(attrMarginLeft);

        Attr attrPaddingLeft = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_LEFT);
        if(attrPaddingLeft == null)
            attrPaddingLeft = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_START);

        int vPadding = loadValueFromAttribute(attrPaddingLeft);

        return getParentMarginOrPaddingLeft(parentElement,  marginValue + vMargin + vPadding);
    }

    private int getParentMarginOrPaddingRight(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;

        Attr attrMarginRight = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_RIGHT);
        if(attrMarginRight == null)
            attrMarginRight = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_END);

        int vMargin = loadValueFromAttribute(attrMarginRight);

        Attr attrPaddingRight = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_RIGHT);
        if(attrPaddingRight == null)
            attrPaddingRight = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_END);

        int vPadding = loadValueFromAttribute(attrPaddingRight);

        return getParentMarginOrPaddingRight(parentElement,  marginValue + vMargin + vPadding);
    }

    private int getParentMarginOrPaddingTop(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;

        Attr attrMarginTop = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_TOP);
        int vMargin = loadValueFromAttribute(attrMarginTop);

        Attr attrPaddingTop = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_TOP);
        int vPadding = loadValueFromAttribute(attrPaddingTop);

        return getParentMarginOrPaddingTop(parentElement,  marginValue + vMargin + vPadding);
    }

    private int getParentMarginOrPaddingBottom(Element element, int marginValue) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return marginValue;

        Element parentElement = (Element) parentNode;

        Attr attrMarginBottom = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_LAYOUT_MARGIN_BOTTOM);
        int vMargin = loadValueFromAttribute(attrMarginBottom);

        Attr attrPaddingBottom = parentElement.getAttributeNodeNS(ANDROID_URI, ATTR_PADDING_BOTTOM);
        int vPadding = loadValueFromAttribute(attrPaddingBottom);

        return getParentMarginOrPaddingBottom(parentElement,  marginValue + vMargin + vPadding);
    }

    private int loadValueFromAttribute(Attr attribute) {
        if(attribute != null) {
            String value = attribute.getValue();

            if(!value.startsWith("@") && value.endsWith("dp")) {
                int index = value.indexOf("d");
                value = value.substring(0, index);
                return Integer.parseInt(value);
            }
        }
        return 0;
    }
}
