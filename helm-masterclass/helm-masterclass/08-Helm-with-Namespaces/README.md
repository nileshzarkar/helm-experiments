# Helm with Kubernetes Namespaces




## Step-01: Introduction
- Any resource we manage using HELM are specific to Kubernetes Namespace
- By default, Kubernetes resources deployed to k8s cluster using default namespace, so we don't need to specify namespace name explicitly
- In the case if we want to deploy k8s resources to a namespace (other than default), then we need to specify that in `helm install` command with flag `--namespace` or `-n`
- In addition, we can also create a namespace during `helm install` using flags `--namespace`  `--create-namespace` 




## Step-02: Install Helm Release by creating Kubernetes Namespace dev
```t
# List Kubernetes Namespaces 
kubectl get ns

# Install Helm Release by creating Kubernetes Namespace
helm install htmlpage helm-repo/htmlpage --version "1.0.0" --namespace dev --create-namespace 

# List Kubernetes Namespaces 
kubectl get ns
Observation: Found the dev namespace created as part of `helm install`

# List Helm Release
helm list --> NO RELEASES in default namespace
helm list -n dev
helm list --namespace dev

PS D:\Experiments\helm-experiments> helm list --namespace dev
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART           APP VERSION
htmlpage        dev             1               2024-11-12 05:49:46.4620948 +0530 IST   deployed        htmlpage-1.0.0  1.0.0
PS D:\Experiments\helm-experiments> helm list -n dev
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART           APP VERSION
htmlpage        dev             1               2024-11-12 05:49:46.4620948 +0530 IST   deployed        htmlpage-1.0.0  1.0.0

# Helm Status
helm status htmlpage --show-resources -n dev
helm status htmlpage --show-resources --namespace dev

# List Kubernetes Pods
kubectl get pods -n dev
kubectl get pods --namespace dev

# List Services
kubectl get svc -n dev

# List Deployments
kubectl get deploy -n dev

# Access Application
http://localhost:30082/page
```




## Step-03: Run helm upgrade for resources present in dev namespace
```t
# Helm Upgrade
helm upgrade htmlpage helm-repo/htmlpage --version "2.0.0" --namespace dev 
or
helm upgrade htmlpage helm-repo/htmlpage --version "2.0.0" -n dev

PS D:\Experiments\helm-experiments> helm upgrade htmlpage helm-repo/htmlpage --version "2.0.0" -n dev
Release "htmlpage" has been upgraded. Happy Helming!
NAME: htmlpage
LAST DEPLOYED: Tue Nov 12 05:52:01 2024
NAMESPACE: dev
STATUS: deployed
REVISION: 2
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace dev -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace dev -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# List Helm Release
helm list -n dev
helm list --namespace dev

# Helm Status
helm status htmlpage --show-resources -n dev
helm status htmlpage --show-resources --namespace dev

# Access Application
http://localhost:30082/page
```




## Step-04: Uninstall Helm Release from dev Namespace
```t
# Uninstall Helm Releas
helm uninstall htmlpage --namespace dev
helm uninstall htmlpage -n dev

# List Helm Release
helm list -n dev
helm list --namespace dev

# List Kubernetes Namespaces
kubectl get ns
Observation: 
1. When uninstalling helm release, it will not delete the Kubernetes Resource: dev namespace. 
2. If we dont need that dev namespace we need to manually delete it from kubernetes using kubectl

# Delete dev namespace
kubectl delete ns dev

# List Kubernetes Namespaces
kubectl get ns
```