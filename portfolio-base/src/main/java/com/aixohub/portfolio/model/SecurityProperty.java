package com.aixohub.portfolio.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

import java.util.Objects;

@XStreamAlias("property")
@XStreamConverter(value = ToAttributedValueConverter.class, strings = {"value"})
public class SecurityProperty {
    private final Type type;
    private final String name;
    private final String value;
    public SecurityProperty(Type type, String name, String value) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.join(",", type.toString(), name, value); //$NON-NLS-1$
    }

    public enum Type {
        /**
         * Used store a market (e.g. exchange) and symbol pair. It was only used
         * for Portfolio Report which is now providing the symbols as part of
         * the MarketInfo class.
         */
        @Deprecated
        MARKET,

        /**
         * Properties related to loading data from a quote feed.
         */
        FEED
    }
}
