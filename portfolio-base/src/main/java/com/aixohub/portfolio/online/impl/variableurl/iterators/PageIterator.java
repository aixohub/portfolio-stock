package com.aixohub.portfolio.online.impl.variableurl.iterators;

import com.aixohub.portfolio.online.impl.variableurl.macros.Macro;
import com.aixohub.portfolio.online.impl.variableurl.macros.PageNumber;
import com.aixohub.portfolio.online.impl.variableurl.urls.PageURL;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PageIterator implements Iterator<String> {
    private final PageURL url;
    private long current = 0;

    public PageIterator(PageURL url) {
        this.url = url;
    }

    @Override
    public boolean hasNext() {
        return current + 1 < Integer.MAX_VALUE;
    }

    @Override
    public String next() {
        current++;

        if (current == Integer.MAX_VALUE)
            throw new NoSuchElementException();

        StringBuilder result = new StringBuilder();

        for (Macro macro : url.getMacros()) {
            if (macro instanceof PageNumber pageNumber) {
                result.append(pageNumber.resolve(current));
            } else {
                result.append(macro.resolve(url.getSecurity()));
            }
        }

        return result.toString();
    }
}
