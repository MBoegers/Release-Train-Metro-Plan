package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ParentRelationships;
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
public class FindMavenParentRelationships extends Recipe {

    public transient ParentRelationships parentRelationships = new ParentRelationships(this);

    @Override
    public String getDisplayName() {
        return "Find Maven parent relationships";
    }

    @Override
    public String getDescription() {
        return "Find Maven parent POM relationships to understand project hierarchies in multi-module builds.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return Preconditions.check(
                Preconditions.and(new FindMavenProject().getVisitor(), new FindSourceFiles("**/pom.xml").getVisitor()),
                new XmlIsoVisitor<ExecutionContext>() {
                    @Override
                    public Xml.Document visitDocument(Xml.Document document, ExecutionContext ctx) {
                        Optional<String> childGroupId = MavenPomExtractor.findGroupId(document);
                        Optional<String> childArtifactId = MavenPomExtractor.findArtifactId(document);
                        
                        Optional<String> parentGroupId = MavenPomExtractor.findParentGroupId(document);
                        Optional<String> parentArtifactId = MavenPomExtractor.findParentArtifactId(document);
                        Optional<String> parentVersion = MavenPomExtractor.findParentVersion(document);

                        if (childArtifactId.isPresent() && parentArtifactId.isPresent()) {
                            ParentRelationships.Row row = new ParentRelationships.Row(
                                    childGroupId.orElse(null),
                                    childArtifactId.get(),
                                    parentGroupId.orElse(null),
                                    parentArtifactId.get(),
                                    parentVersion.orElse(null),
                                    "MAVEN_PARENT"
                            );
                            parentRelationships.insertRow(ctx, row);
                            return SearchResult.found(document, row.toString());
                        }

                        return document;
                    }

                });
    }
}