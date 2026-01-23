# GitHub Secrets Setup for DockerHub Integration

## Overview

To enable automatic Docker image push to DockerHub via GitHub Actions, you need to configure GitHub Secrets.

---

## Step 1: Create DockerHub Access Token

### In DockerHub

1. Go to: https://hub.docker.com/settings/personal-access-tokens
2. Click **"New Access Token"**
3. Configure:
   - **Token name:** `github-actions-gs-rest-service`
   - **Permissions:** Check "Read, Write, Delete"
4. Click **"Generate"**
5. **Copy the token** (you won't see it again!)

### Tokens Page
```
Account Settings
├── Personal Access Tokens
│   ├── New Access Token
│   ├── Token name: github-actions-gs-rest-service
│   └── Permissions: ✓ Read ✓ Write ✓ Delete
```

---

## Step 2: Add GitHub Secrets

### Navigate to Repository Settings

1. Go to: https://github.com/zkouari-f/gs-rest-service
2. Click **Settings** tab
3. Left sidebar → **Secrets and variables** → **Actions**

### Add DOCKERHUB_USERNAME

1. Click **"New repository secret"**
2. **Name:** `DOCKERHUB_USERNAME`
3. **Value:** Your DockerHub username (e.g., `zkouari-f`)
4. Click **"Add secret"**

### Add DOCKERHUB_TOKEN

1. Click **"New repository secret"**
2. **Name:** `DOCKERHUB_TOKEN`
3. **Value:** Paste the access token from Step 1
4. Click **"Add secret"**

### Verify Secrets Added

Both should appear in the secrets list (values hidden):
```
Secrets (2)
├── DOCKERHUB_USERNAME
└── DOCKERHUB_TOKEN
```

---

## Step 3: Test the CI/CD Pipeline

### Trigger Workflow

1. Make a minor change to any file in the repo:
```bash
echo "# Updated: $(date)" >> complete/README.md
git add .
git commit -m "Test Docker CI/CD pipeline"
git push origin main
```

2. Go to: https://github.com/zkouari-f/gs-rest-service/actions
3. You should see workflow **"CI/CD Pipeline"** running

### Monitor Execution

- **Green checkmark** (✅): Job succeeded
- **Red X** (❌): Job failed
- **Yellow dot** (🟡): In progress

### Expected Jobs

```
Build & Test
├── Compile
├── Tests (9 tests)
└── Mutation Testing

Code Quality
├── SonarQube (if configured)
└── Static Analysis

Security Scan
├── Trivy
└── GitGuardian (if configured)

Docker Build & Push
├── Build Docker image
└── Push to DockerHub ← THIS ONE
```

### Verify Docker Push Success

1. Check GitHub Actions logs:
   - Click the workflow run
   - Click **"docker-build"** job
   - Click **"Build and push Docker image"** step
   - Should see: `Successfully pushed image`

2. Verify on DockerHub:
   - Go to: https://hub.docker.com/r/zkouari-f/gs-rest-service
   - Should see:
     - Public repository
     - Tags: `latest`, commit SHA
     - Created time (just now)

3. Pull from DockerHub:
```bash
docker pull zkouari-f/gs-rest-service:latest
```

---

## Troubleshooting

### Issue: "DOCKERHUB_USERNAME is empty"

**Cause:** Secret not configured
**Solution:**
1. Go to repo Settings → Secrets
2. Verify both secrets exist:
   - `DOCKERHUB_USERNAME`
   - `DOCKERHUB_TOKEN`
3. Trigger workflow again

### Issue: "Invalid auth configuration"

**Cause:** Wrong token or username
**Solution:**
1. Test locally:
```bash
docker login
# Username: zkouari-f
# Password: <your-access-token>
# Should print: Login Succeeded
```

2. If local login works but GitHub fails:
   - Regenerate access token
   - Update `DOCKERHUB_TOKEN` secret

### Issue: "Permission denied"

**Cause:** Access token has insufficient permissions
**Solution:**
1. Delete old token
2. Create new token with: ✓ Read ✓ Write ✓ Delete
3. Update secret

### Issue: Image not appearing on DockerHub

**Cause:** Push step skipped or failed silently
**Solution:**
1. Check GitHub Actions logs for errors
2. Verify image size (too large?)
3. Check DockerHub rate limits (Docker Free tier limit)

---

## Image Tags in DockerHub

After successful push, you'll see:

```
zkouari-f/gs-rest-service

Tags:
├── latest               (latest commit to main)
├── abc123def456         (full commit SHA)
├── 1.0.0               (semantic version - optional)
└── java17              (build variant - optional)
```

### How Tags Work

```bash
# Pull latest version
docker pull zkouari-f/gs-rest-service:latest

# Pull specific commit
docker pull zkouari-f/gs-rest-service:abc123def456

# Run it
docker run -p 8080:8080 zkouari-f/gs-rest-service:latest
```

---

## Setting Up Version Tags

For semantic versioning (optional but recommended):

### Manual Tagging

```bash
# Tag current commit as v1.0.0
git tag -a v1.0.0 -m "Release version 1.0.0"
git push origin v1.0.0
```

### Update Workflow for Version Tags

```yaml
- name: Get version from tag
  id: version
  run: echo "version=${GITHUB_REF#refs/tags/}" >> $GITHUB_OUTPUT

- name: Build and push with version
  if: startsWith(github.ref, 'refs/tags/')
  uses: docker/build-push-action@v5
  with:
    context: ./complete
    push: true
    tags: |
      ${{ secrets.DOCKERHUB_USERNAME }}/gs-rest-service:${{ steps.version.outputs.version }}
      ${{ secrets.DOCKERHUB_USERNAME }}/gs-rest-service:latest
```

---

## Security Best Practices

### ✅ DO

- ✅ Use Access Token (not password)
- ✅ Limit token permissions: Read, Write, Delete only
- ✅ Rotate tokens periodically (every 90 days)
- ✅ Use separate tokens for CI/CD and local development
- ✅ Enable 2FA on DockerHub account
- ✅ Review Actions secrets regularly

### ❌ DON'T

- ❌ Don't commit tokens to git
- ❌ Don't use passwords in GitHub Actions
- ❌ Don't share access tokens
- ❌ Don't grant unnecessary permissions
- ❌ Don't use same token for multiple repos (each repo = separate token)

---

## GitHub Actions Workflow Visibility

### Public Workflows

Your GitHub Actions logs are visible to:
- Anyone who can see your repository (public repos)
- Organization members (private repos)
- **NOT visible:** Secret values (masked with `***`)

### Masking Secrets

GitHub automatically masks secrets in logs:

```yaml
# Before masking
Log: Pushing to zkouari-f/gs-rest-service using token: ghp_abcdef123456

# After masking
Log: Pushing to zkouari-f/gs-rest-service using token: ***
```

---

## Automated Pushes

### When Triggered

Docker image is automatically pushed when:
- ✅ Code pushed to `main` branch
- ✅ Pull request merged to `main`
- ✅ GitHub Actions build succeeds
- ✅ Docker secrets are configured

### Not Pushed

Docker image is skipped when:
- ❌ Push to other branches (develop, feature/*, etc.)
- ❌ Build fails
- ❌ Pull request (no push to main)
- ❌ Secrets not configured

### Manual Push (if needed)

```bash
# Manually build and push locally
cd complete
docker build -t zkouari-f/gs-rest-service:manual .
docker push zkouari-f/gs-rest-service:manual
```

---

## Monitoring & Notifications

### Enable Email Notifications

1. Go to: https://github.com/settings/notifications
2. Enable: "Notify me when my runs are cancelled"
3. You'll get email on failed workflows

### Custom Notifications

GitHub Apps available:
- **Slack:** Notify on build success/failure
- **Discord:** Real-time notifications
- **Teams:** Microsoft Teams integration

---

## Cost Implications

### GitHub Actions Free Tier

```
- Public repos: Unlimited free minutes
- Private repos: 2,000 free minutes/month
- Storage: 1 GB artifacts + logs
```

**GS-REST-Service estimate:**
- Build + test: ~5 minutes per push
- Docker build/push: ~3 minutes per push
- **Total:** ~8 minutes per push
- **Monthly estimate:** 10-20 pushes = 80-160 minutes ✅ Well under 2,000 min limit

---

## DockerHub Rate Limits

### Free Plan Limits

```
- Image pulls: 200 pulls per 6 hours (per user)
- Image builds: 1 concurrent build
- Storage: Unlimited public repos
```

**Note:** `zkouari-f/gs-rest-service` image size: ~250MB

### Avoid Rate Limiting

- ✅ Use `docker pull` instead of rebuilding
- ✅ Cache Docker layers (already in workflow)
- ✅ Don't pull 200+ times in 6 hours
- ✅ Upgrade to Pro for higher limits (optional)

---

## Summary

| Step | Status | Action |
|------|--------|--------|
| 1 | ✅ | Dockerfile created |
| 2 | ✅ | docker-compose.yml created |
| 3 | ⏳ | Create DockerHub Access Token |
| 4 | ⏳ | Add DOCKERHUB_USERNAME secret |
| 5 | ⏳ | Add DOCKERHUB_TOKEN secret |
| 6 | ⏳ | Push code to trigger workflow |
| 7 | ⏳ | Verify image on DockerHub |

---

## Next Steps After Setup

1. **Monitor first push:**
   - Go to GitHub Actions
   - Wait for workflow to complete
   - Check DockerHub for new image

2. **Test the image:**
```bash
docker pull zkouari-f/gs-rest-service:latest
docker run -p 8080:8080 zkouari-f/gs-rest-service:latest
curl http://localhost:8080/greeting
```

3. **Share the image:**
   - DockerHub URL: https://hub.docker.com/r/zkouari-f/gs-rest-service
   - Pull command: `docker pull zkouari-f/gs-rest-service:latest`
   - Use in Kubernetes/Docker Compose

---

**Setup Guide Version:** 1.0  
**Last Updated:** 2026-01-23  
**Status:** Ready to configure GitHub Secrets
