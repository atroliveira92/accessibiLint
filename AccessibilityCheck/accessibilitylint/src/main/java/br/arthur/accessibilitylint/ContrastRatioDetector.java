package br.arthur.accessibilitylint;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import br.arthur.accessibilitylint.utils.ContrastCalculatorUtils;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_BACKGROUND;
import static com.android.SdkConstants.ATTR_TEXT_COLOR;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 16/01/2019.
 *
 */

public class ContrastRatioDetector extends ResourceXmlDetector {

    private static final double MIN_CONTRAST_RATIO = 4.5;

    public static final Issue ISSUE_COLOR_CONTRAST_RATIO = Issue.create(
            "ColorContrastRatio",
            "Color contrast ratio bad accessibility",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(ContrastRatioDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }


    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(ATTR_TEXT_COLOR, ATTR_BACKGROUND);
    }

    private Attr searchForParentBackgroundAttribute(Node element) {
        Node parentNode = element.getParentNode();
        if(parentNode == null || parentNode.getNodeType() != Node.ELEMENT_NODE)
            return null;
        else {
            Element elementParent = (Element) parentNode;
            Attr attrBackgroundColor = elementParent.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
            if(attrBackgroundColor != null)
                return attrBackgroundColor;
        }
        return searchForParentBackgroundAttribute(parentNode);
    }


    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {

        //Treat text color attribute first
        if(attribute.getName().equals("android:"+ATTR_TEXT_COLOR)) {
            Element element = attribute.getOwnerElement();
            Attr attrBackgroundColor = element.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
            if(attrBackgroundColor == null) {
//                Node parentNode = element.getParentNode();
//                if(parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
//                    Element elementParent = (Element) parentNode;
                    //attrBackgroundColor = elementParent.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
                attrBackgroundColor = searchForParentBackgroundAttribute(element);
                if(attrBackgroundColor != null){
                    reportIssue(element, attrBackgroundColor, attribute,
                            "Text color and parent background color contrast ratio is ", context);
                }
                //}
            } else {
                reportIssue(element, attrBackgroundColor, attribute,
                        "Text color and background color contrast ratio is ", context);
            }

        } else if(attribute.getName().equals("android:"+ATTR_BACKGROUND)){
            Element element = attribute.getOwnerElement();
            Node parentNode = element.getParentNode();
            if(parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elementParent = (Element) parentNode;
                Attr attrBackground = elementParent.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
                if (attrBackground != null) {
                    reportIssue(element, attrBackground, attribute,
                            "The contrast ratio between the background and parent background is ", context);
                }
            }
        }
//        Element element = attribute.getOwnerElement();
//        Attr attrTextColor = element.getAttributeNodeNS(ANDROID_URI, ATTR_TEXT_COLOR);
//        if(attrTextColor != null) {
//            String textColor = attrTextColor.getValue();
//            String backgroundColor = attribute.getValue();
//
//            if(textColor.startsWith("#") && backgroundColor.startsWith("#")) {
//                double contrastRatio  = ContrastCalculatorUtils.getConstrastRatio(textColor, backgroundColor);
//
//                if(contrastRatio < MIN_CONTRAST_RATIO) {
//                    context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
//                            context.getLocation(element),
//                            "Text color and background color contrast ratio is " +
//                                    new DecimalFormat("##.##").format(contrastRatio) +
//                                    " the minimum is "+MIN_CONTRAST_RATIO);
//                }
//            }
//        } else {
//            Node parentNode = element.getParentNode();
//            if(parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element elementParent = (Element) parentNode;
//                Attr attrBackground = elementParent.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
//                if(attrBackground != null) {
//                    String textColor = attrBackground.getValue();
//                    String backgroundColor = attribute.getValue();
//
//                    if(textColor.startsWith("#") && backgroundColor.startsWith("#")) {
//                        double contrastRatio = ContrastCalculatorUtils.getConstrastRatio(textColor, backgroundColor);
//
//                        if (contrastRatio < MIN_CONTRAST_RATIO) {
//                            context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
//                                    context.getLocation(element),
//                                    "The contrast ratio between the background and parent background is "+
//                                            new DecimalFormat("##.##").format(contrastRatio) +
//                                            " the minimum is "+ MIN_CONTRAST_RATIO);
//                        }
//                    }
//                }
//            }
//        }
    }

    private void reportIssue(Element element, Attr attrParent, Attr attrChild, String prefixMessage, XmlContext context) {
        if(element == null)
            return;
        if(attrParent == null)
            return;
        if(attrChild == null)
            return;
        if(TextUtils.isEmpty(prefixMessage))
            return;

        String childColor = attrChild.getValue();
        String parentColor = attrParent.getValue();

        if(childColor.startsWith("#") && parentColor.startsWith("#")) {
            double contrastRatio = ContrastCalculatorUtils.getConstrastRatio(childColor, parentColor);

            if (contrastRatio < MIN_CONTRAST_RATIO) {
                context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
                        context.getLocation(element),
                        prefixMessage +
                                new DecimalFormat("##.##").format(contrastRatio) +
                                " the minimum is "+ MIN_CONTRAST_RATIO);
            }
        }
    }

    //    @Override
//    public void visitElement(XmlContext context, Element element) {
//        super.visitElement(context, element);
//
//        Attr attrBackground = element.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
//        if (attrBackground != null) {
//            if(element.hasChildNodes()) {
//                boolean hasAttribute = false;
//                for (Element child : LintUtils.getChildren(element)) {
//                    Attr attrOnClick = child.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
//                    if(attrOnClick != null) {
//                        hasAttribute = true;
//                        break;
//                    }
//                }
//                if(hasAttribute) {
//                    context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
//                            context.getLocation(element),
//                            "is the same color as background");
//                }
//            } else {
//                Attr attrTextColor = element.getAttributeNodeNS(ANDROID_URI, ATTR_TEXT_COLOR);
//                if(attrTextColor != null) {
//                    String textColor = attrTextColor.getValue();
//                    String backgroundColor = attrBackground.getValue();
//
//                    if(ContrastCalculatorUtils.isContrastInValid(textColor, backgroundColor, 4.5)) {
//                        context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
//                                context.getLocation(element),
//                                "Text color "+textColor + " is not in contrast with background "+backgroundColor);
//                    }
//                }
//            }
//        }
//    }

}
