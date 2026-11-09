import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.noveldokusha.android.library)
    alias(libs.plugins.noveldokusha.android.compose)
}

android {
    namespace = "my.noveldokusha.tooling.text_translator.domain"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
}
// Este módulo não contém testes próprios; Gradle 9+ pode falhar ao descobrir zero testes.
tasks.withType<Test>().configureEach {
    failOnNoDiscoveredTests = false
}
