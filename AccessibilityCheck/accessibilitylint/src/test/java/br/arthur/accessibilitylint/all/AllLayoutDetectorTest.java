package br.arthur.accessibilitylint.all;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import br.arthur.accessibilitylint.all.TooManyInteractableViewsDetector;

import static br.arthur.accessibilitylint.all.TooManyInteractableViewsDetector.ISSUE_LIST_ITEM_IN_TOO_MANY_VIEWS_LAYOUT;
import static br.arthur.accessibilitylint.all.TooManyInteractableViewsDetector.ISSUE_TOO_MANY_INTERACTIONS_VIEW;
import static br.arthur.accessibilitylint.all.ViewGroupInteractionDetector.ISSUE_VIEWGROUP_CLICKABLE;

/**
 * Created by arthu on 01/02/2019.
 */

public class AllLayoutDetectorTest extends LintDetectorTest {


    @Override
    protected Detector getDetector() {
        return new AllLayoutDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Arrays.asList(ISSUE_LIST_ITEM_IN_TOO_MANY_VIEWS_LAYOUT, ISSUE_TOO_MANY_INTERACTIONS_VIEW, ISSUE_VIEWGROUP_CLICKABLE);
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
                .issues(ISSUE_TOO_MANY_INTERACTIONS_VIEW)
                .run()
                .expectWarningCount(1);

    }

    public void testViewGroup() throws FileNotFoundException {
        String xmlLayout = null;
        URL url= this.getClass().getClassLoader().getResource("layout.xml");
        if(url != null) {
            File file = new File(url.getPath());
            xmlLayout = new Scanner(file).useDelimiter("\\A").next();
        }

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_VIEWGROUP_CLICKABLE)
                .run()
                .expectWarningCount(1);
    }
}
