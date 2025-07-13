package dev.mboegie.rewrite.releasemetro;

import dev.mboegie.rewrite.releasemetro.table.ParentRelationships;
import org.junit.jupiter.api.Test;
import org.openrewrite.DocumentExample;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openrewrite.maven.Assertions.pomXml;

class FindMavenParentRelationshipsTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new FindMavenParentRelationships());
    }

    @Test
    @DocumentExample
    void childInheritsGroupIdFromParent() {
        rewriteRun(
          spec -> spec.dataTable(ParentRelationships.Row.class, rows -> {
              assertThat(rows).containsExactly(
                new ParentRelationships.Row(
                    "org.springframework.boot", "my-app",
                    "org.springframework.boot", "spring-boot-starter-parent", "3.2.0",
                    "MAVEN_PARENT"
                )
              );
          }),
          pomXml(
            //language=xml
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
                  <artifactId>my-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """,
            //language=xml
            """
              <!--~~(org.springframework.boot:my-app -> org.springframework.boot:spring-boot-starter-parent (MAVEN_PARENT))~~>--><?xml version="1.0" encoding="UTF-8"?>
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
                  <artifactId>my-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }

    @Test
    void noParentRelationship() {
        rewriteRun(
          pomXml(
            //language=xml
            """
              <?xml version="1.0" encoding="UTF-8"?>
              <project xmlns="http://maven.apache.org/POM/4.0.0"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                       xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
                  <modelVersion>4.0.0</modelVersion>
                  <groupId>com.example</groupId>
                  <artifactId>standalone-project</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }

    @Test
    void differentGroupIdFromParent() {
        rewriteRun(
          spec -> spec.dataTable(ParentRelationships.Row.class, rows -> {
              assertThat(rows).containsExactly(
                new ParentRelationships.Row(
                    "com.mycompany", "custom-app",
                    "org.springframework.boot", "spring-boot-starter-parent", "3.2.0",
                    "MAVEN_PARENT"
                )
              );
          }),
          pomXml(
            //language=xml
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
                  <artifactId>custom-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """,
            //language=xml
            """
              <!--~~(com.mycompany:custom-app -> org.springframework.boot:spring-boot-starter-parent (MAVEN_PARENT))~~>--><?xml version="1.0" encoding="UTF-8"?>
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
                  <artifactId>custom-app</artifactId>
                  <version>1.0.0</version>
              </project>
              """
          )
        );
    }
}