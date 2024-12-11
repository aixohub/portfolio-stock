package com.aixohub.portfolio.model;

import com.aixohub.portfolio.model.AttributeType.ImageConverter;
import com.aixohub.portfolio.util.ImageUtil;
import org.eclipse.swt.graphics.Image;


import java.util.HashMap;

public final class ImageManager // NOSONAR
{
    private static final ImageManager instance = new ImageManager();
    private final HashMap<String, Image> imageCache = new HashMap<>();

    private ImageManager() {
    }

    public static ImageManager instance() {
        return instance;
    }

    public static Image getDisabledVersion(Image img) {
        return img;
    }

    /**
     * Retrieves an image associated with the given Attributable and
     * AttributeType. If not found, a default image is returned.
     *
     * @return The retrieved image or null if not found.
     */
    public Image getImage(Attributable target, AttributeType attr) {
        return getImage(target, attr, false);
    }

    /**
     * Retrieves an image associated with the given Attributable and
     * AttributeType. If not found, a default image is returned.
     *
     * @return The retrieved image or null if not found.
     */
    public Image getImage(Attributable target, AttributeType attr, boolean disabled) {
        return getImage(target, attr, 16, 16, disabled);
    }

    /**
     * Retrieves an image associated with the given Attributable, AttributeType,
     * and additional parameters. If not found, a default image is returned.
     *
     * @return The retrieved image or null if not found.
     */
    public Image getImage(Attributable target, AttributeType attr, int width, int height) {
        return getImage(target, attr, width, height, false);
    }

    /**
     * Retrieves an image associated with the given Attributable, AttributeType,
     * and additional parameters. If not found, a default image is returned.
     *
     * @return The retrieved image or null if not found.
     */
    public Image getImage(Attributable target, AttributeType attr, int width, int height, boolean disabled) {
        if (target != null && target.getAttributes().exists(attr) && attr.getConverter() instanceof ImageConverter) {
            Object imgObject = target.getAttributes().get(attr);
            if (imgObject == null)
                return null;

            String imgString = String.valueOf(imgObject);
            String imgKey = imgString + width + height + disabled;
            synchronized (imageCache) {
                Image img = imageCache.getOrDefault(imgKey, null);
                if (img != null)
                    return img;

                img = ImageUtil.instance().toImage(imgString, width, height);
                if (img == null)
                    return null;

                if (disabled) {
                    img = getDisabledVersion(img);
                }
                imageCache.put(imgKey, img);
                return img;
            }
        }
        return null;
    }
}
