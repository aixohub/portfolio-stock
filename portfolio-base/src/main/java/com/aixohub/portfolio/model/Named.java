package com.aixohub.portfolio.model;

import com.aixohub.portfolio.util.TextUtil;

import java.io.Serializable;
import java.util.Comparator;

/**
 * An Named object has an editable name and note.
 */
public interface Named extends Annotated {
    String getName();

    void setName(String name);

    class ByName implements Comparator<Named>, Serializable {
        private static final long serialVersionUID = 1L;

        @Override
        public int compare(Named n1, Named n2) {
            if (n1 == null)
                return n2 == null ? 0 : -1;
            return TextUtil.compare(n1.getName(), n2.getName());
        }
    }
}
