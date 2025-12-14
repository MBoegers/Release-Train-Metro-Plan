package dev.mboegie.rewrite.releasemetro;

import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.gradle.Assertions.buildGradleKts;
import static org.openrewrite.gradle.Assertions.settingsGradleKts;
import static org.openrewrite.gradle.toolingapi.Assertions.withToolingApi;

class FindGradleParentRelationshipsTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.beforeRecipe(withToolingApi()).recipe(new FindGradleParentRelationships());
    }

    @DocumentExample
    @Test
    void singleProjectNoParent() {
        rewriteRun(
          buildGradleKts(
            //language=kotlin
            """
              plugins {
                  id("java")
              }
              
              group = "org.openrewrite.recipe"
              
              dependencies {
                  implementation("org.springframework:spring-core:5.3.21")
                  testImplementation ("org.junit.jupiter:junit-jupiter:5.8.2")
              }
              """,
            //language=kotlin
            """
              /*~~(org.openrewrite.recipe:rewrite-testing-frameworks)~~>*/plugins {
                  id("java")
              }
              
              group = "org.openrewrite.recipe"
              
              dependencies {
                  implementation("org.springframework:spring-core:5.3.21")
                  testImplementation ("org.junit.jupiter:junit-jupiter:5.8.2")
              }
              """
          ),
          settingsGradleKts(
            //language=kotlin
            """
              rootProject.name = "rewrite-testing-frameworks"
              """
          )
        );
    }
}
