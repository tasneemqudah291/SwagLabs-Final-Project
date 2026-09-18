pipeline {
    agent any
    environment {
        SAUCE_PASSWORD = credentials('saucedemo-test-password')
    }
    parameters {
        choice(name: 'SUITE', choices: ['testng-smoke.xml', 'testng.xml', 'testng-parallel.xml'], description: 'TestNG suite')
        booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Run Chrome headlessly')
    }
    tools { jdk 'JDK17'; maven 'Maven3' }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Build') { steps { script {
            if (isUnix()) { sh 'mvn -B -DskipTests clean package' }
            else { bat 'mvn -B -DskipTests clean package' }
        } } }
        stage('Test') { steps { script {
            def command = "mvn -B test -DsuiteXmlFile=${params.SUITE} -Dheadless=${params.HEADLESS}"
            if (isUnix()) { sh command } else { bat command }
        } } }
    }
    post { always {
        script {
            try {
                junit allowEmptyResults: false, testResults: 'target/surefire-reports/TEST-*.xml'
            } finally {
                try {
                    allure includeProperties: false, jdk: '', results: [[path: 'target/allure-results']]
                } finally {
                    archiveArtifacts allowEmptyArchive: true, artifacts: 'target/screenshots/**/*'
                }
            }
        }
    } }
}
