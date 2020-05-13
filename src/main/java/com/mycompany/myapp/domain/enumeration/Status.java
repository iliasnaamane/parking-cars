package com.mycompany.myapp.domain.enumeration;

/**
 * The Status enumeration.
 */
public enum Status {
    Free("Free"), Full("Full");
    private final String label;
    private Status(String label) {
        this.label = label;
    } 
}
