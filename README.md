# Jenkins Shared Library

Reusable Jenkins pipeline library for Node.js projects with Netlify deployment.

## Usage

Add this to your `Jenkinsfile`:

```groovy
@Library('jenkins-shared-library') _

buildNodeApp(deployBranch: 'main')
```

## Available functions

### `buildNodeApp(Map config)`

Runs a full CI/CD pipeline: Install → Build → Test → Deploy.

| Parameter | Type | Default | Description |
|---|---|---|---|
| `deployBranch` | String | `'main'` | Branch that triggers a Netlify production deploy |

## Pipeline stages

| Stage | Description |
|---|---|
| Install | Runs `npm install` |
| Build | Runs `npm run build` |
| Test | Runs `npm test` |
| Deploy | Deploys to Netlify (only on `deployBranch`) |

## Requirements

The following credentials must be configured in Jenkins:

- `netlify-auth-token` — Netlify personal access token
- `netlify-site-id` — Netlify site ID

## Setup in Jenkins

1. Go to **Manage Jenkins → Configure System → Global Pipeline Libraries**
2. Add a new library:
   - **Name**: `jenkins-shared-library`
   - **Default version**: `main`
   - **Retrieval method**: Modern SCM → GitHub
   - **Repository**: `https://github.com/geekgenius/jenkins-shared-library.git`
3. Save

