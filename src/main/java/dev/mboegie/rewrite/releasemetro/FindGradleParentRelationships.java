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
package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ParentRelationships;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jspecify.annotations.Nullable;
import org.openrewrite.*;
import org.openrewrite.gradle.IsBuildGradle;
import org.openrewrite.gradle.marker.GradleProject;
import org.openrewrite.marker.SearchResult;

@Value
@EqualsAndHashCode(callSuper = false)
public class FindGradleParentRelationships extends Recipe {

    public transient ParentRelationships parentRelationships = new ParentRelationships(this);

    @Override
    public String getDisplayName() {
        return "Find Gradle project hierarchy relationships";
    }

    @Override
    public String getDescription() {
        return "Find Gradle parent-child project relationships in multi-project builds to understand project hierarchies.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return Preconditions.check(Preconditions.or(new IsBuildGradle<>()),
                new TreeVisitor<Tree, ExecutionContext>() {
                    private final java.util.Map<String, GradleProject> allProjects = new java.util.HashMap<>();

                    @Override
                    public @Nullable Tree visit(@Nullable Tree tree, ExecutionContext ctx) {
                        if (tree == null) {
                            return null;
                        }

                        return tree.getMarkers()
                                .findFirst(GradleProject.class)
                                .map(gradleProject -> {
                                    String projectPath = gradleProject.getPath();
                                    allProjects.put(projectPath, gradleProject);

                                    // Determine parent from project path
                                    String parentPath = determineParentPath(projectPath);
                                    if (parentPath != null) {
                                        // Check if we already have the parent project
                                        GradleProject parent = allProjects.get(parentPath);
                                        if (parent != null) {
                                            ParentRelationships.Row row = new ParentRelationships.Row(
                                                    gradleProject.getGroup(),
                                                    gradleProject.getName(),
                                                    parent.getGroup(),
                                                    parent.getName(),
                                                    parent.getVersion(),
                                                    "GRADLE_PARENT"
                                            );

                                            parentRelationships.insertRow(ctx, row);
                                            return SearchResult.found(tree, row.toString());
                                        }
                                    }
                                    // Always mark as found to indicate processing
                                    return SearchResult.found(tree, gradleProject.getGroup() + ":" + gradleProject.getName());
                                }).orElse(tree);
                    }

                    private String determineParentPath(String projectPath) {
                        if (":".equals(projectPath)) {
                            return null; // Root project has no parent
                        }

                        int lastColon = projectPath.lastIndexOf(':');
                        if (lastColon > 0) {
                            return projectPath.substring(0, lastColon);
                        }
                        return ":"; // Direct child of root project
                    }
                });
    }
}
