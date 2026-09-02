plugins {
    alias(libs.plugins.fabric.loom)
    id("maven-publish")
}

base {
    archivesName = properties["archives_base_name"] as String
    group = properties["maven_group"] as String

    val suffix = providers.gradleProperty("build_number").getOrElse("local")
    version = "${libs.versions.minecraft.get()}-$suffix"
}

repositories {
    maven {
        name = "meteor-maven"
        url = uri("https://maven.meteordev.org/releases")
    }
    maven {
        name = "meteor-maven-snapshots"
        url = uri("https://maven.meteordev.org/snapshots")
    }
    maven {
        name = "Terraformers"
        url = uri("https://maven.terraformersmc.com")
    }
    maven {
        name = "ViaVersion"
        url = uri("https://repo.viaversion.com")
    }
    mavenCentral()

    exclusiveContent {
        forRepository {
            maven {
                name = "modrinth"
                url = uri("https://api.modrinth.com/maven")
            }
        }
        filter {
            includeGroup("maven.modrinth")
        }
    }
}

val modInclude: Configuration by configurations.creating
val jij: Configuration by configurations.creating
val pythonJij: Configuration by configurations.creating

configurations {
    // include mods
    modImplementation.configure {
        extendsFrom(modInclude)
    }
    include.configure {
        extendsFrom(modInclude)
    }

    // include libraries (jar-in-jar)
    implementation.configure {
        extendsFrom(jij)
    }
    include.configure {
        extendsFrom(jij)
    }

    // Python (GraalPy) runtime ships as a separate, optional companion jar ("potion-addon"), so
    // it's only needed here at compile time. Potion Client must work fine without it installed;
    // ".py" addon scripts simply won't run until "potion-addon.jar" is also in the mods folder.
    compileOnly.configure {
        extendsFrom(pythonJij)
    }
}

dependencies {
    // Fabric
    minecraft(libs.minecraft)
    mappings(variantOf(libs.yarn) { classifier("v2") })
    modImplementation(libs.fabric.loader)

    val fapiVersion = libs.versions.fabric.api.get()
    modInclude(fabricApi.module("fabric-api-base", fapiVersion))
    modInclude(fabricApi.module("fabric-resource-loader-v1", fapiVersion))

    // Compat fixes
    modCompileOnly(fabricApi.module("fabric-renderer-indigo", fapiVersion))
    modCompileOnly(libs.sodium) { isTransitive = false }
    modCompileOnly(libs.lithium) { isTransitive = false }
    modCompileOnly(libs.iris) { isTransitive = false }
    modCompileOnly(libs.viafabricplus) { isTransitive = false }
    modCompileOnly(libs.viafabricplus.api) { isTransitive = false }

    modCompileOnly(libs.baritone)
    modCompileOnly(libs.modmenu)

    // Libraries (JAR-in-JAR)
    jij(libs.orbit)
    jij(libs.starscript)
    jij(libs.discord.ipc)
    jij(libs.reflections)
    jij(libs.netty.handler.proxy) { isTransitive = false }
    jij(libs.netty.codec.socks) { isTransitive = false }
    jij(libs.waybackauthlib)
    jij(libs.minecraft.auth)

    // Potion scripting: lightweight BeanShell-based addons (".bsh"), bundled directly.
    jij("org.beanshell:bsh:2.0b5")

    // Potion scripting: optional Python addon support (".py"), shipped as the separate
    // "potion-addon" companion jar. See the NOTE above the "compileOnly" block.
    // NOTE: the "python-community"/"python" coordinates are POM-only aggregators with no jar of
    // their own (Fabric Loader's classpath scanner crashes on them), so depend on their real
    // leaf jars directly instead.
    pythonJij("org.graalvm.polyglot:polyglot:25.3.4.1")
    pythonJij("org.graalvm.python:python-language:25.3.4.1")
    pythonJij("org.graalvm.python:python-resources:25.3.4.1")
    pythonJij("org.graalvm.truffle:truffle-runtime:25.3.4.1")
}

val generatePythonAddonMetadata by tasks.registering {
    val outputFile = layout.buildDirectory.file("generated/python-addon/fabric.mod.json")
    outputs.file(outputFile)

    doLast {
        outputFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(
                """
                {
                  "schemaVersion": 1,
                  "id": "python-addon",
                  "version": "${project.version}",
                  "name": "Python Addon (Python Runtime)",
                  "description": "Adds Python (\".py\") addon scripting support (GraalPy) to Potion Client. Install alongside potion-client; \".bsh\" addons work fine without it.",
                  "environment": "client",
                  "depends": {
                    "java": ">=21",
                    "fabricloader": "*"
                  }
                }
                """.trimIndent()
            )
        }
    }
}

// Fabric's "jars" nested-jar field only works for jars that are themselves valid Fabric mods
// (their own fabric.mod.json). GraalPy's jars are plain libraries, not mods, so instead this
// shades their classes directly into python-addon.jar so they land on the classpath normally.
val pythonAddonJar by tasks.registering(Jar::class) {
    dependsOn(generatePythonAddonMetadata)
    archiveBaseName.set("python-addon")
    archiveVersion.set(project.version.toString())
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from({ pythonJij.map { if (it.isDirectory) it else zipTree(it) } })
    from(layout.buildDirectory.file("generated/python-addon/fabric.mod.json"))

    exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")

    // Truffle/GraalPy ship as multi-release jars (META-INF/versions/*); without this attribute
    // the JVM ignores those version-specific classes and Truffle fails to initialize.
    manifest {
        attributes["Multi-Release"] = "true"
    }
}

tasks.named("build") {
    dependsOn(pythonAddonJar)
}

val generateNativeAddonMetadata by tasks.registering {
    val outputFile = layout.buildDirectory.file("generated/native-addon/fabric.mod.json")
    outputs.file(outputFile)

    doLast {
        outputFile.get().asFile.apply {
            parentFile.mkdirs()
            writeText(
                """
                {
                  "schemaVersion": 1,
                  "id": "native-addon",
                  "version": "${project.version}",
                  "name": "Native Addon (C/C++ Runtime)",
                  "description": "Adds native (\".dll\"/\".so\"/\".dylib\") addon support to Potion Client via JNI. Install alongside potion-client; \".bsh\"/\".py\" addons work fine without it. Native addons run unsandboxed — only install ones you trust.",
                  "environment": "client",
                  "depends": {
                    "java": ">=21",
                    "fabricloader": "*"
                  }
                }
                """.trimIndent()
            )
        }
    }
}

// Just a marker class + metadata: unlike Python, native addon support needs no bundled runtime
// (JNI is built into the JDK). This jar's only job is to be an explicit opt-in gate, since native
// addons run unsandboxed code — see NativeRuntime's javadoc.
val nativeAddonJar by tasks.registering(Jar::class) {
    dependsOn(generateNativeAddonMetadata)
    archiveBaseName.set("native-addon")
    archiveVersion.set(project.version.toString())
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(sourceSets["nativeAddon"].output)
    from(layout.buildDirectory.file("generated/native-addon/fabric.mod.json"))
}

tasks.named("build") {
    dependsOn(nativeAddonJar)
}

sourceSets {
    val launcher by creating {
        java {
            srcDir("src/launcher/java")
        }
    }

    // Marker class for the "native-addon" companion jar — see NativeRuntime's javadoc.
    val nativeAddon by creating {
        java {
            srcDir("src/nativeAddon/java")
        }
    }
}

dependencies {
    // Native (C/C++ via JNI) addon support is gated behind the separate "native-addon" companion
    // jar, same opt-in pattern as Python. This tiny marker class only exists in that jar, so
    // NativeRuntime (which references it) throws a clean NoClassDefFoundError when it's absent.
    compileOnly(sourceSets["nativeAddon"].output)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    if (System.getenv("CI")?.toBoolean() == true) {
        withSourcesJar()
        withJavadocJar()
    }
}

// Handle transitive dependencies for jar-in-jar
// Based on implementation from BaseProject by FlorianMichael/EnZaXD
// Source: https://github.com/FlorianMichael/BaseProject/blob/main/src/main/kotlin/de/florianmichael/baseproject/Fabric.kt
// Licensed under Apache License 2.0
afterEvaluate {
    val jijConfig = configurations.findByName("jij") ?: return@afterEvaluate

    // Dependencies to exclude from jar-in-jar
    val excluded = setOf(
        "org.slf4j",    // Logging provided by Minecraft
        "jsr305"        // Compile time annotations only
    )

    jijConfig.incoming.resolutionResult.allDependencies.forEach { dep ->
        val requested = dep.requested.displayName

        if (excluded.any { requested.contains(it) }) return@forEach

        val compileOnlyDep = dependencies.create(requested) {
            isTransitive = false
        }

        val implDep = dependencies.create(compileOnlyDep)

        dependencies.add("compileOnlyApi", compileOnlyDep)
        dependencies.add("implementation", implDep)
        dependencies.add("include", compileOnlyDep)
    }
}

loom {
    accessWidenerPath = file("src/main/resources/meteor-client.accesswidener")
}

tasks {
    processResources {
        val buildNumber = providers.gradleProperty("build_number").getOrElse("")
        val commit = providers.gradleProperty("commit").getOrElse("")

        val propertyMap = mapOf(
            "version" to project.version,
            "build_number" to buildNumber,
            "commit" to commit,
            "minecraft_version" to libs.versions.minecraft.get(),
            "loader_version" to libs.versions.fabric.loader.get()
        )

        inputs.properties(propertyMap)
        filesMatching("fabric.mod.json") {
            expand(propertyMap)
        }
    }

    // Compile launcher with Java 8 for backwards compatibility
    named<JavaCompile>("compileLauncherJava").configure {
        sourceCompatibility = JavaVersion.VERSION_1_8.toString()
        targetCompatibility = JavaVersion.VERSION_1_8.toString()
        options.compilerArgs.add("-Xlint:-options")
    }

    jar {
        inputs.property("archivesName", project.base.archivesName.get())

        from("LICENSE") {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }

        // Include launcher classes
        from(sourceSets["launcher"].output)

        manifest {
            attributes["Main-Class"] = "meteordevelopment.meteorclient.Main"
        }
    }

    withType<JavaCompile>().configureEach {
        options.compilerArgs.addAll(
            listOf(
                "-Xlint:deprecation",
                "-Xlint:unchecked"
            )
        )
    }

    javadoc {
        with(options as StandardJavadocDocletOptions) {
            addStringOption("Xdoclint:none", "-quiet")
            addStringOption("encoding", "UTF-8")
            addStringOption("charSet", "UTF-8")
        }
    }

    build {
        if (System.getenv("CI")?.toBoolean() == true) {
            dependsOn("javadocJar")
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifactId = "meteor-client"

            version = "${libs.versions.minecraft.get()}-SNAPSHOT"
        }
    }

    repositories {
        maven("https://maven.meteordev.org/snapshots") {
            name = "meteor-maven"

            credentials {
                username = System.getenv("MAVEN_METEOR_ALIAS")
                password = System.getenv("MAVEN_METEOR_TOKEN")
            }

            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }
}
