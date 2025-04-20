// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

// Add this if you're using version catalogs (libs.versions.toml)
// If not, you can add the dependency directly like:
// classpath("com.google.gms:google-services:4.4.2")
buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
    }
}