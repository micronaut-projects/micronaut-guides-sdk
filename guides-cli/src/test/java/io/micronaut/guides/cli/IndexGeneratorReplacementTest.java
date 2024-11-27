package io.micronaut.guides.cli;

import io.micronaut.guides.core.Guide;
import io.micronaut.guides.core.GuideParser;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@MicronautTest(startApplication = false)
public class IndexGeneratorReplacementTest {
    @Inject
    IndexGeneratorReplacement indexGeneratorReplacement;

    @Inject
    GuideParser guideParser;

    @Test
    void test() throws IOException {
        String path = "src/test/resources";
        File file = new File(path);

        Optional<? extends Guide> metadata = guideParser.parseGuideMetadata(file, "metadata.json");
        List<? extends Guide> metadataList = List.of(metadata.get());

        String result = indexGeneratorReplacement.renderIndex(metadataList);
        File expectedFile = new File("src/test/resources/index.html");

        String expected = Files.readString(expectedFile.toPath());
        assertEquals(expected, result);
    }
}
