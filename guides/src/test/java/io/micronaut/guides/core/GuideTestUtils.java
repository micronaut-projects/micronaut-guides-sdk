package io.micronaut.guides.core;

import java.util.List;
import java.util.Map;

public final class GuideTestUtils {
    private GuideTestUtils() {
    }

    public static Guide guideWithSlug(String slug) {
        Guide guide = new Guide();
        guide.setSlug(slug);
        return guide;
    }
}
