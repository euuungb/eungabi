import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinxKover)
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }

    jvm("desktop")

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        macosX64(),
        macosArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "eungabi"
            isStatic = true
        }
    }

    @OptIn(ExperimentalWasmDsl::class)
    listOf(
        js(IR),
        wasmJs()
    ).forEach {
        it.browser()
        it.binaries.executable()
    }

    sourceSets {
        val desktopTest by getting

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            implementation(libs.uri)
            implementation(libs.napier)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        desktopTest.dependencies {
            implementation(compose.desktop.currentOs)
        }
    }
}

kover {
    reports.verify.rule {
        minBound(80)
    }
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    dependsOn(tasks.withType(Test::class))
}

dependencies {
    androidTestImplementation(libs.androidx.ui.test.junit4.android)
    debugImplementation(libs.androidx.ui.test.manifest)
}

android {
    namespace = "com.easternkite.eungabi"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val androidSourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(
        project.extensions
            .getByType<BaseExtension>()
            .sourceSets
            .getByName("main")
            .java.srcDirs
    )
}

(project as ExtensionAware).extensions.configure<LibraryExtension>("android") {
    publishing.singleVariant("release")
}

val groupId = "io.github.easternkite"
val artifactId = "eungabi"
val version = "0.3.1"

mavenPublishing {
    coordinates(groupId, artifactId, version)
    pom {
        name.set("Eungabi")
        description.set(
            "A Compose Multiplatform Navigation Library which support Platform Native Features like Predictive Back Gesture in Android and Swipe-To-Back Gesture in iOS."
        )
        url.set("https://github.com/easternkite/eungabi")
        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("https://github.com/easternkite/eungabi/blob/main/LICENSE")
            }

            developers {
                developer {
                    id.set("easternkite")
                    name.set("DongYeon-Lee")
                    email.set("eaternkite7@gmail.com")
                }
            }

            scm {
                url.set("https://github.com/easternkite/eungabi.git")
            }
        }
    }
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL, true)
    signAllPublications()
}
