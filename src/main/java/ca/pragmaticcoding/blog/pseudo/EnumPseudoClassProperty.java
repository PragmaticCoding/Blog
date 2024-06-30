package ca.pragmaticcoding.blog.pseudo;

import javafx.beans.property.ObjectPropertyBase;
import javafx.scene.Node;

public class EnumPseudoClassProperty<T extends PseudoClassProvider> extends ObjectPropertyBase<T> {
    private final Node node;
    private PseudoClassProvider oldValue;

    public EnumPseudoClassProperty(Node node) {
        this.node = node;
    }

    public EnumPseudoClassProperty(Node node, T initialValue) {
        this.node = node;
        this.set(initialValue);
    }

    @Override
    protected void invalidated() {
        if (oldValue != null) {
            node.pseudoClassStateChanged(oldValue.getPseudoClass(), false);
        }
        if (getValue() != null) {
            node.pseudoClassStateChanged(getValue().getPseudoClass(), true);
        }
        oldValue = getValue();
    }

    @Override
    public Object getBean() {
        return node;
    }

    @Override
    public String getName() {
        return "";
    }
}
