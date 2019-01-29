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

import static br.arthur.accessibilitylint.SliderVerifyDetector.ISSUE_AVOID_SLIDERS;
import static br.arthur.accessibilitylint.SliderVerifyDetector.ISSUE_AVOID_SWIPEREFRESHLAYOUT;

/**
 * Created by arthu on 25/01/2019.
 */

public class SliderVerifyDetectorTest extends LintDetectorTest {


    @Override
    protected Detector getDetector() {
        return new SliderVerifyDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(ISSUE_AVOID_SLIDERS, ISSUE_AVOID_SWIPEREFRESHLAYOUT);
    }

    public void testSwipe() throws FileNotFoundException {
        String xmlLayout = null;
        URL url= this.getClass().getClassLoader().getResource("layout.xml");
        if(url != null) {
            File file = new File(url.getPath());
            xmlLayout = new Scanner(file).useDelimiter("\\A").next();
        }

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_AVOID_SWIPEREFRESHLAYOUT)
                .run()
                .expectWarningCount(1);
    }

    public void testSlider() throws FileNotFoundException {
        String xmlLayout = null;
        URL url= this.getClass().getClassLoader().getResource("layout.xml");
        if(url != null) {
            File file = new File(url.getPath());
            xmlLayout = new Scanner(file).useDelimiter("\\A").next();
        }

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_AVOID_SLIDERS)
                .run()
                .expectWarningCount(1);
    }
}
