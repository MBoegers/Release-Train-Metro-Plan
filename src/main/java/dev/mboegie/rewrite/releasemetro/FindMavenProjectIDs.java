package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ProjectCoordinates;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.*;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.search.FindMavenProject;
import org.openrewrite.xml.XmlIsoVisitor;
import org.openrewrite.xml.search.FindTags;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;

@Value
@EqualsAndHashCode(callSuper = false)
public class FindMavenProjectIDs extends Recipe {

    public transient ProjectCoordinates projectCoordinatess = new ProjectCoordinates(this);

    @Override
    public String getDisplayName() {
        return "Find maven project IDs";
    }

    @Override
    public String getDescription() {
        return "Find Maven group Id and artifactId in pom.xml files to determine the project ID.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return Preconditions.check(
                Preconditions.and(new FindMavenProject().getVisitor(), new FindSourceFiles("**/pom.xml").getVisitor()),
                new XmlIsoVisitor<ExecutionContext>() {
                    @Override
                    public Xml.Document visitDocument(Xml.Document document, ExecutionContext ctx) {
                        Optional<String> groupId = findGroupId(document);
                        Optional<String> artifactId = findArtifactId(document);

                        if (groupId.isPresent() && artifactId.isPresent()) {
                            ProjectCoordinates.Row row = new ProjectCoordinates.Row(groupId.get(), artifactId.get());
                            projectCoordinatess.insertRow(ctx,
                                    row);
                            return SearchResult.found(document, row.toString());
                        }

                        return document;
                    }

                    private Optional<String> findArtifactId(Xml.Document document) {
                        Xml.Tag maybeArtifactId = FindTags.findSingle(document, "/project/artifactId");
                        if (maybeArtifactId != null) {
                            return maybeArtifactId.getValue();
                        }

                        return Optional.empty();
                    }

                    private Optional<String> findGroupId(Xml.Document document) {
                        Xml.Tag maybeGroupId = FindTags.findSingle(document, "/project/groupId");
                        if (maybeGroupId != null && maybeGroupId.getValue().isPresent()) {
                            return maybeGroupId.getValue();
                        }

                        Xml.Tag maybeParentGroupId = FindTags.findSingle(document, "/project/parent/groupId");
                        if (maybeParentGroupId != null && maybeParentGroupId.getValue().isPresent()) {
                            return maybeParentGroupId.getValue();
                        }

                        return Optional.empty();
                    }
                });
    }
}
