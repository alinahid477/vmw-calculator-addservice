podTemplate( //idleMinutes: 30, 
    yaml: '''
apiVersion: v1
kind: Pod
metadata:
  namespace: jenkins
spec:
  volumes:
    - name: docker-insecure-registries
      configMap:
        name: harbor-allow-insecure-registries
        items:
          - key: daemon.json
            path: daemon.json
    - name: cache
      hostPath:
        path: /tmp
        type: Directory
    - name: m2
      persistentVolumeClaim:
        claimName: jenkins-pvc1
  serviceAccountName: jenkins-sa
  containers:
  - name: docker
    image: docker:19.03.1-dind
    securityContext:
      privileged: true
    env:
      - name: DOCKER_TLS_CERTDIR
        value: ""
    volumeMounts:
    - name: cache
      mountPath: /var/lib/docker
    - name: docker-insecure-registries
      mountPath: /etc/docker/daemon.json
      subPath: daemon.json
  - name: maven
    image: maven:latest
    command:
    - cat
    tty: true
    volumeMounts:
    - mountPath: "/root/.m2"
      name: m2
''') {
    node(POD_LABEL) {
        
        stage("GIT") {
          git credentialsId: 'pvt-repo-cred', branch: 'main', url: 'https://github.com/alinahid477/vmw-calculator-addservice.git'
        }
        
        stage("MAVEN") {
          container('maven') {
              sh """
                  mvn package -DskipTests
              """
          }
        }
        
        stage("DOCKER") {
          container('docker') {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-cred', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
              sh """ 
                  docker login -u ${USERNAME} -p ${PASSWORD} &&
                  docker build -t harbor-svc.haas-422.pez.vmware.com/anahid/addservice:latest .
                  docker logout
              """    
                
            }
            withCredentials([usernamePassword(credentialsId: 'pvt-registry-cred', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
              sh """
                  docker login -u ${USERNAME} -p ${PASSWORD} harbor-svc.haas-422.pez.vmware.com &&
                  docker push harbor-svc.haas-422.pez.vmware.com/anahid/addservice:latest
              """
            }
          }
        }
        
        stage("K8S") {
          withKubeConfig([credentialsId: 'jenkins-robot-token',
                      serverUrl: 'https://192.168.220.9:6443',
                      clusterName: 'apps-g1',
                      namespace: 'calculator'
                      ]) {
            sh 'curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"'
            // sh 'cat $(ls -la | grep config | awk '{print $9}')'
            sh 'chmod 777 ./kubectl'
            sh './kubectl apply -f kubernetes/deployment.yaml'
            // sh './kubectl patch deployment addservice-deploy -p \"{\\"spec\\": {\\"template\\": {\\"metadata\\": { \\"labels\\": {  \\"redeploy\\": \\"$(date +%s)\\"}}}}}\" -n calculator'
          }
        }
                  
        
    }
}
