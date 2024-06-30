package ca.pragmaticcoding.blog.pseudo;

import javafx.css.PseudoClass;

public enum StatusEnum implements PseudoClassProvider {
    NORMAL {
        @Override
        public PseudoClass getPseudoClass() {
            return normal;
        }
    }, WARNING {
        @Override
        public PseudoClass getPseudoClass() {
            return warning;
        }
    }, ERROR {
        @Override
        public PseudoClass getPseudoClass() {
            return error;
        }
    }, FAILED {
        @Override
        public PseudoClass getPseudoClass() {
            return failed;
        }
    };

    private static final PseudoClass normal = PseudoClass.getPseudoClass("normal");
    private static final PseudoClass warning = PseudoClass.getPseudoClass("warning");
    private static final PseudoClass error = PseudoClass.getPseudoClass("error");
    private static final PseudoClass failed = PseudoClass.getPseudoClass("failed");
}
