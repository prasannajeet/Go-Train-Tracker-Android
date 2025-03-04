import java.util.Properties
import java.io.FileInputStream
import org.gradle.api.GradleException

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.0"
    alias(libs.plugins.ksp)
}

/**
 * Validates and retrieves the API key from local.properties.
 * Throws GradleException with detailed error messages if validation fails.
 */
fun validateAndGetApiKey(): String {
    val localPropertiesFile = rootProject.file("local.properties")
    
    if (!localPropertiesFile.exists()) {
        throw GradleException("""
            local.properties file not found!
            
            Resolution:
            1. Create a local.properties file in the project root directory
            2. Add your API key in the following format:
               MY_GO_TRAIN_API=your_api_key_here
            
            Note: local.properties is typically not committed to version control.
            Each developer should maintain their own local.properties file.
        """.trimIndent())
    }

    val properties = try {
        Properties().apply {
            load(FileInputStream(localPropertiesFile))
        }
    } catch (e: Exception) {
        throw GradleException("""
            Failed to read local.properties file: ${e.message}
            
            Resolution:
            1. Ensure local.properties is readable
            2. Check file permissions
            3. Verify file is not corrupted
        """.trimIndent())
    }

    val apiKey = properties.getProperty("MY_GO_TRAIN_API")
    
    if (apiKey.isNullOrBlank()) {
        throw GradleException("""
            MY_GO_TRAIN_API not found or empty in local.properties!
            
            Resolution:
            1. Open local.properties
            2. Add or update the API key:
               MY_GO_TRAIN_API=your_api_key_here
            3. Make sure there are no spaces around the equals sign
            4. Ensure the API key is not empty
            
            Note: The API key is required for the app to function properly.
            Contact your team lead or project administrator for the correct API key.
        """.trimIndent())
    }

    // Additional validation for API key format if needed
    if (!apiKey.matches(Regex("^[0-9]+$"))) {
        throw GradleException("""
            Invalid API key format in local.properties!
            
            Resolution:
            1. Verify the API key contains only numeric characters
            2. Remove any spaces or special characters
            3. Ensure you're using the correct API key format
            
            Current value: $apiKey
            Expected format: Numeric string (e.g., "12345678")
        """.trimIndent())
    }
    return apiKey
}

// Get API key with validation
val apiKey = validateAndGetApiKey()

android {
    namespace = "com.p2.apps.mygotraintracker"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.p2.apps.mygotraintracker"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        // Add validated API key to BuildConfig with proper escaping
        buildConfigField("String", "API_KEY", "\"${apiKey.replace("\"", "\\\"")}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        
        debug {
            // Use the same validated API key for debug builds
            buildConfigField("String", "API_KEY", "\"${apiKey.replace("\"", "\\\"")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

// Add a task to validate the API key before any build
tasks.register("validateApiKey") {
    doLast {
        validateAndGetApiKey()
    }
}

// Make the assemble task depend on API key validation
tasks.named("assemble") {
    dependsOn("validateApiKey")
}

// Add this at the end of the file, before the last closing brace
tasks.register("installGitHooks") {
    group = "git hooks"
    description = "Installs git hooks for conventional commits"
    
    doLast {
        val hooksDir = File(rootProject.projectDir, ".husky")
        val gitHooksDir = File(rootProject.projectDir, ".git/hooks")
        
        try {
            // Create .git/hooks directory if it doesn't exist
            if (!gitHooksDir.exists()) {
                gitHooksDir.mkdirs()
            }
            
            // Copy commit-msg hook
            val commitMsgHook = File(hooksDir, "commit-msg")
            val targetHook = File(gitHooksDir, "commit-msg")
            
            if (!commitMsgHook.exists()) {
                throw GradleException("""
                    Git hook not found in .husky directory!
                    
                    Resolution:
                    1. Ensure the .husky/commit-msg file exists
                    2. Check if the file was properly committed
                    3. Verify file permissions
                """.trimIndent())
            }
            
            // Copy the hook file
            commitMsgHook.copyTo(targetHook, overwrite = true)
            
            // Make the hook executable
            if (!targetHook.setExecutable(true)) {
                throw GradleException("""
                    Failed to make git hook executable!
                    
                    Resolution:
                    1. Check file permissions
                    2. Ensure you have write access to .git/hooks
                    3. Try running with elevated permissions
                """.trimIndent())
            }
            
            // Verify the hook was installed correctly
            if (!targetHook.exists() || !targetHook.canExecute()) {
                throw GradleException("""
                    Git hook installation verification failed!
                    
                    Resolution:
                    1. Check if the hook file exists in .git/hooks
                    2. Verify file permissions
                    3. Try running the installation task again
                """.trimIndent())
            }
            
            logger.lifecycle("Git hooks installed successfully!")
        } catch (e: Exception) {
            logger.error("Failed to install git hooks: ${e.message}")
            throw e
        }
    }
}

// Make both preBuild and project sync tasks depend on installGitHooks
tasks.named("preBuild") {
    dependsOn("installGitHooks")
}

// Add hook installation to project sync
tasks.named("clean") {
    dependsOn("installGitHooks")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Room
    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.bundles.ktor.client)
    implementation(libs.ktor.client.logging)
    implementation(libs.arrow.core)
    implementation(libs.kotlinx.datetime)

    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.core.coroutines)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.androidx.compose)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.mockk)
}