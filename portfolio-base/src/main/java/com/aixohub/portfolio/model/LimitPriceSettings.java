package com.aixohub.portfolio.model;

import com.aixohub.portfolio.money.Values;
import com.aixohub.portfolio.util.ColorConversion;
import org.eclipse.swt.graphics.Color;

import java.text.DecimalFormat;
import java.util.StringJoiner;

public class LimitPriceSettings {
    private final TypedMap properties;

    public LimitPriceSettings(TypedMap properties) {
        this.properties = properties;
    }

    public boolean getShowRelativeDiff() {
        return properties.getBoolean(PropertyKeys.SHOW_RELATIVE_DIFF);
    }

    public void setShowRelativeDiff(boolean value) {
        properties.putBoolean(PropertyKeys.SHOW_RELATIVE_DIFF, value);
    }

    public boolean getShowAbsoluteDiff() {
        return properties.getBoolean(PropertyKeys.SHOW_ABSOLUTE_DIFF);
    }

    public void setShowAbsoluteDiff(boolean value) {
        properties.putBoolean(PropertyKeys.SHOW_ABSOLUTE_DIFF, value);
    }

    public String getFullLabel(LimitPrice limit, SecurityPrice price) {
        if (price == null)
            return limit.toString();

        StringJoiner joiner = new StringJoiner(" / ", " (", ")"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        joiner.setEmptyValue(""); //$NON-NLS-1$
        if (getShowAbsoluteDiff()) {
            double absDistance = (limit.getValue() - price.getValue()) / Values.Quote.divider();
            DecimalFormat df = new DecimalFormat("+#.##;-#.##"); //$NON-NLS-1$
            joiner.add(df.format(absDistance));
        }

        if (getShowRelativeDiff()) {
            DecimalFormat df = new DecimalFormat("+#.#%;-#.#%"); //$NON-NLS-1$
            joiner.add(df.format(limit.calculateRelativeDistance(price.getValue())));
        }

        return limit.toString() + joiner;
    }

    public Color getLimitExceededPositivelyColor() {
        return getLimitExceededPositivelyColor(null);
    }

    public void setLimitExceededPositivelyColor(Color value) {
        if (value != null)
            properties.putString(PropertyKeys.LIMIT_EXCEEDED_POSITIVELY_COLOR, ColorConversion.toHex(value.getRGBA()));
        else
            properties.remove(PropertyKeys.LIMIT_EXCEEDED_POSITIVELY_COLOR);
    }

    public Color getLimitExceededPositivelyColor(Color fallback) {
        String hex = properties.getString(PropertyKeys.LIMIT_EXCEEDED_POSITIVELY_COLOR);
        return hex != null ? new Color(ColorConversion.hex2RGBA(hex)) : fallback;
    }

    public Color getLimitExceededNegativelyColor() {
        return getLimitExceededNegativelyColor(null);
    }

    public void setLimitExceededNegativelyColor(Color value) {
        if (value != null)
            properties.putString(PropertyKeys.LIMIT_EXCEEDED_NEGATIVELY_COLOR, ColorConversion.toHex(value.getRGBA()));
        else
            properties.remove(PropertyKeys.LIMIT_EXCEEDED_NEGATIVELY_COLOR);
    }

    public Color getLimitExceededNegativelyColor(Color fallback) {
        String hex = properties.getString(PropertyKeys.LIMIT_EXCEEDED_NEGATIVELY_COLOR);
        return hex != null ? new Color(ColorConversion.hex2RGBA(hex)) : fallback;
    }

    private interface PropertyKeys // NOSONAR
    {
        String SHOW_RELATIVE_DIFF = "SHOW_RELATIVE_DIFF";//$NON-NLS-1$
        String SHOW_ABSOLUTE_DIFF = "SHOW_ABSOLUTE_DIFF";//$NON-NLS-1$
        String LIMIT_EXCEEDED_POSITIVELY_COLOR = "LIMIT_EXCEEDED_POSITIVELY_COLOR";//$NON-NLS-1$
        String LIMIT_EXCEEDED_NEGATIVELY_COLOR = "LIMIT_EXCEEDED_NEGATIVELY_COLOR";//$NON-NLS-1$
    }
}
