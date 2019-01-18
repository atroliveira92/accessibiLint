package br.arthur.accessibilitylint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static br.arthur.accessibilitylint.ContrastRatioDetector.ISSUE_COLOR_CONTRAST_RATIO;
import static br.arthur.accessibilitylint.ToolbarOnScrollDetector.*;

/**
 * Created by arthu on 17/01/2019.
 */

public class ToolbarOnScrollDetectorTest extends LintDetectorTest {
    @Override
    protected Detector getDetector() {
        return new ToolbarOnScrollDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(ISSUE_TOOLBAR_SCROLL_VIEW);
    }

    public void test() throws FileNotFoundException {
        String xmlLayout = null;
        URL url= this.getClass().getClassLoader().getResource("layout.xml");
        if(url != null) {
            File file = new File(url.getPath());
            xmlLayout = new Scanner(file).useDelimiter("\\A").next();
        }

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_TOOLBAR_SCROLL_VIEW)
                .run()
                .expectWarningCount(1);
    }
}
