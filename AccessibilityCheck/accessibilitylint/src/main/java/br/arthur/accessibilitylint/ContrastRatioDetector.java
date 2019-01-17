package br.arthur.accessibilitylint;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.LintUtils;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

import br.arthur.accessibilitylint.utils.ContrastCalculatorUtils;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_BACKGROUND;
import static com.android.SdkConstants.ATTR_BOX_BACKGROUND_COLOR;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.ATTR_TEXT_COLOR;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.TEXT_VIEW;
import static com.android.SdkConstants.VIEW;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 16/01/2019.
 *
 */

public class ContrastRatioDetector extends ResourceXmlDetector {



    public static final Issue ISSUE_COLOR_CONTRAST_RATIO = Issue.create(
            "TextViewColorContrastRatio",
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
    public Collection<String> getApplicableElements() {
        return Collections.singleton(TEXT_VIEW);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        super.visitElement(context, element);

        Attr attrBackground = element.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
        if (attrBackground != null) {
            if(element.hasChildNodes()) {
                boolean hasAttribute = false;
                for (Element child : LintUtils.getChildren(element)) {
                    Attr attrOnClick = child.getAttributeNodeNS(ANDROID_URI, ATTR_BACKGROUND);
                    if(attrOnClick != null) {
                        hasAttribute = true;
                        break;
                    }
                }
                if(hasAttribute) {
                    context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
                            context.getLocation(element),
                            "is the same color as background");
                }
            } else {
                Attr attrTextColor = element.getAttributeNodeNS(ANDROID_URI, ATTR_TEXT_COLOR);
                if(attrTextColor != null) {
                    String textColor = attrTextColor.getValue();
                    String backgroundColor = attrBackground.getValue();

                    if(ContrastCalculatorUtils.isContrastInValid(textColor, backgroundColor, 4.5)) {
                        context.report(ISSUE_COLOR_CONTRAST_RATIO, element,
                                context.getLocation(element),
                                "Text color "+textColor + " is not in contrast with background "+backgroundColor);
                    }
                }
            }
        }
    }

}
