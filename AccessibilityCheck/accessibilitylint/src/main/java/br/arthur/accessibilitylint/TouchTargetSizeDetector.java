package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_LAYOUT_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MIN_HEIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MIN_WIDTH;
import static com.android.SdkConstants.ATTR_LAYOUT_WIDTH;
import static com.android.SdkConstants.ATTR_MIN_HEIGHT;
import static com.android.SdkConstants.ATTR_MIN_WIDTH;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 18/01/2019.
 *
 */

public class TouchTargetSizeDetector extends ResourceXmlDetector {

    private static int MIN_VALUE_FOR_TOUCH_DP = 48;

    static final Issue ISSUE_TOUCH_TARGET_VIEW = Issue.create(
            "TouchTargetView",
            "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(TouchTargetSizeDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(ATTR_LAYOUT_WIDTH, ATTR_LAYOUT_HEIGHT);
    }

    @Override
    public void visitAttribute(XmlContext context, Attr attribute) {
        Element element = attribute.getOwnerElement();
        Attr attr = element.getAttributeNodeNS(ANDROID_URI, ATTR_ON_CLICK);

        if(attr == null) {
            attr = element.getAttributeNodeNS(ANDROID_URI, ATTR_CLICKABLE);
            if(attr != null && "false".equalsIgnoreCase(attr.getValue()))
                return;
        }

        if(attr == null || attribute.getValue().startsWith("@")) {
            return;
        }

        if(attribute.getValue().equals(SdkConstants.VALUE_WRAP_CONTENT) ||
                attribute.getValue().equals(SdkConstants.VALUE_MATCH_PARENT)) {

            if(attribute.getName().endsWith(ATTR_LAYOUT_WIDTH)) {
                attr = element.getAttributeNodeNS(ANDROID_URI, ATTR_MIN_WIDTH);
                if(attr != null) {
                    checkAndReportValue(attr, context);
                } else {
                    context.report(ISSUE_TOUCH_TARGET_VIEW, attribute, context.getValueLocation(attribute),
                            "If using wrap_content or match_parent, also implement android:"+ATTR_LAYOUT_MIN_WIDTH+" with at least "+MIN_VALUE_FOR_TOUCH_DP + "dp");
                }
            } else if(attribute.getName().endsWith(ATTR_LAYOUT_HEIGHT)) {
                attr = element.getAttributeNodeNS(ANDROID_URI, ATTR_MIN_HEIGHT);
                if(attr != null) {
                    checkAndReportValue(attr, context);
                } else {
                    context.report(ISSUE_TOUCH_TARGET_VIEW, attribute, context.getValueLocation(attribute),
                            "If using wrap_content or match_parent, also implement android:"+ATTR_LAYOUT_MIN_HEIGHT+" with at least "+MIN_VALUE_FOR_TOUCH_DP+"dp");
                }
            }
        } else if(attribute.getValue().endsWith("dp")) {
            checkAndReportValue(attribute, context);
        }
    }

    private void checkAndReportValue(Attr attribute, XmlContext context) {
        String dpValue = attribute.getValue();
        int index = dpValue.indexOf("d");
        dpValue = dpValue.substring(0, index);

        int value = Integer.parseInt(dpValue);
        if(value < MIN_VALUE_FOR_TOUCH_DP) {
            context.report(ISSUE_TOUCH_TARGET_VIEW, attribute, context.getValueLocation(attribute),
                    "Touch item must have at least 48dp");
        }
    }
}
