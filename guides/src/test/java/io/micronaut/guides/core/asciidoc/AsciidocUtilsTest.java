package io.micronaut.guides.core.asciidoc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsciidocUtilsTest {

    @Test
    void passthroughBlock() {
        String content = """
                <p>
                Content in a passthrough block is passed to the output unprocessed.
                That means you can include raw HTML, like this embedded Gist:
                </p>
                
                <script src="https://gist.github.com/mojavelinux/5333524.js">
                </script>""";
        String expected = """
                ++++
                <p>
                Content in a passthrough block is passed to the output unprocessed.
                That means you can include raw HTML, like this embedded Gist:
                </p>
                
                <script src="https://gist.github.com/mojavelinux/5333524.js">
                </script>
                ++++
                """;
        assertEquals(expected, AsciidocUtils.passthroughBlock(content));
    }
}