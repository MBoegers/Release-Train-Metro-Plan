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

import dev.mboegie.rewrite.releasemetro.table.ProjectCoordinates;
import org.jspecify.annotations.Nullable;
import org.openrewrite.*;
import org.openrewrite.gradle.IsBuildGradle;
import org.openrewrite.gradle.marker.GradleProject;
import org.openrewrite.marker.SearchResult;

public class FindGradleProjectIDs extends Recipe {

    public transient ProjectCoordinates projectCoordinatess = new ProjectCoordinates(this);

    @Override
    public String getDisplayName() {
        return "Find Gradle project IDs";
    }

    @Override
    public String getDescription() {
        return "Find Gradle project IDs in build.gradle files to determine the project ID.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return Preconditions.check(Preconditions.or(new IsBuildGradle<>()),
                new TreeVisitor<Tree, ExecutionContext>() {
                    @Override
                    public @Nullable Tree visit(@Nullable Tree tree, ExecutionContext ctx) {
                        if (tree == null) {
                            return null;
                        }

                        return tree.getMarkers()
                                .findFirst(GradleProject.class)
                                .map(gp -> {
                                    ProjectCoordinates.Row row = new ProjectCoordinates.Row(gp.getGroup(), gp.getName());
                                    projectCoordinatess.insertRow(ctx, row);
                                    return SearchResult.found(tree, row.toString());
                                }).orElse(tree);
                    }
                });
    }
}
