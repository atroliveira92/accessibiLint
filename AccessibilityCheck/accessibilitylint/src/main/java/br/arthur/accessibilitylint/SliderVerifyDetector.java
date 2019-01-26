package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import static com.android.SdkConstants.*;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.resources.ResourceFolderType.RAW;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 18/01/2019.
 */

public class SliderVerifyDetector extends ResourceXmlDetector {

    private static final String SWIPE_REFREH_LAYOUT = "android.support.v4.widget.SwipeRefreshLayout";

    public static final Issue ISSUE_AVOID_SLIDERS = Issue.create(
            "AvoidSliderIssue",
            "Color contrast ratio bad accessibility",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(SliderVerifyDetector.class, RESOURCE_FILE_SCOPE));

    public static final Issue ISSUE_AVOID_SWIPEREFRESHLAYOUT = Issue.create(
            "AvoidPullToRefresh",
            "Avoide use SwipeRefreshLayout to update content. Provide a button to do the action insted",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(SliderVerifyDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }

    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Arrays.asList(SEEK_BAR, SWIPE_REFREH_LAYOUT);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        if(element.getNodeName().contains(SEEK_BAR))
            context.report(ISSUE_AVOID_SLIDERS, element, context.getLocation(element), "AAvoid to use sliders. This components is not well used with screen readers");

        else if(element.getNodeName().contains("SwipeRefreshLayout"))
            context.report(ISSUE_AVOID_SWIPEREFRESHLAYOUT, element, context.getLocation(element), "Avoide use SwipeRefreshLayout to update content. Provide a button to do the action insted");
    }
}
