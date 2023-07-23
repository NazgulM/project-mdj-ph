pipeline {
    agent {
        any
    }
    
    stages {
        stage('git checkout') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/NazgulM/project-mdj-ph.git']]])
            }
        }
        
        stage('mysql deployment') {
            steps {
                sh '''
                    kubectl apply -f mysql-user-pass.yaml
                    kubectl apply -f mysql-db-url.yaml
                    kubectl apply -f mysql-root-pass.yaml
                    kubectl apply -f mysql-pv.yaml
                    kubectl apply -f mysql-pvc.yaml
                    kubectl apply -f mysql-deployment.yaml
                    kubectl apply -f mysql-service.yaml'''
            }
        }
        
        stage('php-myadmin deployment') { // Fixed typo in the stage name here.
            steps {
                sh '''
                    kubectl apply -f phpmyadmin-deploy.yaml 
	                  // Make sure there is no extra space at the start of this line.
			              // Also, ensure correct indentation for all subsequent lines within this block.
                    
		                kubectl apply -f phpmyadmin-service.yaml'''
          	}
		    }

		    stage('wordpress deployment') {  // Fixed typo in the stage name here as well.
			     steps{
			         sh '''
			             kubectl apply -f wordpress-deploy.yaml
                   kubectl apply -f wordpress-service.yaml'''
			       }
		      }
        }
    }
}