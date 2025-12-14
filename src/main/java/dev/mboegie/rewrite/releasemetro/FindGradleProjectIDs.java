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
