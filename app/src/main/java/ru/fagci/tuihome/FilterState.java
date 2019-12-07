package ru.fagci.tuihome;

import java.util.regex.Pattern;

public class FilterState {
    String query;
    Pattern pattern;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
        pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
    }

    public Pattern getPattern() {
        return pattern;
    }


}
