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

import static br.arthur.accessibilitylint.TitleActivityDetector.*;

/**
 * Created by arthu on 25/01/2019.
 */

public class TitleActivityDetectorTest extends LintDetectorTest{
    @Override
    protected Detector getDetector() {
        return new TitleActivityDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(ISSUE_TITLE_ACTIVITY);
    }

    public void test() throws FileNotFoundException {
        String xmlLayout = null;
        URL url= this.getClass().getClassLoader().getResource("AndroidManifest.xml");
        if(url != null) {
            File file = new File(url.getPath());
            xmlLayout = new Scanner(file).useDelimiter("\\A").next();
        }

        lint().files(manifest(xmlLayout))
                .issues(ISSUE_TITLE_ACTIVITY)
                .run()
                .expectWarningCount(1);
    }
}
