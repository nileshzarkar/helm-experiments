#  Helm Repository on GitHub




## Step-01: Introduction
- Host Helm Repository on GitHub




## Step-02: Create GitHub Repository
- **Repository Name:** helm-charts-repo
- **Repository Name:** htmlpage-chart-repo
- **Description:** Helm Charts Repository on GitHub
- **Repository Type:** Public
- **Initialize this repository with:** CHECK Add a README file
- Click on **Create repository**




## Step-03: Create gh-pages branch
- **New branch name:** gh-pages
- **Source:** main
- Click on **Create new branch**




## Step-04: Enable GitHub Pages for gh-pages branch (if not enabled by default)
- Go to repository: helm-charts-repo -> Settings -> Code and Automation -> Pages
- Go to repository: htmlpage-chart-repo -> Settings -> Code and Automation -> Pages
- Review **Branch**
- Also access the GitHub pages site
- https://stacksimplify.github.io/helm-charts-repo/
- https://nileshzarkar.github.io/htmlpage-chart-repo/



## Step-05: Clone the GitHub Repository local desktop
```t
# Clone the GitHub Repository
git clone git@github.com:stacksimplify/helm-charts-repo.git
https://github.com/nileshzarkar/htmlpage-chart-repo.git
```




## Step-06: Review and Copy GitRepo Files
```t
# Change Directory
cd 44-Helm-Repo-on-GitHub
D:\Experiments\helm-experiments\helm-experiments\htmlpage-chart-repo

# Copy content from gitrepo-content to helm-charts-repo
1. .github folder: contains GitHub Actions release.yaml
Copy the folder 
https://github.com/nileshzarkar/helm-experiments/tree/main/helm-masterclass/helm-masterclass/44-Helm-Repo-on-GitHub/gitrepo-content/.github/workflows
to 
D:\Experiments\helm-experiments\helm-experiments\htmlpage-chart-repo

2. charts folder: Contains "myfirstchart" helm chart
Copy the htmlpage chart from 
D:\Experiments\helm-experiments\helm-experiments\html-page-1.0.0\htmlpage
to 
D:\Experiments\helm-experiments\helm-experiments\htmlpage-chart-repo
```

Chart Releaser is tool designed to helm github repos self-host their own chart repos by adding helm chart artifacts to github releases named for the chart version and then creating an index.yaml file for those releases that can be hosted on github pages (or else where)


## Step-07: Create Chart Release 0.1.0
### Step-07-01: Verify Chart.yaml
- Ensure we have the `appVersion: "1.0.0"` and `version: 1.0.0`
```yaml
apiVersion: v2
appVersion: "1.0.0"
description: Signed Charts
name: myfirstchart
type: application
version: 1.0.0
```



### Step-07-02: Check-in Code to Remote GitHub Repo
```t
# Check-in Code
git add .
git commit -am "0.1.0 commit"
git push
```



### Step-07-03: Verify Actions in GitHub Repo
- Go to helm-charts-repo -> Actions
- Review workflow runs
  - 0.1.0 commit
  - pages build and deployment




### Step-07-04: Switch to gh-pages branch and verify index.yaml
- Switch to `gh-pages` and review `index.yaml`
- https://github.com/stacksimplify/helm-charts-repo/blob/gh-pages/index.yaml




### Step-07-05: Verify Releases and Tags
- Go to **Releases** and verify 
- Go to **Tags** and verify




## Step-08: Create Chart Release 0.2.0
### Step-08-01: Update Chart.yaml version
```t
# Update Chart.yaml
version: 0.2.0
appVersion: "0.2.0"
```



### Step-08-02: Check-in Code to Remote GitHub Repo
```t
# Check-in Code
git add .
git commit -am "0.2.0 commit"
git push
```



### Step-08-03: Verify Actions in GitHub Repo
- Go to helm-charts-repo -> Actions
- Review workflow runs
  - 0.1.0 commit
  - pages build and deployment




### Step-08-04: Switch to gh-pages branch and verify index.yaml
- Switch to `gh-pages` and review `index.yaml`
- https://github.com/stacksimplify/helm-charts-repo/blob/gh-pages/index.yaml




### Step-08-05: Verify Releases and Tags
- Go to **Releases** and verify 
- Go to **Tags** and verify




## Step-09: Add GitHub Helm Repo in local desktop and Search Repo
```t
# Helm Repo URL
https://stacksimplify.github.io/helm-charts-repo/

# List Helm Repo
helm repo list

# Add Helm Repo
helm repo add mygithelmrepo https://stacksimplify.github.io/helm-charts-repo/

# List Helm Repo
helm repo list

# Helm Search Repo
helm search repo mygithelmrepo/myfirstchart

# Helm Search Repo with --versions
helm search repo mygithelmrepo/myfirstchart --versions
```
## Step-10: Deploy and Verify from GitHub Helm Repo
```t
# Helm Install
helm install myapp1 mygithelmrepo/myfirstchart --atomic

# Helm Status
helm status myapp1 --show-resources

# Access Application
http://localhost:31239

# Helm Uninstall
helm uninstall myapp1
```
