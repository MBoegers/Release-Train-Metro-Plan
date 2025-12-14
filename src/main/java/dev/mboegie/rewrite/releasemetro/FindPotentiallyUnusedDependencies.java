package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.UnusedDependencies;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.tree.J;
import org.openrewrite.marker.SearchResult;

import java.util.HashSet;
import java.util.Set;

@Value
@EqualsAndHashCode(callSuper = false)
public class FindPotentiallyUnusedDependencies extends Recipe {

    public transient UnusedDependencies unusedDependencies = new UnusedDependencies(this);

    @Override
    public String getDisplayName() {
        return "Find potentially unused dependencies";
    }

    @Override
    public String getDescription() {
        return "Collects import information to help identify potentially unused dependencies.";
    }

    @Override
    public TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            private final Set<String> usedPackages = new HashSet<>();

            @Override
            public J.CompilationUnit visitCompilationUnit(J.CompilationUnit cu, ExecutionContext ctx) {
                // Clear for each compilation unit
                usedPackages.clear();

                // Visit imports first
                cu = (J.CompilationUnit) super.visitCompilationUnit(cu, ctx);

                // Log findings for this compilation unit
                if (!usedPackages.isEmpty()) {
                    String packageList = String.join(", ", usedPackages);
                    return SearchResult.found(cu, "Uses packages: " + packageList);
                }

                return cu;
            }

            @Override
            public J.Import visitImport(J.Import _import, ExecutionContext ctx) {
                String fullImport = _import.getPackageName();
                String packageName = extractMeaningfulPackage(fullImport);

                if (packageName != null) {
                    usedPackages.add(packageName);

                    // Create a data table entry for this import usage
                    UnusedDependencies.Row row = new UnusedDependencies.Row(
                            extractGroupIdFromPackage(packageName),
                            extractArtifactIdFromPackage(packageName),
                            null, // version unknown from imports
                            "unknown", // scope unknown from imports
                            false, // not analyzing direct vs transitive from imports
                            "Import found: " + fullImport
                    );
                    unusedDependencies.insertRow(ctx, row);
                }

                return _import;
            }

            private String extractMeaningfulPackage(String fullImport) {
                if (fullImport == null) {
                    return null;
                }

                // Extract meaningful package prefixes for common libraries
                if (fullImport.startsWith("org.springframework")) {
                    return "org.springframework";
                }
                if (fullImport.startsWith("org.junit")) {
                    return "org.junit";
                }
                if (fullImport.startsWith("org.slf4j")) {
                    return "org.slf4j";
                }
                if (fullImport.startsWith("org.apache.commons")) {
                    return "org.apache.commons";
                }
                if (fullImport.startsWith("org.apache.maven")) {
                    return "org.apache.maven";
                }
                if (fullImport.startsWith("com.fasterxml.jackson")) {
                    return "com.fasterxml.jackson";
                }
                if (fullImport.startsWith("io.micrometer")) {
                    return "io.micrometer";
                }
                if (fullImport.startsWith("org.openrewrite")) {
                    return "org.openrewrite";
                }

                // For other packages, take first two segments as a heuristic
                String[] parts = fullImport.split("\\.");
                if (parts.length >= 2) {
                    return parts[0] + "." + parts[1];
                }
                return fullImport;
            }

            private String extractGroupIdFromPackage(String packageName) {
                // Simple heuristic: package name often matches groupId
                return packageName;
            }

            private String extractArtifactIdFromPackage(String packageName) {
                // Simple heuristic: last part of package might indicate artifact
                String[] parts = packageName.split("\\.");
                if (parts.length > 1) {
                    return parts[parts.length - 1];
                }
                return packageName;
            }
        };
    }
}
