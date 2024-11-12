# Helm Install Atomic Flag




## Step-01: Introduction
- We will learn to use `--atomic` flag when installing the Helm Release and also understand the importance of using it in a practical way




## Step-02: Install Helm Chart - Release: dev101
```t
# Install Helm Chart 
helm install htmlpage helm-repo/htmlpage

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status htmlpage --show-resources

# Access Application
http://localhost:30082/page
```




## Step-03: Install Helm Chart - Release: qa101
```t
# Install Helm Chart 
helm install myapp helm-repo/htmlpage

# List Helm Release
helm list 
Observation: You should see myapp release installed with FAILED status

Error: INSTALLATION FAILED: 1 error occurred:
        * Service "myapp-htmlpage" is invalid: spec.ports[0].nodePort: Invalid value: 30082: provided port is already allocated

# Uninstall qa101 release which is in failed state
helm uninstall myapp

# List Helm Release
helm list 
```




## Step-04: Install Helm Chart - Release: qa101 with --atomic flag
- when `--atomic` flagis set, the installation process deletes the installation on failure. 
- The `--wait` flag will be set automatically if `--atomic` is used
- `--wait` will wait until all Pods, PVCs, Services, and minimum number of Pods of a Deployment, StatefulSet, or ReplicaSet are in a ready state before marking the release as successful. It will wait for as long as `--timeout`
- `--timeout`  time to wait for any individual Kubernetes operation (like Jobs for hooks) (default 5m0s)
```t
# Install Helm Chart 
helm install myapp helm-repo/htmlpage --atomic

# List Helm Release
helm list 
Observation: We will not see myapp FAILED release, --atomic flag deleted the release as soon as it is failed with error

Error: INSTALLATION FAILED: release myapp failed, and has been uninstalled due to atomic being set: 1 error occurred:
        * Service "myapp-htmlpage" is invalid: spec.ports[0].nodePort: Invalid value: 30082: provided port is already allocated
```
