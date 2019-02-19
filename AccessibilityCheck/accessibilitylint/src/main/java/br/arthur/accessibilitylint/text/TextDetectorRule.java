package br.arthur.accessibilitylint.text;

import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Attr;

public interface TextDetectorRule {
    void beforeCheckFile(@NotNull Context context);

    void afterCheckFile(@NotNull Context context);

    void visitAttribute(@NotNull XmlContext context, @NotNull Attr attribute);
}