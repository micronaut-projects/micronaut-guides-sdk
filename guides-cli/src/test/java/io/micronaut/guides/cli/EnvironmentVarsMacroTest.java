package io.micronaut.guides.cli;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class EnvironmentVarsMacroTest {
    @Inject
    EnvironmentVarsMacro environmentVarsMacro;


    @Test
    void testSubstitute() {
        String content = "environment-vars:[DATASOURCES_DEFAULT_URL=jdbc:mysql://localhost:3306/db,DATASOURCES_DEFAULT_USERNAME=sherlock,DATASOURCES_DEFAULT_PASSWORD=elementary]\n";

        String path = "src/test/resources";
        File file = new File(path);

        String result = environmentVarsMacro.substitute(content, null, null);
        String expected = """
                ++++
                <div class="tabs-doc ui-tabs ui-corner-all ui-widget ui-widget-content" data-name="system">
                    <div data-value="linux" data-label="Linux &amp; macOS" id="linux" aria-labelledby="ui-id-11" role="tabpanel" class="ui-tabs-panel ui-corner-bottom ui-widget-content" aria-hidden="false" style="">
                      <pre><code class="language-bash hljs" data-highlighted="yes"><span class="hljs-built_in">export</span> DATASOURCES_DEFAULT_URL=jdbc:mysql://localhost:3306/db
                <span class="hljs-built_in">export</span> DATASOURCES_DEFAULT_USERNAME=sherlock
                <span class="hljs-built_in">export</span> DATASOURCES_DEFAULT_PASSWORD=elementary
                </code><button>Copy</button></pre>
                    </div>
                    <div data-value="windows" data-label="Windows" id="windows" aria-labelledby="ui-id-12" role="tabpanel" class="ui-tabs-panel ui-corner-bottom ui-widget-content" aria-hidden="true" style="display: none;">
                      <pre><code class="language-bash hljs" data-highlighted="yes"><span class="hljs-built_in">set</span> DATASOURCES_DEFAULT_URL=jdbc:mysql://localhost:3306/db
                <span class="hljs-built_in">set</span> DATASOURCES_DEFAULT_USERNAME=sherlock
                <span class="hljs-built_in">set</span> DATASOURCES_DEFAULT_PASSWORD=elementary
                </code><button>Copy</button></pre>
                    </div>
                    <div data-value="windows-powershell" data-label="Windows PowerShell" id="windows-powershell" aria-labelledby="ui-id-13" role="tabpanel" class="ui-tabs-panel ui-corner-bottom ui-widget-content" aria-hidden="true" style="display: none;">
                      <pre><code class="language-bash hljs" data-highlighted="yes"><span class="hljs-variable">$ENV</span> DATASOURCES_DEFAULT_URL = <span class="hljs-string">"jdbc:mysql://localhost:3306/db"</span>
                <span class="hljs-variable">$ENV</span> DATASOURCES_DEFAULT_USERNAME = <span class="hljs-string">"sherlock"</span>
                <span class="hljs-variable">$ENV</span> DATASOURCES_DEFAULT_PASSWORD = <span class="hljs-string">"elementary"</span>
                </code><button>Copy</button></pre>
                    </div>
                </div>
                ++++
                
                """;
        assertEquals(expected, result);
    }
}
