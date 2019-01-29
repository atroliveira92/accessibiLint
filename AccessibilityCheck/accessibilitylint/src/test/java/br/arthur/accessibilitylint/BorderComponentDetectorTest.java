package br.arthur.accessibilitylint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static br.arthur.accessibilitylint.BorderComponentDetector.ISSUE_BORDER_COMPONENT_DETECTOR;



public class BorderComponentDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new BorderComponentDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(ISSUE_BORDER_COMPONENT_DETECTOR);
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
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }
}
