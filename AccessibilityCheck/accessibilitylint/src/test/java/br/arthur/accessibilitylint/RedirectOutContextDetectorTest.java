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

import static br.arthur.accessibilitylint.RedirectOutContextDetector.ISSUE_REDIRECT_OUT_OF_CONTEXT;


/**
 * Created by arthu on 21/01/2019.
 */

public class RedirectOutContextDetectorTest extends LintDetectorTest {
    @Override
    protected Detector getDetector() {
        return new RedirectOutContextDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(ISSUE_REDIRECT_OUT_OF_CONTEXT);
    }

    public void test() throws FileNotFoundException {
        String java;
        URL url= this.getClass().getClassLoader().getResource("Main.java");
        if(url != null) {
            File file = new File(url.getPath());
            java = new Scanner(file).useDelimiter("\\A").next();


            lint().files(
                    java(java))
                    .run()
                    .expectWarningCount(1);
        }
    }

}
