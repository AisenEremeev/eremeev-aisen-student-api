pipeline {
    agent any
    options { skipDefaultCheckout(true); disableConcurrentBuilds(); }
    triggers { pollSCM('* * * * *') }
    environment {
        IMAGE = 'eremeev-aisen-student-api:v1.0-eremeev-aisen'
        STUDENT = 'Aisen Eremeev'
    }
    stages {
        stage('Checkout') { steps { checkout scm; sh 'echo "Student: $STUDENT"; git log -1 --format="%H %s"' } }
        stage('Build') { steps { sh './gradlew --no-daemon classes bootJar -x test' } }
        stage('Test') { steps { sh './gradlew --no-daemon test' } }
        stage('Docker Build') { steps { sh 'docker build -t "$IMAGE" .' } }
        stage('Verify') { steps { sh 'docker image inspect "$IMAGE" --format="Image={{.Id}} Size={{.Size}} Created={{.Created}}"; echo "Verified for $STUDENT"' } }
    }
    post {
        always { junit allowEmptyResults: true, testResults: 'build/test-results/test/*.xml' }
    }
}
