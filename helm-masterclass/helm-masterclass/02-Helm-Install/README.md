# Helm Install 

## Step-01: Introduction
- We will use the following commands as part of this demo
- helm repo list
- helm repo add
- helm repo update
- helm search repo
- helm install
- helm list
- helm uninstall 




## Step-02: List, Add and Search Helm Repository
- [Bitnami Applications packaged using Helm](https://bitnami.com/stacks/helm)
- [Search for Helm Charts at Artifacthub.io](https://artifacthub.io/)

```t
# List Helm Repositories
helm repo list

# Add Helm Repository
helm repo add <DESIRED-NAME> <HELM-REPO-URL>
helm repo add htmlrepo https://nileshzarkar.github.io/helm-charts/

# List Helm Repositories
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\02-Helm-Install> helm repo list
NAME            URL
mybitnami       https://charts.bitnami.com/bitnami
stacksimplify   https://stacksimplify.github.io/helm-charts/
htmlrepo        https://nileshzarkar.github.io/helm-charts/

# Search Helm Repository
helm search repo <KEY-WORD>
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\02-Helm-Install> helm search repo htmlpage
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
htmlrepo/htmlpage       4.0.0           4.0.0           A Helm chart for Kubernetes
```




## Step-03: Install Helm Chart
- Installs the Helm Chart
```t
# Update Helm Repo
helm repo update  # Make sure we get the latest list of charts

# Install Helm Chart
helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>
helm install htmlpage htmlrepo/htmlpage
```




## Step-04: List Helm Releases
- This command lists all of the releases for a specified namespace
```t
# List Helm Releases (Default Table Output)
helm list 
helm ls

# List Helm Releases (YAML Output)
helm list --output=yaml

# List Helm Releases (JSON Output)
helm list --output=json

# List Helm Releases with namespace flag
helm list --namespace=default
helm list -n default
```




## Step-05: List Kubernetes Resources
```t
# List Kubernetes Pods
kubectl get pods

# List Kubernetes Services
kubectl get svc
Observation: Review the EXTERNAL-IP field and you will see it as localhost. Access the nginx page from local desktop localhost

# Access Nginx Application on local desktop browser
http://localhost:30082/page
http://127.0.0.1:30082/page

# Access Application using curl command
curl http://localhost:30082/page
curl http://127.0.0.1:30082/page
```




## Step-06: Uninstall Helm Release - NO FLAGS
```t
# List Helm Releases
helm ls

# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall htmlpage 
```
