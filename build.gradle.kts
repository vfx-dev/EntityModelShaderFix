import org.spongepowered.asm.gradle.plugins.MixinExtension

plugins {
    java
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    id("org.spongepowered.mixin") version "0.7-SNAPSHOT"
}

group = "com.ventooth"
version = "1.0.0"

val mod_modid = "entitymodelshaderfix"
val mod_name = "entitymodelshaderfix"
val mod_version = "$version"
val mod_rootPkg = "$group.$mod_modid"

base {
    archivesName = "${mod_modid}-forge"
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
    minecraft("net.minecraftforge:forge:1.20.1-47.4.0")

    compileOnly("org.projectlombok:lombok:1.18.42")
    annotationProcessor("org.projectlombok:lombok:1.18.42")

    annotationProcessor("io.github.llamalad7:mixinextras-common:0.5.0")
    compileOnly("io.github.llamalad7:mixinextras-common:0.5.0")
    implementation(jarJar("io.github.llamalad7:mixinextras-forge:0.5.0") {
        jarJar.ranged(this, "[0.5.0,)")
    })
    annotationProcessor("org.spongepowered:mixin:0.8.5:processor")

    implementation(fg.deobf("maven.modrinth:${"entitytexturefeatures:7.0.2"}"))
    runtimeOnly(fg.deobf("maven.modrinth:${"entity-model-features:3.0.1"}"))

    runtimeOnly(fg.deobf("maven.modrinth:${"embeddium:0.3.31+mc1.20.1"}"))
    implementation(fg.deobf("maven.modrinth:${"oculus:1.20.1-1.8.0"}"))
}

minecraft {
    mappings("official", "1.20.1")
    copyIdeResources = true
    runs {
        create("client") {
            properties(
                mapOf(
                    "forge.logging.console.level" to "debug",

                    // Needed so mixins get deobfuscated
                    "mixin.env.remapRefMap" to "true",
                    "mixin.env.refMapRemappingFile" to "$projectDir/build/createSrgToMcp/output.srg",
                )
            )

            arg("-mixin.config=$mod_modid.mixins.json")
            workingDirectory = project.file("run").canonicalPath
            source(sourceSets.main.get())
        }
    }
}
configure<MixinExtension> {
    add(sourceSets.main.get(), "$mod_modid.refmap.json")
}

tasks.jar {
    version = "${mod_version}+mc1.20.1"
    manifest {
        attributes("MixinConfigs" to "$mod_modid.mixins.json")
    }

    finalizedBy("reobfJar")
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("META-INF/mods.toml") {
        expand("version" to project.version)
    }
}
