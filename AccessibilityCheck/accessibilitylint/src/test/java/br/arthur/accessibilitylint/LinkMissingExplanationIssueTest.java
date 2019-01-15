package br.arthur.accessibilitylint;


import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.util.Collections;
import java.util.List;

import br.arthur.accessibilitylint.LinkMissingExplanationIssue;

import static br.arthur.accessibilitylint.LinkMissingExplanationIssue.LINK_WITHOUT_RIGHT_EXPLANATION;
import static br.arthur.accessibilitylint.ImportantActionOnMenuIssue.ISSUE_IMPORTANT_ACTION_ON_MENU;

/**
 * Created by arthu on 29/04/2018.
 *
 */
public class LinkMissingExplanationIssueTest extends LintDetectorTest{


    @Override
    protected Detector getDetector() {
        return new LinkMissingExplanationIssue();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(LINK_WITHOUT_RIGHT_EXPLANATION);
    }

    public void testHasSaveInMenu() {
        String xmlMenu = "<menu xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
                "    xmlns:app=\"http://schemas.android.com/apk/res-auto\">" +
                "    <item\n" +
                "        android:id=\"@+id/action_settings\"" +
                "        android:orderInCategory=\"100\"" +
                "        android:title=\"@string/salvar\"" +
                "        app:showAsAction=\"never\"/>" +
                "</menu>";

        lint().files(xml("res/menu/menu_main.xml",xmlMenu))
                .allowMissingSdk()
                .issues(ISSUE_IMPORTANT_ACTION_ON_MENU)
                .run()
                .expectWarningCount(1);

//        String xmlLayout = "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
//                "    android:layout_width=\"match_parent\"" +
//                "    android:layout_height=\"match_parent\"" +
//                "    android:orientation=\"vertical\">" +
//                "" +
//                "    <TextView" +
//                "        android:layout_width=\"wrap_content\"" +
//                "        android:layout_height=\"wrap_content\"" +
//                "        android:text=\"Clique aqui\"" +
//                "        android:onClick=\"acessarLink\"/>" +
//                "    " +
//                "</LinearLayout>";
//
//        lint().files(xml("res/layout/blabla.xml",xmlLayout))
//                .allowMissingSdk()
//                .issues(ISSUE_IMPORTANT_ACTION_ON_MENU)
//                .run()
//                .expectWarningCount(1);
    }

    public void textHasCliqueAqui() {

        String xmlLayout = "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
                "    android:layout_width=\"match_parent\"" +
                "    android:layout_height=\"match_parent\"" +
                "    android:orientation=\"vertical\">" +
                "" +
                "    <TextView" +
                "        android:layout_width=\"wrap_content\"" +
                "        android:layout_height=\"wrap_content\"" +
                "        android:text=\"Clique aqui\"" +
                "        android:onClick=\"acessarLink\"/>" +
                "    " +
                "</LinearLayout>";

        lint().files(xml("res/menu/menu_main.xml",xmlLayout))
                .allowMissingSdk()
                .issues(LINK_WITHOUT_RIGHT_EXPLANATION)
                .run()
                .expectWarningCount(1);
    }
}