package br.arthur.accessibilitylint.all;

import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_CLICKABLE;
import static com.android.SdkConstants.ATTR_CONTENT_DESCRIPTION;
import static com.android.SdkConstants.ATTR_FOCUSABLE;
import static com.android.SdkConstants.ATTR_HINT;
import static com.android.SdkConstants.ATTR_ON_CLICK;
import static com.android.SdkConstants.ATTR_TITLE;
import static com.android.SdkConstants.GRID_VIEW;
import static com.android.SdkConstants.LIST_VIEW;
import static com.android.SdkConstants.SPINNER;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;



public class TooManyInteractableViewsDetector implements AllLayoutDetectorRule {

    public static final Issue ISSUE_TOO_MANY_INTERACTIONS_VIEW =  Issue.create(
            "TooManyTouchableViews",
            "Layout has too many views that can be interact and it's bad for visually impairment people. Check your design layout",
            "Using too many views in a single layout is bad for "
                    + "performance. Consider using compound drawables or other tricks for "
                    + "reducing the number of views in this layout.\n"
                    + "\n"
                    + "The maximum view count defaults to 10 but can be configured with the "
                    + "environment variable `ANDROID_LINT_MAX_VIEW_COUNT`.",
            A11Y,
            5,
            WARNING,
            new Implementation(AllLayoutDetector.class, RESOURCE_FILE_SCOPE));

    public static final Issue ISSUE_LIST_ITEM_IN_TOO_MANY_VIEWS_LAYOUT = Issue.create(
            "ListWithTooManyViewsLayout",
            "Layout has a list view and layout already has too many interative views. Check if can increase the number of interactive views",
            "The layout already has too many interactive views. With an list View like RecyclerView, GridView or Spinner, the amount of " +
                    "integrative items can increase. Please, check your layout design and check if really necessary it contains the items presents.\n" +
                    "If you want you can separate your layout in tabLayout, for example, for user interact with the right information at time",
            A11Y,
            5,
            WARNING,
            new Implementation(AllLayoutDetector.class, RESOURCE_FILE_SCOPE)
    );

    private static final int MAX_VIEW_COUNT = 10;
    private static final List<String> clickList =
                         Arrays.asList("RecyclerView", GRID_VIEW, LIST_VIEW, SPINNER);

//    static {
//        int maxViewCount = 0;
//        int maxDepth = 0;
//
//        String countValue = System.getenv("ANDROID_LINT_MAX_VIEW_COUNT");
//        if (countValue != null) {
//            try {
//                maxViewCount = Integer.parseInt(countValue);
//            } catch (NumberFormatException e) {
//                // pass: set to default below
//            }
//        }
//
//        if (maxViewCount == 0) {
//            maxViewCount = 5;
//        }
//
//        MAX_VIEW_COUNT = maxViewCount;
//    }

    private int maxCount;
    private List<Element> lstListElements = new ArrayList<>();



    public void checkList(XmlContext xmlContext) {
        if(lstListElements.size() > 0) {
            if(maxCount > (MAX_VIEW_COUNT / 2)) {

                for(Element element : lstListElements) {
                    xmlContext.report(ISSUE_LIST_ITEM_IN_TOO_MANY_VIEWS_LAYOUT, element,
                            xmlContext.getLocation(element),
                            "hahaha");
                }

                lstListElements = new ArrayList<>();
            }
        }
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        Attr attrOnClick = element.getAttributeNodeNS(ANDROID_URI, ATTR_ON_CLICK);
        Attr attrClickable = element.getAttributeNodeNS(ANDROID_URI, ATTR_CLICKABLE);
        Attr attrFocusable = element.getAttributeNodeNS(ANDROID_URI, ATTR_FOCUSABLE);
        Attr attrContentDescription = element.getAttributeNodeNS(ANDROID_URI, ATTR_CONTENT_DESCRIPTION);
        Attr attrTitle = element.getAttributeNodeNS(ANDROID_URI, ATTR_TITLE);
        Attr attrHint = element.getAttributeNodeNS(ANDROID_URI, ATTR_HINT);

        if(attrOnClick != null) {
            maxCount ++;
        } else if(attrClickable != null && "true".equals(attrClickable.getValue())) {
            maxCount ++;
        } else if(attrFocusable != null && "true".equals(attrFocusable.getValue())) {
            maxCount++;
        } else if(attrContentDescription != null) {
            maxCount++;
        } else if(attrTitle != null) {
            maxCount++;
        } else if(attrHint != null) {
            maxCount++;
        }

        checkAndReport(context, element);

        String elementName = element.getTagName();
        if(clickList.contains(elementName)) {
            lstListElements.add(element);
        }

        //checkList(context);
    }


    private void checkAndReport(XmlContext context, Element element) {
        if(maxCount > MAX_VIEW_COUNT) {
            String msg =
                    String.format(
                            "`%1$s` has more than %2$d interactive views. It's bad for visally impairment people interaction, please check your layout",
                            context.file.getName(), MAX_VIEW_COUNT);
            context.report(ISSUE_TOO_MANY_INTERACTIONS_VIEW, element, context.getLocation(element), msg);
        }
    }

}
