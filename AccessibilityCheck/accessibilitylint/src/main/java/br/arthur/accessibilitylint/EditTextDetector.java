package br.arthur.accessibilitylint;

import com.android.SdkConstants;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.XmlContext;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_INPUT_TYPE;
import static com.android.SdkConstants.EDIT_TEXT;
import static com.android.resources.ResourceFolderType.LAYOUT;
import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.RESOURCE_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 26/01/2019.
 *
 */

public class EditTextDetector extends ResourceXmlDetector {
    static final Issue ISSUE_AWAYS_IMPLEMENT_INPUTTEXT = Issue.create(
            "AwaysImplementInputTextIssue",
            "Aways implement android:inputText",
            "Problemas com a cor do componente",
            A11Y,
            4,
            WARNING,
            new Implementation(EditTextDetector.class, RESOURCE_FILE_SCOPE));

    static final Issue ISSUE_USE_AUTOCOMPLETE_TEXT_VIEW = Issue.create(
            "UseAutoCompleteTextViewIssue",
            "Use autoCompleteTextView insted of regular EditText",
            "Problemas com a cor do componente",
            A11Y,
            2,
            WARNING,
            new Implementation(EditTextDetector.class, RESOURCE_FILE_SCOPE));

    @Override
    public boolean appliesTo(ResourceFolderType folderType) {
        return EnumSet.of(LAYOUT).contains(folderType);
    }


    @Nullable
    @Override
    public Collection<String> getApplicableElements() {
        return Collections.singleton(EDIT_TEXT);
    }

    @Override
    public void visitElement(XmlContext context, Element element) {
        checkIfHasInputTypeAttribute(context, element);

        checkIfNeedToUseAutoCompleteTextView(context, element);
    }

    private void checkIfHasInputTypeAttribute(XmlContext context, Element element) {
        Attr attribute = element.getAttributeNodeNS(ANDROID_URI, ATTR_INPUT_TYPE);
        if(attribute == null){
            context.report(ISSUE_AWAYS_IMPLEMENT_INPUTTEXT, element, context.getLocation(element),
                    "Aways provide `android:inputtype`, it helps screen reader users to interact with correct keyboard");
        }
    }

    private void checkIfNeedToUseAutoCompleteTextView(XmlContext context, Element element) {
        Attr attributeId = element.getAttributeNodeNS(ANDROID_URI, ATTR_ID);
        if(attributeId != null) {
            String value = attributeId.getValue();

            if(value.contains("search") || value.contains("Search") ||
                    value.contains("find") || value.contains("Find")) {
                context.report(ISSUE_AWAYS_IMPLEMENT_INPUTTEXT, element, context.getLocation(element),
                        "Use AutoCompleteTextView. With this, you can provide search shortcuts to help user make the seatch");
            }
        }
    }
}
