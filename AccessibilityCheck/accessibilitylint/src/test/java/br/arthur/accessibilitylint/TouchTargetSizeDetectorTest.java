package br.arthur.accessibilitylint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static br.arthur.accessibilitylint.ToolbarOnScrollDetector.ISSUE_TOOLBAR_SCROLL_VIEW;
import static br.arthur.accessibilitylint.TouchTargetSizeDetector.ISSUE_TOUCH_TARGET_VIEW;

/**
 * Created by arthu on 19/01/2019.
 */

public class TouchTargetSizeDetectorTest extends LintDetectorTest {


    @Override
    protected Detector getDetector() {
        return new TouchTargetSizeDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(ISSUE_TOUCH_TARGET_VIEW);
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
                .issues(ISSUE_TOUCH_TARGET_VIEW)
                .run()
                .expectWarningCount(1);
    }
}
