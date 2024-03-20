plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

val awaitilityVersion = "4.+"
val springBootVersion = "2.+"

repositories {
    mavenCentral()
}

dependencies {
    // Smack libraries for XMPP
    implementation("org.igniterealtime.smack:smack-core:4.4.1")
    implementation("org.igniterealtime.smack:smack-tcp:4.4.1")
    implementation("org.igniterealtime.smack:smack-im:4.4.1")
    implementation("org.igniterealtime.smack:smack-extensions:4.4.1")
    implementation("org.igniterealtime.smack:smack-java8:4.4.1")
    implementation("org.igniterealtime.smack:smack-xmlparser-xpp3:4.4.1")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("commons-io:commons-io:2.11.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.assertj:assertj-core:3.17.0")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testImplementation("org.awaitility:awaitility:${awaitilityVersion}")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:${springBootVersion}"))

    testImplementation("org.jmock:jmock:2.12.0")
    testImplementation("org.jmock:jmock-junit5:2.12.0")

    testImplementation("org.igniterealtime.smack:smack-core:4.4.1")
    testImplementation("org.igniterealtime.smack:smack-tcp:4.4.1")
    testImplementation("org.igniterealtime.smack:smack-im:4.4.1")
    testImplementation("org.igniterealtime.smack:smack-extensions:4.4.1")
    testImplementation("org.igniterealtime.smack:smack-java8:4.4.1")
    testImplementation("org.igniterealtime.smack:smack-xmlparser-xpp3:4.4.1")

    testImplementation("org.assertj:assertj-swing-junit:3.17.0")
}

tasks.test {
    useJUnitPlatform()
}