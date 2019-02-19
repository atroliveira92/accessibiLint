package br.arthur.accessibilitylint.all;

import com.android.tools.lint.detector.api.XmlContext;

import org.w3c.dom.Element;

/**
 * Created by arthu on 02/02/2019.
 */

public interface AllLayoutDetectorRule {

    void visitElement(XmlContext context, Element element);
}
