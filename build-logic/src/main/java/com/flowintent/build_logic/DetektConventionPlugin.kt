import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("io.gitlab.arturbosch.detekt")

            val detektExtension = extensions.getByType<DetektExtension>()

            detektExtension.apply {
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                buildUponDefaultConfig = true
                ignoreFailures = false
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    html.required.set(true)
                    xml.required.set(true)
                    sarif.required.set(true)
                }

                jdkHome.set(file(System.getProperty("java.home")))
            }

            tasks.named("check") {
                dependsOn(tasks.withType<Detekt>())
            }
        }
    }
}
