package ru.fagci.tuihome;

import java.util.regex.Pattern;

public class FilterState {
    public void setQuery(String query) {
        this.query = query;
        pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
    }

    public String getQuery() {
        return query;
    }

    public Pattern getPattern() {
        return pattern;
    }

    String query;
    Pattern pattern;


}
