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

public class ParentRelationships extends DataTable<ParentRelationships.Row> {

    public ParentRelationships(Recipe recipe) {
        super(recipe,
                "Maven Parent and Gradle Project Hierarchies",
                "Relationships between child projects and their parent POMs or Gradle parent projects.");
    }

    @Value
    public static class Row {
        @Column(displayName = "childGroupId",
                description = "Group ID of the child project")
        @Nullable
        String childGroupId;

        @Column(displayName = "childArtifactId",
                description = "Artifact ID of the child project")
        String childArtifactId;

        @Column(displayName = "parentGroupId",
                description = "Group ID of the parent project")
        @Nullable
        String parentGroupId;

        @Column(displayName = "parentArtifactId",
                description = "Artifact ID of the parent project")
        String parentArtifactId;

        @Column(displayName = "parentVersion",
                description = "Version of the parent project")
        @Nullable
        String parentVersion;

        @Column(displayName = "hierarchyType",
                description = "Type of hierarchy relationship (MAVEN_PARENT or GRADLE_PARENT)")
        String hierarchyType;

        @Override
        public String toString() {
            return String.format("%s:%s -> %s:%s (%s)",
                    childGroupId != null ? childGroupId : "",
                    childArtifactId,
                    parentGroupId != null ? parentGroupId : "",
                    parentArtifactId,
                    hierarchyType);
        }
    }
}
