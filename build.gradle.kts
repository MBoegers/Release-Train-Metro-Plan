import java.util.Calendar

plugins {
    id("org.openrewrite.build.recipe-library-base") version "latest.release"

    // Configures artifact repositories used for dependency resolution to include maven central and nexus snapshots.
    // If you are operating in an environment where public repositories are not accessible, we recommend using a
    // virtual repository which mirrors both maven central and nexus snapshots.
    id("org.openrewrite.build.recipe-repositories") version "latest.release"

    // License header management
    id("com.github.hierynomus.license") version "0.16.1"

    // Maven Central publishing
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
}

// Set as appropriate for your organization
group = "dev.mboegie.rewrite"
description = "A set of recipes to derive your release train's metro plan from your organizations repositories using OpenRewrite."
version = findProperty("version")!!

// Required for Maven Central: sources and javadoc JARs
java {
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    // The bom version can also be set to a specific version
    // https://github.com/openrewrite/rewrite-recipe-bom/releases
    implementation(platform("org.openrewrite.recipe:rewrite-recipe-bom:latest.release"))

    implementation("org.openrewrite:rewrite-java")
    implementation("org.assertj:assertj-core:latest.release")

    // to have access to org.openrewrite.java.dependencies.DependencyInsight
    implementation("org.openrewrite.recipe:rewrite-java-dependencies")

    // for dev.mboegie.rewrite.releasemetro.FindMavenProjectIDs
    implementation("org.openrewrite:rewrite-xml")
    implementation("org.openrewrite:rewrite-maven")

    // for dev.mboegie.rewrite.releasemetro.FindGradleProjectIDs
    implementation("org.openrewrite:rewrite-groovy")
    implementation("org.openrewrite:rewrite-gradle")
    testImplementation("org.openrewrite.gradle.tooling:model:latest.release")
    testImplementation("org.gradle:gradle-tooling-api:latest.release")

    // The RewriteTest class needed for testing recipes
    testImplementation("org.openrewrite:rewrite-test")

    // Support for parsing Java 25 (backward compatible with 21 and earlier)
    testRuntimeOnly("org.openrewrite:rewrite-java-25")

    // Need to have a slf4j binding to see any output enabled from the parser.
    runtimeOnly("ch.qos.logback:logback-classic:1.5.+")

    // needed for IntelliJ OpenRewrite plugin run action
    runtimeOnly("org.openrewrite.recipe:rewrite-rewrite")
}

// Maven Central Publishing Configuration
publishing {
    publications {
        create<MavenPublication>("nebula") {
            from(components["java"])

            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set("Release Train Metro Plan")
                description.set(project.description)
                url.set("https://github.com/MBoegers/Release-Train-Metro-Plan")
                inceptionYear.set("2024")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("MBoegers")
                        name.set("Merlin Bögershausen")
                        url.set("https://mboegie.dev/")
                    }
                }

                scm {
                    connection.set("scm:git:https://github.com/MBoegers/Release-Train-Metro-Plan.git")
                    developerConnection.set("scm:git:git@github.com:MBoegers/Release-Train-Metro-Plan.git")
                    url.set("https://github.com/MBoegers/Release-Train-Metro-Plan")
                }

                issueManagement {
                    system.set("GitHub Issues")
                    url.set("https://github.com/MBoegers/Release-Train-Metro-Plan/issues")
                }
            }
        }
    }
}

// Nexus Publishing to Maven Central
nexusPublishing {
    repositories {
        sonatype {
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
        }
    }
}

// GPG Signing Configuration
signing {
    // Enable signing only when publishing (not for local builds)
    isRequired = gradle.taskGraph.hasTask("publishToSonatype")

    // Use in-memory signing key from environment variables (for CI)
    val signingKey: String? by project
    val signingPassword: String? by project
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
    }

    sign(publishing.publications["nebula"])
}

// Ensure Javadoc doesn't fail the build on warnings
tasks.withType<Javadoc> {
    options {
        (this as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }
}

// License header configuration
license {
    header = file("gradle/licenseHeader.txt")
    ext["year"] = Calendar.getInstance().get(Calendar.YEAR)
    mapping("java", "SLASHSTAR_STYLE")
    strictCheck = true
    exclude("**/package-info.java")
}
