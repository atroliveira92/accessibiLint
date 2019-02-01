package br.arthur.accessibilitylint;

import com.android.resources.ResourceFolderType;
import com.android.tools.lint.checks.TooManyViewsDetector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;

import java.util.EnumSet;

import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 31/01/2019.
 */

public class TooManyInteractableViewsDetector extends ResourceXmlDetector {

    static final Issue ISSUE_TOO_MANY_INTERACTIONS_VIEW = Issue.create(
            "TooManyInteractionsViews",
            "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(TooManyInteractableViewsDetector.class, RESOURCE_FILE_SCOPE));


    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }
}
