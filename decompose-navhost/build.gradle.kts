import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    `maven-publish`
    signing
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "decompose-navhost"
            isStatic = true

            export(libs.decompose)
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.decompose.extension.android)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.core.ktx)
        }
        commonMain.dependencies {
            api(libs.decompose)
            api(libs.decompose.extension.compose)
            api(libs.decompose.extension.compose.experimental)

            implementation(compose.ui)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.blucky8649.decompose_navhost"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

val androidSourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(project.extensions.getByType<BaseExtension>().sourceSets.getByName("main").java.srcDirs)
}

(project as ExtensionAware).extensions.configure<LibraryExtension>("android") {
    publishing.singleVariant("release")
}

afterEvaluate {
    val GROUP_ID = "io.github.blucky8649"
    val ARTIFACT_ID = "decompose-navhost"
    val VERSION = "1.0.0-alpha01"

    publishing {
        publications {
            create<MavenPublication>("release") {
                groupId = GROUP_ID
                artifactId = ARTIFACT_ID
                version = VERSION
//                artifact("$projectDir/build/outputs/aar/${project.name}-release.aar")

                if (project.plugins.hasPlugin("com.android.library")) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }
                artifact(androidSourceJar.get())

                pom {
                    name.set("Decompose Navhost")
                    groupId = GROUP_ID
                    description.set("A Decompose extension library which facilitate writing Jetpack based navigation style code for Compose Multiplatform.")
                    url.set("https://github.com/blucky8649/decompose-navhost")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("https://github.com/blucky8649/decompose-navhost/blob/main/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("blucky8649")
                            name.set("DongYeon-Lee")
                            email.set("blucky8649@gmail.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/blucky8649/decompose-navhost.git")
                    }
                }
            }
        }
    }
}

signing {
    useInMemoryPgpKeys(
        rootProject.ext["signing.keyId"] as String,
        rootProject.ext["signing.key"] as String,
        rootProject.ext["signing.password"] as String,
    )

    sign(publishing.publications)
}
