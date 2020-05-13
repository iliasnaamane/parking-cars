package com.mycompany.myapp.domain.enumeration;

/**
 * The Type enumeration.
 */
public enum Type {
    Gasoline("Gasoline"),
    Elec("Elec");
    public final String label;
    private Type(String label) {
        this.label = label;
    }
}
