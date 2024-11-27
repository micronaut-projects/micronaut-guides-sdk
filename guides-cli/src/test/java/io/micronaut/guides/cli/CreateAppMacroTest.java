package io.micronaut.guides.cli;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideParser;
import io.micronaut.guides.core.GuidesOption;
import io.micronaut.starter.api.TestFramework;
import io.micronaut.starter.options.BuildTool;
import io.micronaut.starter.options.Language;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class CreateAppMacroTest {
    @Inject
    CreateAppMacro createAppMacro;

    @Inject
    GuideParser guideParser;

    @Test
    void testSubstitute() {
        String content = "This is a test of the create-app macro\n" +
                "\n" +
                "create-app:default[]";

        String path = "src/test/resources";
        File file = new File(path);

        Optional<? extends Guide> metadata = guideParser.parseGuideMetadata(file, "metadata.json");
        String result = createAppMacro.substitute(content, metadata.get(), new GuidesOption(BuildTool.GRADLE, Language.JAVA, TestFramework.JUNIT));
        String expected = """
                This is a test of the create-app macro
                
                ++++
                <div id="tabs-doc1" class="ui-tabs ui-corner-all ui-widget ui-widget-content">
                    <ul role="tablist" class="ui-tabs-nav ui-corner-all ui-helper-reset ui-helper-clearfix ui-widget-header">
                        <li class="tabs-gradle ui-tabs-tab ui-corner-top ui-state-default ui-tabs-active ui-state-active" role="tab" tabindex="0" aria-controls="gradle" aria-labelledby="ui-id-1" aria-selected="true" aria-expanded="true"><a name="gradle" href="#gradle" tabindex="-1" class="ui-tabs-anchor" id="ui-id-1">Gradle</a></li>
                        <li class="tabs-maven ui-tabs-tab ui-corner-top ui-state-default" role="tab" tabindex="-1" aria-controls="maven" aria-labelledby="ui-id-2" aria-selected="false" aria-expanded="false"><a name="maven" href="#maven" tabindex="-1" class="ui-tabs-anchor" id="ui-id-2">Maven</a></li>
                    </ul>
                    <div id="gradle" aria-labelledby="ui-id-1" role="tabpanel" class="ui-tabs-panel ui-corner-bottom ui-widget-content" aria-hidden="false">
                        <code class="language-bash hljs" data-highlighted="yes"  style="white-space: pre-line;">gdk create-app  com.example.default \\
                 --services=database \\
                 --features=graalvm \\
                 --lang=java \\
                 --build=gradle \\
                </code>
                    </div>
                    <div id="maven" aria-labelledby="ui-id-2" role="tabpanel" class="ui-tabs-panel ui-corner-bottom ui-widget-content" aria-hidden="true" style="display: none;">
                        <code class="language-bash hljs" data-highlighted="yes"  style="white-space: pre-line;">gdk create-app  com.example.default \\
                 --services=database \\
                 --features=graalvm \\
                 --lang=java \\
                 --build=maven \\
                </code>
                    </div>
                </div>
                ++++
                """;
        assertEquals(expected, result);
    }

}
