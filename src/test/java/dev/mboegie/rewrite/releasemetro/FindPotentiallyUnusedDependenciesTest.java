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

import dev.mboegie.rewrite.releasemetro.table.UnusedDependencies;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.java.Assertions.java;

class FindPotentiallyUnusedDependenciesTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new FindPotentiallyUnusedDependencies());
    }

    @DocumentExample
    @Test
    void capturesSpringFrameworkImports() {
        rewriteRun(
          spec -> spec.dataTable(UnusedDependencies.Row.class, rows -> {
              assertThat(rows).anySatisfy(row -> {
                  assertThat(row.getDependencyGroupId()).isEqualTo("org.springframework");
                  assertThat(row.getReasonSuspected()).contains("Import found:");
              });
          }),
          java(
            """
              package com.example;
              
              import org.springframework.context.ApplicationContext;
              import org.springframework.boot.SpringApplication;
              import java.util.List;
              
              public class MyApp {
                  // Simple class without method calls to avoid type resolution issues
              }
              """,
            """
              /*~~(Uses packages: java.util, org.springframework)~~>*/package com.example;
              
              import org.springframework.context.ApplicationContext;
              import org.springframework.boot.SpringApplication;
              import java.util.List;
              
              public class MyApp {
                  // Simple class without method calls to avoid type resolution issues
              }
              """
          )
        );
    }

    @Test
    void capturesMultipleLibraryImports() {
        rewriteRun(
          spec -> spec.dataTable(UnusedDependencies.Row.class, rows -> {
              assertThat(rows).hasSizeGreaterThanOrEqualTo(3); // At least org.springframework, org.junit, org.slf4j
              assertThat(rows).extracting(UnusedDependencies.Row::getDependencyGroupId)
                      .contains("org.springframework", "org.junit", "org.slf4j");
          }),
          java(
            """
              package com.example;
              
              import org.springframework.context.ApplicationContext;
              import org.junit.jupiter.api.Test;
              import org.slf4j.Logger;
              import org.slf4j.LoggerFactory;
              
              public class TestClass {
                  // Simple class structure
              }
              """,
            """
              /*~~(Uses packages: org.slf4j, org.springframework, org.junit)~~>*/package com.example;
              
              import org.springframework.context.ApplicationContext;
              import org.junit.jupiter.api.Test;
              import org.slf4j.Logger;
              import org.slf4j.LoggerFactory;
              
              public class TestClass {
                  // Simple class structure
              }
              """
          )
        );
    }

    @Test
    void noImportsNoDataTableEntries() {
        rewriteRun(
          java(
            """
              package com.example;
              
              public class SimpleClass {
                  public void doSomething() {
                      System.out.println("Hello World");
                  }
              }
              """
          )
        );
    }
}
