package br.arthur.accessibilitylint.text;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.TextFormat;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.arthur.accessibilitylint.DetectorRule;

import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

public class SpacedWordsRule implements DetectorRule {
    /**
     * Check spaced words like "W E L C O M E"
     */
    public static final Issue ISSUE_SPACED_WORDS = Issue.create(
            "SpacedWords",
            "Avoid using spaced words like \"W E L C O M E\"",
            "Talkback may have some difficulty reading text with spacing",
            A11Y,
            6,
            WARNING,
            new Implementation(TextDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public void beforeCheckFile(@NotNull Context context) {
        //Do nothing
    }

    @Override
    public void afterCheckFile(@NotNull Context context) {
        //Do nothing
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        String value = attribute.getValue();

        if (hasSpacingBetweenWords(value)) {
            context.report(ISSUE_SPACED_WORDS,
                    attribute,
                    context.getValueLocation(attribute),
                    ISSUE_SPACED_WORDS.getBriefDescription(TextFormat.TEXT));
        }
    }

    private boolean hasSpacingBetweenWords(String text) {
        int countCharactersWithoutSpace = countCharactersWithoutSpace(text);
        int countCharactersBetweenSpace = text.split("\\s+").length;

        return countCharactersWithoutSpace == countCharactersBetweenSpace;
    }

    private int countCharactersWithoutSpace(String text) {
        Pattern pattern = Pattern.compile("\\S");
        Matcher matcher = pattern.matcher(text);

        int count = 0;
        int i = 0;
        while (matcher.find(i)) {
            count++;
            i = matcher.start() + 1;
        }

        return count;
    }
}
