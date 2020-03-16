package org.fibo.seo.autocomplete.dto;

/**
 * One of autocomplete results
 */
public class AutocompleteDto {
    public String value;
    // if this is a reference to a market category, can be deleted if no effect
    public boolean isAlias;
    // identical to keyword
    public boolean isIdentical;
}
