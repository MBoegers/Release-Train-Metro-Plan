package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ProjectCoordinates;
import org.jspecify.annotations.Nullable;
import org.openrewrite.*;
import org.openrewrite.gradle.IsBuildGradle;
import org.openrewrite.gradle.marker.GradleProject;

import java.util.Optional;

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
                    public @Nullable Tree visit(@Nullable Tree tree, ExecutionContext executionContext) {
                        if (tree == null) {
                            return null;
                        }

                        Optional<GradleProject> gp = tree.getMarkers().findFirst(GradleProject.class);
                        gp.map(GradleProject::getGroup).ifPresent(group -> {
                            String artifactId = gp.map(GradleProject::getName).orElse("none");
                            projectCoordinatess.insertRow(executionContext, new ProjectCoordinates.Row(group, artifactId));
                        });

                        return tree;
                    }
                });
    }
}