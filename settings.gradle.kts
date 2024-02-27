pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://www.jitpack.io") {
            content {
                includeGroup("com.github.androidmads")
            }
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io") {
            content {
                includeGroup("com.github.androidmads")
            }
        }
    }
}

rootProject.name = "QR Code Keeper"
include(":mobile")
//include(":wear")
include(":core")
include(":storage")
