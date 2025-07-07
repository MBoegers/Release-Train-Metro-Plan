package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ProjectCoordinates;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.maven.Assertions.pomXml;

class FindMavenProjectIDsTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new FindMavenProjectIDs());
    }

    @DocumentExample
    @Test
    void singleModule() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class, rows -> {
              assertThat(rows).containsExactly(
                new ProjectCoordinates.Row("com.example", "simple-project")
              );
          }),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>simple-project</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }

    @Test
    void multiModule() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class, rows -> {
              assertThat(rows).containsExactlyInAnyOrder(
                new ProjectCoordinates.Row("com.example", "parent-project"),
                new ProjectCoordinates.Row("com.example", "module-a"),
                new ProjectCoordinates.Row("com.example", "module-b")
              );
          }),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>parent-project</artifactId>
                  <version>1.0.0</version>
                  <packaging>pom</packaging>
                  <modules>
                      <module>module-a</module>
                      <module>module-b</module>
                  </modules>
              </project>
              """
          ),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                      <groupId>com.example</groupId>
                      <artifactId>parent-project</artifactId>
                      <version>1.0.0</version>
                  </parent>
                  <artifactId>module-a</artifactId>
              </project>
              """,
            spec -> spec.path("module-a/pom.xml")
          ),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                      <groupId>com.example</groupId>
                      <artifactId>parent-project</artifactId>
                      <version>1.0.0</version>
                  </parent>
                  <artifactId>module-b</artifactId>
              </project>
              """,
            spec -> spec.path("module-b/pom.xml")
          )
        );
    }

    @Test
    void groupIdFromParent() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class, rows -> {
              assertThat(rows).containsExactly(
                new ProjectCoordinates.Row("org.springframework.boot", "my-spring-app")
              );
          }),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-parent</artifactId>
                      <version>3.2.0</version>
                      <relativePath/>
                  </parent>
                  <artifactId>my-spring-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }

    @Test
    void overwriteParentGroupId() {
        rewriteRun(
          spec -> spec.dataTable(ProjectCoordinates.Row.class, rows -> {
              assertThat(rows).containsExactly(
                new ProjectCoordinates.Row("com.mycompany", "my-spring-app")
              );
          }),
          pomXml(
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <parent>
                      <groupId>org.springframework.boot</groupId>
                      <artifactId>spring-boot-starter-parent</artifactId>
                      <version>3.2.0</version>
                      <relativePath/>
                  </parent>
                  <groupId>com.mycompany</groupId>
                  <artifactId>my-spring-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }
}