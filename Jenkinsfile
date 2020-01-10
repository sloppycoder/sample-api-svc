#!groovy

node {
    stage('Clone sources') {
        git url: 'https://github.com/sloppycoder/sample-api-svc.git', branch:simplify
    }

    stage('Maven build') {
        buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean install -Pjib-build'
    }
}