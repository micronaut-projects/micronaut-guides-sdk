package io.micronaut.guides.core.html;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HtmlUtilsTest {

    @Test
    void link() {
        assertEquals("<a href=\"https://www.micronaut.io\">Micronaut</a>", HtmlUtils.link("https://www.micronaut.io", "Micronaut"));
    }

    @Test
    void img() {
        assertEquals("<img src=\"https://graal.cloud/gdk/resources/img/gdk_modules/download-archive.png\" alt=\"Download completed example\" class=\"download-img-guides foo\"/>",
                HtmlUtils.img("https://graal.cloud/gdk/resources/img/gdk_modules/download-archive.png", "Download completed example", "download-img-guides", "foo"));
    }
}