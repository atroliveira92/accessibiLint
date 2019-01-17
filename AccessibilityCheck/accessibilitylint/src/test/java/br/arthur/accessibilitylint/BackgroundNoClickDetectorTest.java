package br.arthur.accessibilitylint;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Issue;
import com.android.utils.SdkUtils;

import org.apache.commons.compress.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static br.arthur.accessibilitylint.BackgroundNoClickDetector.ISSUE_NO_CLICK_ON_BACKGROUND_VIEW;


/**
 * Created by arthu on 14/01/2019.
 */

public class BackgroundNoClickDetectorTest extends LintDetectorTest {

    @Override
    protected Detector getDetector() {
        return new BackgroundNoClickDetector();
    }

    @Override
    protected List<Issue> getIssues() {
        return Collections.singletonList(ISSUE_NO_CLICK_ON_BACKGROUND_VIEW);
    }


    public void test() throws Exception {
//        CodeSource source = getClass().getProtectionDomain().getCodeSource();
//        if (source != null) {
//            URL location = source.getLocation();
//            try {
//                File classesDir = SdkUtils.urlToFile(location);
//                File file = classesDir.getParentFile().getAbsoluteFile().getParentFile().getParentFile();
//                String path = "/accessibilitylint/src/test/res";
//                String pathFile = (path  + File.separatorChar + "layout.xml").replace('/', File.separatorChar);
//                File filePath = new File(file, pathFile);
//            } catch (MalformedURLException e) {
//                fail(e.getLocalizedMessage());
//            }
//        }
        String xmlLayout = null;
         URL url= this.getClass().getClassLoader().getResource("layout.xml");
         if(url != null) {
             File file = new File(url.getPath());
             xmlLayout = new Scanner(file).useDelimiter("\\A").next();
         }

//        if(xmlLayout == null) {
//            xmlLayout = "<LinearLayout xmlns:android=\"http://schemas.android.com/apk/res/android\"" +
//                    "    android:layout_width=\"match_parent\"" +
//                    "    android:layout_height=\"match_parent\"" +
//                    "    android:onClick=\"acessarLink\"" +
//                    "    android:orientation=\"vertical\">" +
//                    "" +
//                    "    <TextView" +
//                    "        android:layout_width=\"wrap_content\"" +
//                    "        android:layout_height=\"wrap_content\"" +
//                    "        android:text=\"Clique aqui\"" +
//                    "        android:onClick=\"acessarLink\"/>" +
//                    "    " +
//                    "</LinearLayout>";
        //}

        lint().files(xml("res/layout/layout.xml", xmlLayout))
                .allowMissingSdk()
                .issues(ISSUE_NO_CLICK_ON_BACKGROUND_VIEW)
                .run()
                .expectWarningCount(1);
    }

    public static String getStringFromFile (File file) throws Exception {
        FileInputStream fin = new FileInputStream(file);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
