package br.arthur.accessibilitylint.text;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import static com.android.resources.ResourceFolderType.LAYOUT;

public class TextDetector extends ResourceXmlDetector {
    private List<TextDetectorRule> rules;

    @SuppressWarnings("WeakerAccess") //Need to be public
    public TextDetector() {
        rules = Arrays.asList(new DuplicatedTextRuleText(), new SpacedWordsRuleText());
    }

    @Override
    public void beforeCheckFile(@NotNull Context context) {
        for (TextDetectorRule r : rules)
            r.beforeCheckFile(context);
    }

    @Override
    public void afterCheckFile(@NotNull Context context) {
        for (TextDetectorRule r : rules)
            r.afterCheckFile(context);
    }

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }


    @Nullable
    @Override
    public Collection<String> getApplicableAttributes() {
        return Arrays.asList(SdkConstants.ATTR_CONTENT_DESCRIPTION, SdkConstants.ATTR_TEXT);
    }

    @Override
    public void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute) {
        for (TextDetectorRule r : rules)
            r.visitAttribute(context, attribute);
    }
}
