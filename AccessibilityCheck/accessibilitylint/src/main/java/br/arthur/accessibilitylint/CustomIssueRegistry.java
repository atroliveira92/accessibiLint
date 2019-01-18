package br.arthur.accessibilitylint;

import com.android.tools.lint.client.api.IssueRegistry;
import com.android.tools.lint.detector.api.Issue;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomIssueRegistry extends IssueRegistry {

    @NotNull
    @Override
    public List<Issue> getIssues() {
        return new ArrayList<Issue>() {
            {
                add(LinkMissingExplanationIssue.LINK_WITHOUT_RIGHT_EXPLANATION);
                add(ImportantActionOnMenuIssue.ISSUE_IMPORTANT_ACTION_ON_MENU);
                add(BackgroundNoClickDetector.ISSUE_NO_CLICK_ON_BACKGROUND_VIEW);
                add(ContrastRatioDetector.ISSUE_COLOR_CONTRAST_RATIO);
            }
        };
    }
}

