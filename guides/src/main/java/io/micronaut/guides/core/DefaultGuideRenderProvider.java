package io.micronaut.guides.core;

import jakarta.inject.Singleton;

@Singleton
public class DefaultGuideRenderProvider implements GuideRenderProvider {
    private GuideRender guideRender;

    @Override
    public void setGuideRender(GuideRender guideRender) {
        this.guideRender = guideRender;
    }

    @Override
    public GuideRender getGuideRender() {
        return guideRender;
    }
}
