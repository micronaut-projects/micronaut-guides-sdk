package io.micronaut.guides.core;

import io.micronaut.core.util.CollectionUtils;
import io.micronaut.starter.application.ApplicationType;

import java.util.ArrayList;
import java.util.List;

public final class GuideTestUtils {
    private GuideTestUtils() {
    }

    public static Guide guideWithSlug(String slug) {
        return guideWithSlug(slug, "default");
    }

    public static Guide guideWithSlug(String slug, String appName) {
        Guide guide = new Guide();
        guide.setSlug(slug);

        if (CollectionUtils.isEmpty(guide.getApps())) {
            List<App> apps = new ArrayList<>();
            App app = new App();
            app.setName(appName);
            app.setPackageName(GuidesConfigurationProperties.DEFAULT_PACKAGE_NAME);
            app.setApplicationType(ApplicationType.DEFAULT);
            app.setFramework("Micronaut");
            apps.add(app);
            guide.setApps(apps);
        }
        return guide;
    }
}
