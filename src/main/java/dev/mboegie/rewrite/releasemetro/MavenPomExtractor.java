package dev.mboegie.rewrite.releasemetro;

import org.openrewrite.xml.search.FindTags;
import org.openrewrite.xml.tree.Xml;

import java.util.Optional;

public class MavenPomExtractor {

    public static Optional<String> findGroupId(Xml.Document document) {
        return FindTags.find(document, "/project/groupId")
                .stream()
                .findFirst()
                .flatMap(tag -> tag.getValue().map(String::trim))
                .filter(s -> !s.isEmpty())
                .map(Optional::of)
                .orElse(findParentGroupId(document)); // Fallback to parent groupId
    }

    public static Optional<String> findArtifactId(Xml.Document document) {
        return FindTags.find(document, "/project/artifactId")
                .stream()
                .findFirst()
                .flatMap(tag -> tag.getValue().map(String::trim))
                .filter(s -> !s.isEmpty());
    }

    public static Optional<String> findParentGroupId(Xml.Document document) {
        return FindTags.find(document, "/project/parent/groupId")
                .stream()
                .findFirst()
                .flatMap(tag -> tag.getValue().map(String::trim))
                .filter(s -> !s.isEmpty());
    }

    public static Optional<String> findParentArtifactId(Xml.Document document) {
        return FindTags.find(document, "/project/parent/artifactId")
                .stream()
                .findFirst()
                .flatMap(tag -> tag.getValue().map(String::trim))
                .filter(s -> !s.isEmpty());
    }

    public static Optional<String> findParentVersion(Xml.Document document) {
        return FindTags.find(document, "/project/parent/version")
                .stream()
                .findFirst()
                .flatMap(tag -> tag.getValue().map(String::trim))
                .filter(s -> !s.isEmpty());
    }
}
