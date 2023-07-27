template = '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    run: kubernetes
  name: kubernetes
spec:
  serviceAccount: kubernetes
  containers:
  - image: nurbakar/command:1.0
    name: kubernetes
'''


podTemplate(cloud: 'kubernetes', label: 'kubernetes', showRawYaml: false, yaml: template) {
node("kubernetes"){
container("kubernetes"){
stage("Git clone"){
git branch: 'main', url: 'https://github.com/NazgulM/project-mdj-ph.git'
}
stage('mysql deployment') {
    sh '''
        kubectl  apply -f mysql-user-pass.yaml
        kubectl  apply -f mysql-db-url.yaml
        kubectl  apply -f mysql-root-pass.yaml
        kubectl  apply -f mysql-pv.yaml
        kubectl  apply -f mysql-pvc.yaml
        kubectl  apply -f mysql-deployment.yaml
        kubectl  apply -f mysql-service.yaml
    '''
            }
        stage('php-myadmin deployment') { // Fixed typo in the stage name here.
                sh '''
                    kubectl  apply -f phpmyadmin-deploy.yaml 
	                kubectl  apply -f phpmyadmin-service.yaml'''
          	}


		    stage('wordpress deployment') {  // Fixed typo in the stage name here as well.
			     
			         sh '''
			             kubectl  apply -f wordpress-deploy.yaml
                         kubectl  apply -f wordpress-service.yaml'''
}
}
}
}