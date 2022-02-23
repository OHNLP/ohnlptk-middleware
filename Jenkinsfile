/**
 * Deploys an Atlas image using Docker and Docker Compose
 */
def deployMiddlewareServiceContainer() {
    return sh("""
    ls -al
    # Stop and remove existing services
    docker-compose down -v --rmi all | xargs echo 
    docker stop ohnlptk-middleware_ohnlptk-middleware_1 | xargs echo
    docker rm ohnlptk-middleware_ohnlptk-middleware_1 | xargs echo
    docker-compose up -d

    # Clean up unused images.
    docker rmi \$(docker images -aq) | xargs echo

    """)
}

pipeline {
    options {
        skipDefaultCheckout()
        timestamps()
    }
    
    agent {
        label 'middleware-ec2-stage-01'
    }
    stages {
        
        stage('Deploy - NONPRD') {
            agent {
                label 'middleware-ec2-stage-01'
            }
            steps {
                cleanWs()
                checkout scm
                configFileProvider([

                    configFile(fileId: 'middleware-compose-file', targetLocation: 'docker-compose.yml')
                ]) {                      
                         deployMiddlewareServiceContainer()
                                
                }
            }
        }
    }
}