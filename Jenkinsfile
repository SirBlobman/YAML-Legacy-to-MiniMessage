pipeline {
    agent any

    options {
        githubProjectProperty(projectUrlStr: "https://github.com/SirBlobman/YAML-Legacy-to-MiniMessage")
    }

    triggers {
        githubPush()
    }

    tools {
        jdk "JDK 17"
    }

    stages {
        stage("Gradle: Build") {
            steps {
                withGradle {
                    sh("./gradlew clean build --refresh-dependencies --no-daemon")
                }
            }
        }
    }

    post {
        success {
            archiveArtifacts artifacts: 'build/libs/converter.jar', fingerprint: true
        }
    }
}
