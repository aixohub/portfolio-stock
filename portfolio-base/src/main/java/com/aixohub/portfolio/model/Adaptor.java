package com.aixohub.portfolio.model;

import java.util.Optional;

public final class Adaptor {
    private Adaptor() {
    }

    public static <T> T adapt(Class<T> type, Object subject) {
        if (subject == null)
            return null;

        if (type.isAssignableFrom(subject.getClass()))
            return type.cast(subject);

        if (subject instanceof Adaptable adaptable)
            return adaptable.adapt(type);

        return null;
    }

    public static <T> Optional<T> optionally(Class<T> type, Object subject) {
        return Optional.ofNullable(adapt(type, subject));
    }
}
