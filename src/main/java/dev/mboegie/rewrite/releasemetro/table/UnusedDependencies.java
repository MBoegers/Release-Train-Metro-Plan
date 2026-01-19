/*
 * Copyright 2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.mboegie.rewrite.releasemetro.table;

import lombok.Value;
import org.jspecify.annotations.Nullable;
import org.openrewrite.Column;
import org.openrewrite.DataTable;
import org.openrewrite.Recipe;

public class UnusedDependencies extends DataTable<UnusedDependencies.Row> {

    public UnusedDependencies(Recipe recipe) {
        super(recipe,
                "Potentially Unused Dependencies",
                "Dependencies that are declared in build files but may not be used based on import analysis.");
    }

    @Value
    public static class Row {
        @Column(displayName = "dependencyGroupId",
                description = "Group ID of the potentially unused dependency")
        @Nullable
        String dependencyGroupId;

        @Column(displayName = "dependencyArtifactId",
                description = "Artifact ID of the potentially unused dependency")
        String dependencyArtifactId;

        @Column(displayName = "dependencyVersion",
                description = "Version of the potentially unused dependency")
        @Nullable
        String dependencyVersion;

        @Column(displayName = "dependencyScope",
                description = "Scope of the dependency (compile, test, etc.)")
        @Nullable
        String dependencyScope;

        @Column(displayName = "isDirect",
                description = "Whether this is a direct dependency (not transitive)")
        Boolean isDirect;

        @Column(displayName = "reasonSuspected",
                description = "Reason why this dependency is suspected to be unused")
        String reasonSuspected;

        @Override
        public String toString() {
            return String.format("%s:%s:%s (%s) - %s",
                    dependencyGroupId != null ? dependencyGroupId : "",
                    dependencyArtifactId,
                    dependencyVersion != null ? dependencyVersion : "",
                    isDirect ? "direct" : "transitive",
                    reasonSuspected);
        }
    }
}
