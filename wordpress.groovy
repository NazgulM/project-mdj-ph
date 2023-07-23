pipeline {
    agent any
    
    stages {
        stage('git checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/NazgulM/project-mdj-ph.git']]])
            }
        }
        
        stage('mysql deployment') {
            steps {
                sh '''
                    /usr/bin/kubectl  apply -f mysql-user-pass.yaml
                    /usr/bin/kubectl  apply -f mysql-db-url.yaml
                    /usr/bin/kubectl  apply -f mysql-root-pass.yaml
                    /usr/bin/kubectl  apply -f mysql-pv.yaml
                    /usr/bin/kubectl  apply -f mysql-pvc.yaml
                    /usr/bin/kubectl  apply -f mysql-deployment.yaml
                    /usr/bin/kubectl  apply -f mysql-service.yaml'''
            }
        }
        
        stage('php-myadmin deployment') { // Fixed typo in the stage name here.
            steps {
                sh '''
                    /usr/bin/kubectl  apply -f phpmyadmin-deploy.yaml 
	                /usr/bin/kubectl  apply -f phpmyadmin-service.yaml'''
          	}
		    }

		    stage('wordpress deployment') {  // Fixed typo in the stage name here as well.
			     steps{
			         sh '''
			             /usr/bin/kubectl  apply -f wordpress-deploy.yaml
                         /usr/bin/kubectl  apply -f wordpress-service.yaml'''
			       }
		      }
        }
    }
