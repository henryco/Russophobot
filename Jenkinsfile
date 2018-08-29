pipeline {
  agent any
  stages {

    stage('Prepare') {
      steps {
        sh 'rm -f src/main/resources/application.properties'
        sh 'cp /home/deploy-props/Russophobot/application.properties src/main/resources/application.properties'
        sh 'rm -f src/main/resources/bot.properties'
        sh 'cp /home/deploy-props/Russophobot/bot.properties src/main/resources/bot.properties'
      }
    }

    stage('Check') {
      steps {
        sh './gradlew check -x build -x test --stacktrace'
      }
    }

    stage('Test') {
      steps {
        sh '(cd build/test-results/ && touch *.xml) || true'
        sh './gradlew test --stacktrace'
      }
    }

    stage('Build') {
      steps {
        sh './gradlew build -x test --stacktrace'
      }
    }

    stage('Artifacts') {
      steps {
        archiveArtifacts(artifacts: 'build/libs/*', allowEmptyArchive: true, onlyIfSuccessful: true)
      }
    }

    stage('Clean') {
      steps {
        sh '(pkill -f gradle) || true'
      }
    }

    stage('Deploy') {
      when {
        branch 'release'
      }
      steps {
        sh 'rm -f /home/Programs/Russophobot/out/russophobot.jar'
        sh 'cp build/libs/russophobot-0.0.1-SNAPSHOT.jar /home/Programs/Russophobot/out/russophobot.jar'
        sh 'chmod a+x /home/Programs/Russophobot/out/russophobot.jar'

	    withEnv(overrides: ['JENKINS_NODE_COOKIE=dontKillMe']) {
          sh 'cd /home/Programs/Russophobot/out/ && ./russophobot.sh'
        }
      }
    }
  }

  post {
    always {

      sh '(pkill -f gradle) || true'

      junit 'build/test-results/**.xml'
      sh 'rm -f -r test-arch'
      sh 'mkdir test-arch'
      sh 'zip -r test-arch/test-report.zip build/reports'
      archiveArtifacts 'test-arch/*.zip'
    }
  }


}