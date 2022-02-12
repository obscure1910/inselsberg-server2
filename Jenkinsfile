pipeline {
  agent any

  stages {
    stage('information') {
      steps {
        sh 'echo information:'
        sh 'javac -version'
        sh 'gradle --version'
        sh 'docker --version'
      }
    }
    stage('chmod') {
      steps {
        sh 'chmod +x ./gradlew'
      }
    }
    stage('test') {
      steps {
        sh './gradlew test --debug --stacktrace'
      }
    }
    stage('docker build') {
      steps {
        sh './gradlew jibDockerBuild'
      }
    }
    stage('deployment information') {
      steps {
        sh 'echo RUN in Jenkins the "Start Services blcode2" job'
      }
    }
  }

  post {
    always {
      sh 'echo clean images'
      sh 'docker image prune --force'
    }
  }

}