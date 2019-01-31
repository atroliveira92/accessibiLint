package br.arthur.accessibilitylint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

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


    public void testChildWithMarginAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_margin=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_margin=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginAndParentWithMarginLeft() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            android:clickable=\"false\"\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginStart=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_marginLeft=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_margin=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginAndParentWithMarginRight() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginEnd=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_marginRight=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_margin=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginAndParentWithMarginBottom() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_margin=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_marginBottom=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_margin=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginAndParentWithMarginTop() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_margin=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_marginTop=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_margin=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginRightAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginEnd=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_marginEnd=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginLeftAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginStart=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_marginStart=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginTopAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginTop=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_marginTop=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithMarginBottomAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginBottom=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_marginBottom=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }

    public void testChildWithoutMarginAndParentWithMargin() throws FileNotFoundException {
        String xmlLayout = "<LinearLayout\n" +
                "            xmlns:android=\"http://schemas.android.com/apk/res/android\"\n" +
                "            android:clickable=\"false\"\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:layout_marginBottom=\"1dp\"" +
                "            android:orientation=\"vertical\">\n" +
                "\n" +
                "        <RelativeLayout\n" +
                "            android:layout_width=\"match_parent\"\n" +
                "            android:layout_height=\"wrap_content\"\n" +
                "            android:orientation=\"vertical\"\n" +
                "            android:layout_margin=\"3dp\">\n" +
                "\n" +
                "            <TextView\n" +
                "                android:layout_width=\"wrap_content\"\n" +
                "                android:layout_height=\"wrap_content\"\n" +
                "                android:text=\"Click here\"\n" +
                "                android:layout_marginBottom=\"3dp\"" +
                "                android:onClick=\"accessLink\"\n" +
                "                android:textColor=\"#A68026\"/>" +
                "\n" +
                "       </RelativeLayout>" +
                "\n" +
                "</LinearLayout>";

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_BORDER_COMPONENT_DETECTOR)
                .run()
                .expectWarningCount(1);
    }
}
