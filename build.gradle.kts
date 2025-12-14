plugins {
    id("org.openrewrite.build.recipe-library-base") version "latest.release"

    // This uses the nexus publishing plugin to publish to the moderne-dev repository
    // Remove it if you prefer to publish by other means, such as the maven-publish plugin
    id("org.openrewrite.build.publish") version "latest.release"
    id("nebula.release") version "latest.release"

    // Configures artifact repositories used for dependency resolution to include maven central and nexus snapshots.
    // If you are operating in an environment where public repositories are not accessible, we recommend using a
    // virtual repository which mirrors both maven central and nexus snapshots.
    id("org.openrewrite.build.recipe-repositories") version "latest.release"
}

// Set as appropriate for your organization
group = "dev.mboegie.rewrite"
description = "A set of recipes to derive your release train's metro plan from your organizations reporsitries using OpenRewrite."

dependencies {
    // The bom version can also be set to a specific version
    // https://github.com/openrewrite/rewrite-recipe-bom/releases
    implementation(platform("org.openrewrite.recipe:rewrite-recipe-bom:latest.release"))

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

    // Need to have a slf4j binding to see any output enabled from the parser.
    runtimeOnly("ch.qos.logback:logback-classic:1.2.+")

    // needed for IntelliJ OpenRewrite plugin run action
    runtimeOnly("org.openrewrite.recipe:rewrite-rewrite")
}

signing {
    // To enable signing have your CI workflow set the "signingKey" and "signingPassword" Gradle project properties
    isRequired = false
}

// Use maven-style "SNAPSHOT" versioning for non-release builds
configure<nebula.plugin.release.git.base.ReleasePluginExtension> {
    defaultVersionStrategy = nebula.plugin.release.NetflixOssStrategies.SNAPSHOT(project)
}

configure<PublishingExtension> {
    publications {
        named("nebula", MavenPublication::class.java) {
            suppressPomMetadataWarningsFor("runtimeElements")
        }
    }
}

tasks.register("licenseFormat") {
    println("License format task not implemented for rewrite-recipe-starter")
}
