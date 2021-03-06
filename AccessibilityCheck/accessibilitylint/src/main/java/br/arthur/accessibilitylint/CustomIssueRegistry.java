package br.arthur.accessibilitylint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import br.arthur.accessibilitylint.all.TooManyInteractableViewsDetector;
import br.arthur.accessibilitylint.all.ViewGroupInteractionDetector;
import br.arthur.accessibilitylint.text.DuplicatedTextRuleText;
import br.arthur.accessibilitylint.text.SpacedWordsRuleText;

public class CustomIssueRegistry extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return new ArrayList<Issue>() {
            {
                add(LinkMissingExplanationDetector.LINK_WITHOUT_RIGHT_EXPLANATION);
                add(ImportantActionOnMenuDetector.ISSUE_IMPORTANT_ACTION_ON_MENU);
                add(BackgroundNoClickDetector.ISSUE_NO_CLICK_ON_BACKGROUND_VIEW);
                add(ContrastRatioDetector.ISSUE_COLOR_CONTRAST_RATIO);
                add(ToolbarOnScrollDetector.ISSUE_TOOLBAR_SCROLL_VIEW);
                add(SliderVerifyDetector.ISSUE_AVOID_SLIDERS);
                add(SliderVerifyDetector.ISSUE_AVOID_SWIPEREFRESHLAYOUT);
                add(TouchTargetSizeDetector.ISSUE_TOUCH_TARGET_VIEW);
                add(RedirectOutContextDetector.ISSUE_REDIRECT_OUT_OF_CONTEXT);
                add(TitleActivityDetector.ISSUE_TITLE_ACTIVITY);
                add(EditTextDetector.ISSUE_AWAYS_IMPLEMENT_INPUTTEXT);
                add(EditTextDetector.ISSUE_USE_AUTOCOMPLETE_TEXT_VIEW);
                add(DuplicatedTextRuleText.ISSUE_DUPLICATE_TEXTS_IN_LAYOUT);
                add(SpacedWordsRuleText.ISSUE_SPACED_WORDS);
                add(TooManyInteractableViewsDetector.ISSUE_LIST_ITEM_IN_TOO_MANY_VIEWS_LAYOUT);
                add(TooManyInteractableViewsDetector.ISSUE_TOO_MANY_INTERACTIONS_VIEW);
                add(ViewGroupInteractionDetector.ISSUE_VIEWGROUP_CLICKABLE);
            }
        };
    }
}

