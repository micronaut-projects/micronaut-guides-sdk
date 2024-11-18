/*
 * Copyright 2017-2024 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.guides.core;

import io.micronaut.context.exceptions.ConfigurationException;
import jakarta.inject.Singleton;
import java.util.stream.Collectors;

@Singleton
public class CliMacroSubstitution extends PlaceholderWithTargetMacroSubstitution {
    private static final String CLI_MESSAGING = "create-messaging-app";
    private static final String CLI_DEFAULT = "create-app";
    private static final String CLI_GRPC = "create-grpc-app";
    private static final String CLI_FUNCTION = "create-function-app";
    private static final String CLI_CLI = "create-cli-app";

    private static String cliCommandForApp(App app) {
        return switch (app.applicationType()) {
            case CLI -> CLI_CLI;
            case FUNCTION -> CLI_FUNCTION;
            case GRPC -> CLI_GRPC;
            case MESSAGING -> CLI_MESSAGING;
            case DEFAULT -> CLI_DEFAULT;
            default -> throw new IllegalArgumentException("Unknown application type: " + app.applicationType());
        };
    }

    @Override
    protected String getMacroName() {
        return "cli-command";
    }

    @Override
    protected String getSubstitution(Guide guide, GuidesOption option, String appName) {
        App app = guide.apps().stream()
                .filter(a -> a.name().equals(appName))
                .findFirst()
                .orElse(null);
        if (app != null) {
            return cliCommandForApp(app);
        } else {
            throw new ConfigurationException("No CLI command found for app: " + app + " -- should be one of " + guide.apps().stream().map(el -> "@" + el + ":cli-command@").collect(Collectors.joining(", ")));
        }
    }
}
