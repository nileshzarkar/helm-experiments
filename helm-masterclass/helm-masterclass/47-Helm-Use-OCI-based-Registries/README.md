# Helm Use OCI-based Registries

## Step-01: Introduction
- We will use Docker Hub as our OCI registry for storing Helm Charts
What are OCI Registries ?
OCI means Open Containers Initiative. 
An OCI Registry is like a special storage place for containers that follow specific rules created by the Open Container Initiative
In short, the place where we store our Docker Container Images
Examples of OCI Registries

Many popular registries support the OCI standard:

    Docker Hub – One of the most popular registries for storing container images.
    Amazon Elastic Container Registry (ECR) – AWS’s container image registry.
    Azure Container Registry (ACR) – Microsoft’s OCI-compliant registry.
    Google Container Registry (GCR) – Google Cloud’s image registry.
    Harbor – An open-source, cloud-native registry that supports OCI.
    GitHub Container Registry – Stores container images in GitHub.

Helm supports the above OCI registries for storing Helm Charts

What is an OCI Registry?

An OCI Registry is a storage and distribution service for OCI-compliant artifacts. These artifacts are often container images but can also include other software components like Helm charts, WASM modules, and more. OCI Registries allow you to:

    Store container images or other artifacts.
    Distribute them to users, tools, or services in a standardized way.

Practical Example: Using an OCI Registry
Let’s say you have a Docker image you want to store in an OCI-compliant registry (like Docker Hub). Here’s how you’d typically do it:
    Tag the Image: Assign a tag to your image.
docker tag my-app:latest myregistry.com/my-app:latest
  Push the Image to the Registry:
docker push myregistry.com/my-app:latest
  Pull the Image: Anyone with access to the registry can now pull the image.
docker pull myregistry.com/my-app:latest

OCI Registries standardize these commands across platforms, ensuring compatibility and making it easy to switch between registries.




## Step-02: Review Helm Chart
```t
# Create Chart
helm create myocidemo

# values.yaml: Update service to NodePort
service:
  type: NodePort
  port: 80

# values.yaml: Change Docker Image to kubenginxhelm
image:
  repository: ghcr.io/stacksimplify/kubenginxhelm

# Chart.yaml: update appversion
version: 0.1.0
appVersion: "0.1.0"

# Package Helm Chart
cd 47-Helm-Use-OCI-based-Registries
helm package myocidemo
Observation:
Will create package with file name "myocidemo-0.1.0.tgz"
```




## Step-03: OCI Registry: Docker Hub
```t
# SigUp and SignIn to Docker Hub
https://hub.docker.com

# command line: docker login
docker login
Username: xxxxxxxxx
Password: xxxxxxxxx

# Push Helm Chart to Docker Hub
cd 47-Helm-Use-OCI-based-Registries
helm push <HELM-PACKAGE>  oci://registry-1.docker.io/<DOCKER-NAMESPACE>
helm push myocidemo-0.1.0.tgz  oci://registry-1.docker.io/nileshzarkar

# Verify ons Docker Hub
Review Tabs
1. General
2. Tags
```




## Step-04: Update and Push Chart Version: 0.2.0
```t
# Package with Chart Version and App Version 0.2.0
helm package myocidemo --version "0.2.0" --app-version "0.2.0"

# Push Helm Chart to Docker Hub
helm push myocidemo-0.2.0.tgz  oci://registry-1.docker.io/nileshzarkar
```




## Step-05: Pull Helm Chart from OCI Registry
```t
# Create Directory
mkdir mypackages

# Helm Pull
helm pull oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0
helm pull oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.2.0
```




## Step-06: Helm Template and Show Commands
```t
# Helm Template Command
helm template <my-release> oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0
helm template myapp1 oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0
helm template myapp1 oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.2.0

# Helm Show Command
helm show all oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0
helm show all oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.2.0
```





## Step-07: Helm Install and Upgrade from OCI Registry
```t
# Helm Install
helm install <my-release> oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0
helm install myapp1 oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.1.0

# Helm Status
helm status myapp1 --show-resources 

# List k8s services
kubectl get svc

# Access Application
http://localhost:<get-from-svc-output>

# Helm Upgrade
helm upgrade <my-release> oci://registry-1.docker.io/nileshzarkar/myocidemo --version 0.2.0

# Helm Status
helm status myapp1 --show-resources 

# List k8s services
kubectl get svc

# Access Application
http://localhost:<get-from-svc-output>
```




## Step-08: Migrate from Classic Chart Repository to OCI Registry
```t
# List and add Helm Chart Repository
helm repo list
helm repo add mygithelmrepo https://stacksimplify.github.io/helm-charts-repo/
helm repo update

# Search Helm Repository
helm search repo myfirstchart
helm search repo myfirstchart --versions
# Create folder migrate
mkdir migrate
cd migrate

# Helm Pull (downloads latest chart version - in our case it is 0.2.0)
helm pull mygithelmrepo/myfirstchart

# Helm Pull --version (downloads specified chart version)
helm pull mygithelmrepo/myfirstchart --version 0.1.0

# Docker Login (if not logged in)
docker login

# Helm Push
helm push myfirstchart-0.1.0.tgz  oci://registry-1.docker.io/nileshzarkar
helm push myfirstchart-0.2.0.tgz  oci://registry-1.docker.io/nileshzarkar

# Verify on Docker Hub
https://hub.docker.com
```

