package br.arthur.accessibilitylint.text;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import org.intellij.lang.annotations.Language;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static br.arthur.accessibilitylint.text.DuplicatedTextRule.ISSUE_DUPLICATE_TEXTS_IN_LAYOUT;
import static br.arthur.accessibilitylint.text.SpacedWordsRule.ISSUE_SPACED_WORDS;

public class TextDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new TextDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(ISSUE_DUPLICATE_TEXTS_IN_LAYOUT, ISSUE_SPACED_WORDS);
    }

    public void testSpacedWords() throws FileNotFoundException {
        URL url = this.getClass().getClassLoader().getResource("layout.xml");
        @SuppressWarnings("ConstantConditions")
        File file = new File(url.getPath());
        @Language("XML")
        String xmlLayout = new Scanner(file).useDelimiter("\\A").next();

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_SPACED_WORDS)
                .run()
                .expectWarningCount(1);
    }
}