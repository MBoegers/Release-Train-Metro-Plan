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
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.*;
import org.openrewrite.marker.SearchResult;
import org.openrewrite.maven.search.FindMavenProject;
import org.openrewrite.xml.XmlIsoVisitor;
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
                        Optional<String> groupId = MavenPomExtractor.findGroupId(document);
                        Optional<String> artifactId = MavenPomExtractor.findArtifactId(document);

                        if (groupId.isPresent() && artifactId.isPresent()) {
                            ProjectCoordinates.Row row = new ProjectCoordinates.Row(groupId.get(), artifactId.get());
                            projectCoordinatess.insertRow(ctx,
                                    row);
                            return SearchResult.found(document, row.toString());
                        }

                        return document;
                    }

                });
    }
}
