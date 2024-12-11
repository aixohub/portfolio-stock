package com.aixohub.portfolio.online.impl.variableurl;

import com.aixohub.portfolio.online.impl.variableurl.macros.ConstString;
import com.aixohub.portfolio.online.impl.variableurl.macros.Currency;
import com.aixohub.portfolio.online.impl.variableurl.macros.FormattedDate;
import com.aixohub.portfolio.online.impl.variableurl.macros.ISIN;
import com.aixohub.portfolio.online.impl.variableurl.macros.Macro;
import com.aixohub.portfolio.online.impl.variableurl.macros.PageNumber;
import com.aixohub.portfolio.online.impl.variableurl.macros.TickerSymbol;
import com.aixohub.portfolio.online.impl.variableurl.macros.Today;
import com.aixohub.portfolio.online.impl.variableurl.macros.WKN;
import com.aixohub.portfolio.online.impl.variableurl.urls.ConstURL;
import com.aixohub.portfolio.online.impl.variableurl.urls.VariableURL;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Factory {
    private static final Pattern URL_MACRO_ABSTRACT = Pattern.compile("\\{(.*?)}"); //$NON-NLS-1$
    private static final Collection<MacroParser> MACRO_PARSERS = Arrays.asList(FormattedDate::new, PageNumber::new,
            ISIN::new, WKN::new, TickerSymbol::new, Currency::new, Today::new);
    private Factory() {
    }

    public static VariableURL fromString(CharSequence input) {
        Matcher abstractMatcher = URL_MACRO_ABSTRACT.matcher(input);
        int lastEnd = 0;
        CharSequence part;
        List<Macro> macros = new LinkedList<>();
        Macro macro = null;

        while (abstractMatcher.find()) {
            part = input.subSequence(lastEnd, abstractMatcher.start());

            if (part.length() > 0)
                macros.add(new ConstString(part));

            lastEnd = abstractMatcher.end();
            part = abstractMatcher.group(1);

            for (MacroParser macroParser : MACRO_PARSERS) {
                try {
                    macro = macroParser.parse(part);
                } catch (Exception e) {
                    continue;
                }

                macros.add(macro);
                break;
            }

            if (macro == null)
                return new ConstURL(Collections.singletonList(new ConstString(input)));

            macro = null;
        }

        part = input.subSequence(lastEnd, input.length());

        if (part.length() > 0)
            macros.add(new ConstString(part));

        VariableURLConstructor variableURLConstructor = null;

        for (Macro urlMacro : macros) {
            VariableURLConstructor constructor = urlMacro.getVariableURLConstructor();

            if (variableURLConstructor == null)
                variableURLConstructor = constructor;
            else if (constructor != null && constructor != variableURLConstructor)
                return new ConstURL(Collections.singletonList(new ConstString(input)));

        }

        return variableURLConstructor == null ? new ConstURL(macros) : variableURLConstructor.construct(macros);
    }

    private interface MacroParser {
        Macro parse(CharSequence input) throws IllegalArgumentException;
    }
}
