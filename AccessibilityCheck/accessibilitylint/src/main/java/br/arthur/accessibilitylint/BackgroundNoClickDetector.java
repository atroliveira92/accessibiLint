package br.arthur.accessibilitylint;

import com.android.ddmlib.Log;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.FRAME_LAYOUT;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.RELATIVE_LAYOUT;
import static com.android.SdkConstants.VIEW;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 14/01/2019.
 *
 */

public class BackgroundNoClickDetector extends ResourceXmlDetector {

    public static final Issue ISSUE_NO_CLICK_ON_BACKGROUND_VIEW = Issue.create(
            "NoClickBackgroundView",
             "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            3,
            WARNING,
            new Implementation(BackgroundNoClickDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(LINEAR_LAYOUT);
        arrayList.add(RELATIVE_LAYOUT);
        arrayList.add(FRAME_LAYOUT);
        arrayList.add(VIEW);
        return arrayList;
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        List<String> arrayList = new ArrayList<>();
        arrayList.add(ATTR_ON_CLICK);

        return arrayList;
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        super.visitAttribute(context, attribute);

        Element element = attribute.getOwnerElement();
        Node nodeError = null;
        if(element.hasChildNodes()) {
            NodeList nodeList = element.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if(node.hasAttributes() && ((ElementNSImpl)node).hasAttribute("android:onClick")) {
                    nodeError = node;
                    break;
                }
            }
        }
        if(nodeError != null) {
            context.report(ISSUE_NO_CLICK_ON_BACKGROUND_VIEW, nodeError,
                    context.getValueLocation(element.getAttributeNode("android:onClick")),
                    "Avoid make the background view clickable");
        }
    }

//    @Override
//    public void visitElement(XmlContext context, Element element) {
//        super.visitElement(context, element);
//        NamedNodeMap namedNodeMap = element.getAttributes();
//        boolean hasxmlsAndroid = false;
//        boolean hasOnClick = false;
//        Node xmlsnINode = null;
//        for(int i = 0; i < namedNodeMap.getLength(); i++) {
//            Node node = namedNodeMap.item(i);
//            if(node.getNodeName().equals("xmlns:android")) {
//                xmlsnINode = node;
//                hasxmlsAndroid = true;
//            }
//            if(node.getNodeName().equals("android:onClick")) {
//                hasOnClick = true;
//            }
//        }
//
//        if(hasxmlsAndroid && hasOnClick) {
//            context.report(ISSUE_NO_CLICK_ON_BACKGROUND_VIEW, xmlsnINode,
//                    context.getValueLocation(element.getAttributeNode(xmlsnINode.getNodeName())),
//                    "Avoid make the background view clickable");
//        }
//    }
}