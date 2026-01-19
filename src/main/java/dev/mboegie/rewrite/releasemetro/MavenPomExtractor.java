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
