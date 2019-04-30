#!/usr/bin/groovy

podTemplate(label: 'builder',
        containers: [
            containerTemplate(name: 'maven', image: 'maven:3.6.1-jdk-11', ttyEnabled: true, command: 'cat')
        ],
        volumes: [
            persistentVolumeClaim(claimName: 'mavenrepo-volume-claim', mountPath: '/root/.m2/repository')
        ]) {

    node('builder') {

        stage('Checkout') {
            checkout scm
        }

        stage('Build') {
            container('maven') {
                sh 'mvn clean install'
            }
        }
    }
}
