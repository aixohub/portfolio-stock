package com.aixohub.portfolio.online.impl.variableurl.macros;

import com.aixohub.portfolio.model.Security;
import com.aixohub.portfolio.online.impl.variableurl.VariableURLConstructor;
import com.aixohub.portfolio.online.impl.variableurl.iterators.DateIterator;
import com.aixohub.portfolio.online.impl.variableurl.urls.DateURL;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormattedDate implements Macro {
    private static final Pattern MACRO = Pattern.compile("DATE:(.*?)"); //$NON-NLS-1$

    private final DateTimeFormatter formatter;

    public FormattedDate(CharSequence input) {
        Matcher matcher = MACRO.matcher(input);

        if (!matcher.matches())
            throw new IllegalArgumentException("Bad date macro: " + input); //$NON-NLS-1$

        // throws IllegalArgumentException
        formatter = DateTimeFormatter.ofPattern(matcher.group(1));

        List<String> results = new LinkedList<>();
        (new DateIterator(
                new DateURL(Collections.singletonList(this)),
                LocalDate.of(2016, 1, 1),
                LocalDate.of(2016, 1, 8),
                1
        )).forEachRemaining(results::add);

        // Prevent DoS
        if (results.size() > 2)
            throw new IllegalArgumentException("Too fine date macro: " + input); //$NON-NLS-1$
    }

    @Override
    public VariableURLConstructor getVariableURLConstructor() {
        return DateURL::new;
    }

    @Override
    public CharSequence resolve(Security security) {
        throw new UnsupportedOperationException();
    }

    public String resolve(LocalDate date) {
        return date.format(formatter);
    }
}
