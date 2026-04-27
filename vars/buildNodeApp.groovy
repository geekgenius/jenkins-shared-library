def call(Map config = [:]) {
    def deployBranch = config.deployBranch ?: 'main'

    pipeline {
        agent any

        options {
            timeout(time: 15, unit: 'MINUTES')
            disableConcurrentBuilds()
            buildDiscarder(logRotator(numToKeepStr: '10'))
        }

        triggers {
            cron('H 2 * * *')
        }

        environment {
            PATH               = "/usr/bin:/usr/local/bin:${env.PATH}"
            NETLIFY_AUTH_TOKEN = credentials('netlify-auth-token')
            NETLIFY_SITE_ID    = credentials('netlify-site-id')
            CI                 = 'true'
        }

        stages {
            stage('Install') {
                steps {
                    sh 'npm install'
                }
            }

            stage('Build') {
                steps {
                    sh 'npm run build'
                }
            }

            stage('Test') {
                steps {
                    sh 'npm test'
                }
            }

            stage('Deploy') {
                when {
                    branch deployBranch
                }
                steps {
                    sh 'npx netlify-cli deploy --prod --dir=dist --auth=$NETLIFY_AUTH_TOKEN --site=$NETLIFY_SITE_ID'
                }
            }
        }

        post {
            success {
                echo "✅ Pipeline passed on branch: ${env.BRANCH_NAME}"
            }
            failure {
                echo "❌ Pipeline failed on branch: ${env.BRANCH_NAME} — check Console Output"
            }
            always {
                cleanWs(patterns: [
                    [pattern: 'dist/**', type: 'INCLUDE'],
                    [pattern: 'src/**', type: 'INCLUDE'],
                    [pattern: '*.ts', type: 'INCLUDE'],
                    [pattern: '*.html', type: 'INCLUDE'],
                    [pattern: '*.json', type: 'INCLUDE'],
                    [pattern: '*.toml', type: 'INCLUDE'],
                    [pattern: '.git/**', type: 'INCLUDE']
                ])
            }
        }
    }
}
