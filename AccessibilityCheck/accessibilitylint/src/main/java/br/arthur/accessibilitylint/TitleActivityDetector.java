package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.Collection;
import java.util.Collections;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_LABEL;
import static com.android.SdkConstants.TAG_APPLICATION;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.MANIFEST_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 25/01/2019.
 */

public class TitleActivityDetector extends Detector implements Detector.XmlScanner {


    public static final Issue ISSUE_TITLE_ACTIVITY = Issue.create(
            "TitleActivityIssue",
            "Provide the android:label attribute",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(TitleActivityDetector.class, MANIFEST_SCOPE));


    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singleton(SdkConstants.TAG_ACTIVITY);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        Node parentNode = element.getParentNode();
        if(TAG_APPLICATION.equals(parentNode.getNodeName())) {
            Attr attribute = element.getAttributeNodeNS(ANDROID_URI, ATTR_LABEL);
            if (attribute == null) {
                context.report(ISSUE_TITLE_ACTIVITY, element, context.getLocation(element), "Provide the android:label attribute");
            }
        }
    }
}
