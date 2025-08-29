plugins {
    id("java")
}

java {
    withJavadocJar()
}

tasks.withType<JavaCompile>().configureEach {
    enabled = false
}

tasks.withType<Test>().configureEach {
    enabled = false
}