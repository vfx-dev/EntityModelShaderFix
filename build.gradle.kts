import org.spongepowered.asm.gradle.plugins.MixinExtension

plugins {
    java
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.spongepowered.mixin") version "0.7-SNAPSHOT"
}

val minecraft_version: String by project
val forge_version: String by project

val entityTextureFeatures_version: String by project
val entityModelFeatures_version: String by project

val embeddium_version: String by project
val oculus_version: String by project

version = "1.0.0"

base {
    archivesName = "entitymodelshaderfix-forge-1.20.1"
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }

    withSourcesJar()
}

repositories {
    mavenCentral()
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        forRepositories(fg.repository)
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

dependencies {
    minecraft("net.minecraftforge:forge:${minecraft_version}-${forge_version}")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.0")
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0") {
        jarJar.ranged(this, "[0.5.0,)")
    })
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    implementation(fg.deobf("maven.modrinth:entitytexturefeatures:${entityTextureFeatures_version}"))
    runtimeOnly(fg.deobf("maven.modrinth:entity-model-features:${entityModelFeatures_version}"))

    runtimeOnly(fg.deobf("maven.modrinth:embeddium:${embeddium_version}"))
    implementation(fg.deobf("maven.modrinth:oculus:${oculus_version}"))
}

minecraft {
    mappings("official", "1.20.1")
    copyIdeResources = true
    runs {
        create("client"){
            properties(mapOf(
                "forge.logging.console.level" to "debug",

                // Needed so mixins get deobfuscated
                "mixin.env.remapRefMap" to "true",
                "mixin.env.refMapRemappingFile" to "$projectDir/build/createSrgToMcp/output.srg",
            ))

            arg("-mixin.config=entitymodelshaderfix.mixins.json")
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets.main.get())
        }
    }
}
configure<MixinExtension> {
    add(sourceSets.main.get(), "entitymodelshaderfix.refmap.json")
}

tasks.jar {
    manifest {
        attributes("MixinConfigs" to "entitymodelshaderfix.mixins.json")
    }

    finalizedBy("reobfJar")
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}
