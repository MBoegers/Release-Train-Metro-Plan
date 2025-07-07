package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ProjectCoordinates;
import org.junit.jupiter.api.Test;
import org.openrewrite.gradle.toolingapi.Assertions;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.gradle.Assertions.buildGradleKts;
import static org.openrewrite.gradle.Assertions.settingsGradleKts;

class FindGradleProjectIDsTest implements RewriteTest {
    @Override
    public void defaults(RecipeSpec spec) {
        spec.beforeRecipe(Assertions.withToolingApi()).recipe(new FindGradleProjectIDs());
    }

    @Test
    void simpleProject() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class,
            rows -> assertThat(rows).containsExactly(
              new ProjectCoordinates.Row("org.openrewrite.recipe", "rewrite-testing-frameworks"))),
          buildGradleKts(
            // language=groovy
            """
              plugins {
                  id("java")
              }
              
              group = "org.openrewrite.recipe"
              
              dependencies {
                  implementation("org.springframework:spring-core:5.3.21")
                  testImplementation ("org.junit.jupiter:junit-jupiter:5.8.2")
              }
              """),
          settingsGradleKts(
            // language=groovy
            """
              rootProject.name = "rewrite-testing-frameworks"
              """
          ));
    }

    @Test
    void multiModule() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class,
            rows -> assertThat(rows).containsExactlyInAnyOrder(
              new ProjectCoordinates.Row("org.openrewrite", "rewrite"),
              new ProjectCoordinates.Row("org.openrewrite", "rewrite-java"),
              new ProjectCoordinates.Row("org.openrewrite", "rewrite-gradle"))),
          buildGradleKts(
            // language=kotlin
            """
              allprojects {
                  group = "org.openrewrite"
                  description = "Eliminate tech-debt. Automatically."
              }
              """),
          settingsGradleKts(
            // language=kotlin
            """
              rootProject.name = "rewrite"
              val allProjects = listOf("rewrite-gradle", "rewrite-java")
              include(*allProjects.toTypedArray())
              """
          ),
          buildGradleKts(
            // language=kotlin
            """
              plugins {
                  id("java")
              }
              
              dependencies {
                  implementation("org.openrewrite:rewrite-core:latest.release")
              }
              """,
            sourceSpecs -> sourceSpecs.path("rewrite-gradle/build.gradle")
          ),
          buildGradleKts(
            // language=kotlin
            """
              plugins {
                  id("java")
              }
              
              dependencies {
                  implementation("org.openrewrite:rewrite-core:latest.release")
              }
              """,
            sourceSpecs -> sourceSpecs.path("rewrite-java/build.gradle")
          )
        );
    }
}
