package dev.mboegie.rewrite.releasemetro.table;

import lombok.Value;
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
        String groupId;
        @Column(displayName = "artifactId",
                description = "Artifact ID of the current module/subproject")
        String artifactId;
    }
}