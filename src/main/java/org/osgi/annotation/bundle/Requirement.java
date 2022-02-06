package org.osgi.annotation.bundle;

/**
 * Workaround for "warning: unknown enum constant Resolution.OPTIONAL" during compilation. This class is not used at runtime.
 * See also https://github.com/eclipse/microprofile-config/issues/716
 */
@SuppressWarnings("unused")
public class Requirement {
    public enum Resolution {
        OPTIONAL
    }
}
