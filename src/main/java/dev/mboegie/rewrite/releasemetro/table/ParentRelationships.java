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
