pipeline {
    agent any
   tools {
      // Install the Maven version configured as "M3" and add it to the path.
      maven "M3"
	jdk "JAVAINSTALL"
   }

    stages {
        stage ('Compile Stage') {

            steps {
               
                    bat 'mvn clean compile'
                
            }
        }

        stage ('Testing Stage') {

            steps {
  
                    bat 'mvn test'
                
            }
        }
    }
}