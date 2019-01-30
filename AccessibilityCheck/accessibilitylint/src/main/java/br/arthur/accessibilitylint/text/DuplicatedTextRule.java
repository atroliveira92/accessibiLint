package br.arthur.accessibilitylint.text;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

import br.arthur.accessibilitylint.DetectorRule;

import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class DuplicatedTextRule implements DetectorRule {
    public static final Issue ISSUE_DUPLICATE_TEXTS_IN_LAYOUT = Issue.create(
            "DuplicateTextInLayout",
            "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(TextDetector.class, RESOURCE_FILE_SCOPE));

    private final List<String> titles;

    DuplicatedTextRule() {
        this.titles = new ArrayList<>();
    }

    @Override
    public void beforeCheckFile(@NotNull Context context) {
        //Do nothing
    }

    @Override
    public void afterCheckFile(@NotNull Context context) {
        titles.clear();
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        String value = attribute.getValue();
        if (titles.contains(value)) {
            context.report(ISSUE_DUPLICATE_TEXTS_IN_LAYOUT,
                    attribute,
                    context.getValueLocation(attribute),
                    "Avoid use same texts on layout. If need, provide a diferent contentDescription to differ the element");
        } else {
            titles.add(value);
        }
    }
}
