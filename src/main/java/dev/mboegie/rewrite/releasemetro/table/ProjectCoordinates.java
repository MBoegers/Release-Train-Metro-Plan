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

public class ProjectCoordinates extends DataTable<ProjectCoordinates.Row> {

    public ProjectCoordinates(Recipe recipe) {
        super(recipe,
                "Maven or Gradle Artifact coordinates IDs",
                "Maven Modules or Gradle (sub-)project groupId and artifactId.");
    }

    @Value
    public static class Row {
        @Column(displayName = "groupId",
                description = "Group ID of the current module/subproject")
        @Nullable
        String groupId;

        @Column(displayName = "artifactId",
                description = "Artifact ID of the current module/subproject")
        String artifactId;

        @Override
        public String toString() {
            return (groupId != null? groupId : "") + ":" + artifactId;
        }
    }
}
