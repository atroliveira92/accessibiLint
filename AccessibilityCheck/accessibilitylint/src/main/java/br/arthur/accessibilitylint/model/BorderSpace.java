package br.arthur.accessibilitylint.model;

import com.android.SdkConstants;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import static com.android.SdkConstants.ANDROID_URI;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_BOTTOM;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_END;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_LEFT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_RIGHT;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_START;
import static com.android.SdkConstants.ATTR_LAYOUT_MARGIN_TOP;
import static com.android.SdkConstants.ATTR_PADDING;
import static com.android.SdkConstants.ATTR_PADDING_BOTTOM;
import static com.android.SdkConstants.ATTR_PADDING_END;
import static com.android.SdkConstants.ATTR_PADDING_LEFT;
import static com.android.SdkConstants.ATTR_PADDING_RIGHT;
import static com.android.SdkConstants.ATTR_PADDING_START;
import static com.android.SdkConstants.ATTR_PADDING_TOP;

public class BorderSpace {
    private static final int DISREGARD_ATTRIBUTE = 0;
    private final int paddingTop;
    private final int paddingBottom;
    private final int paddingStart;
    private final int paddingEnd;
    private int borderSpaceTop;
    private int borderSpaceBottom;
    private int borderSpaceStart;
    private int borderSpaceEnd;

    public BorderSpace(Element element) {
        int margin = findValue(element, ATTR_LAYOUT_MARGIN);
        borderSpaceTop = (margin == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_LAYOUT_MARGIN_TOP) : margin;
        borderSpaceBottom = (margin == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_LAYOUT_MARGIN_BOTTOM) : margin;
        borderSpaceStart = (margin == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_LAYOUT_MARGIN_START, ATTR_LAYOUT_MARGIN_LEFT) : margin;
        borderSpaceEnd = (margin == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_LAYOUT_MARGIN_END, ATTR_LAYOUT_MARGIN_RIGHT) : margin;

        int padding = findValue(element, ATTR_PADDING);
        this.paddingTop = (padding == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_PADDING_TOP) : padding;
        this.paddingBottom = (padding == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_PADDING_BOTTOM) : padding;
        this.paddingStart = (padding == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_PADDING_START, ATTR_PADDING_LEFT) : padding;
        this.paddingEnd = (padding == DISREGARD_ATTRIBUTE)
                ? findValue(element, ATTR_PADDING_END, ATTR_PADDING_RIGHT) : padding;

        sumParent(element.getParentNode());
    }

    /**
     * Find margin from element
     *
     * @param element       used to pick up the margin
     * @param attr          type of margin. If the element has no margin, the secondary element will
     *                      be used. See: {@link SdkConstants#ATTR_LAYOUT_MARGIN},
     *                      {@link SdkConstants#ATTR_PADDING}, etc
     * @param attrSecundary type of margin with less priority
     * @return the margin found or {@link BorderSpace#DISREGARD_ATTRIBUTE}
     */
    private static int findValue(Element element, String attr, String attrSecundary) {
        int marginValue = findValue(element, attr);

        if (marginValue != DISREGARD_ATTRIBUTE) {
            return marginValue;
        } else return findValue(element, attrSecundary);
    }

    /**
     * Find margin from element
     *
     * @param element used to pick up the margin
     * @param attr    type of margin. See: {@link SdkConstants#ATTR_LAYOUT_MARGIN},
     *                {@link SdkConstants#ATTR_PADDING}, etc
     * @return the margin found or {@link BorderSpace#DISREGARD_ATTRIBUTE}
     */
    private static int findValue(Element element, String attr) {
        Attr attribute = element.getAttributeNodeNS(ANDROID_URI, attr);
        if (attribute != null) {
            String value = attribute.getValue();

            if (!value.startsWith("@") && (value.endsWith("dp") || value.endsWith("dip"))) {
                int index = value.indexOf("d");
                value = value.substring(0, index);
                return Integer.parseInt(value);
            }
        }

        return DISREGARD_ATTRIBUTE;
    }

    public int getBorderSpaceTop() {
        return borderSpaceTop;
    }

    public int getBorderSpaceBottom() {
        return borderSpaceBottom;
    }

    public int getBorderSpaceStart() {
        return borderSpaceStart;
    }

    public int getBorderSpaceEnd() {
        return borderSpaceEnd;
    }

    private void sumParent(Node node) {
        if (!(node instanceof Element) || node.getNodeType() != Node.ELEMENT_NODE)
            return;

        Element element = (Element) node;
        sumParentSpace(new BorderSpace(element));
    }

    private void sumParentSpace(BorderSpace parent) {
        this.borderSpaceTop += parent.borderSpaceTop + parent.paddingTop;
        this.borderSpaceBottom += parent.borderSpaceBottom + parent.paddingBottom;
        this.borderSpaceStart += parent.borderSpaceStart + parent.paddingStart;
        this.borderSpaceEnd += parent.borderSpaceEnd + parent.paddingEnd;
    }
}
