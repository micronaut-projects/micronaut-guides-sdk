package io.micronaut.guides.core;

public interface GuideRenderProvider {
    GuideRender getGuideRender();

    void setGuideRender(GuideRender guideRender);
}
