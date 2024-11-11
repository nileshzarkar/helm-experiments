# helm-masterclass





Documents steps to install kubectl
==================================
Verify if kubectl installed (Docker desktop should install kubectl automatically)
linux and windows 

# Verify kubectl version
kubectl version 
kubectl version --client --output=yaml

# List Config Contexts
kubectl config get-contexts

# Config Current Context
kubectl config current-context

# Config Use Context (Only if someother context is present in current-context output)
kubectl config use-context docker-desktop

# List Kubernetes Nodes
kubectl get nodes





Deploy sample apps
===================
https://github.com/stacksimplify/helm-masterclass/blob/main/01-Install-Docker-Desktop-and-HelmCLI/kube-manifests/deployment.yaml
https://github.com/stacksimplify/helm-masterclass/blob/main/01-Install-Docker-Desktop-and-HelmCLI/kube-manifests/service.yaml
Download and store these files at location : D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\kube-manifests

# Deploy k8s Resources to Docker Desktop k8s Cluster
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> kubectl apply -f .\kube-manifests\
deployment.apps/myapp1-deployment created
service/myapp1-nodeport-service created

kubectl apply -f kube-manifests/

# List k8s Deployments
kubectl get deploy

# List k8s pods
kubectl get pods

# List k8s Services
kubectl get svc

# Access Application
http://localhost:31300
or
http://127.0.0.1:31300

# Uninstall k8s Resources from Docker Desktop k8s cluster
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> kubectl delete -f kube-manifests/
deployment.apps "myapp1-deployment" deleted
service "myapp1-nodeport-service" deleted

kubectl delete -f kube-manifests/

# List pods, svc, deploy
kubectl get pods
kubectl get svc
kubectl get deploy





Documents steps to install helm cli
===================================
linux and windows 

# MacOS
brew install helm

# From Chocolatey (Windows)
choco install kubernetes-helm

# From Scoop (Windows)
scoop install helm

# Verify Helm version
helm version

# Helm Environment variables
helm env


What is bitnami
===============
In the context of Helm, Bitnami provides a collection of pre-built Helm charts for popular applications and services. These charts are essentially templates that define how an application should be deployed and configured on Kubernetes.

How Bitnami Helm Charts Work:

    Ready-Made Helm Charts: Bitnami offers Helm charts for applications like MySQL, WordPress, NGINX, and many others, saving you from building charts from scratch.

    Easy Deployment on Kubernetes: You can use Bitnami’s Helm charts to quickly set up these applications in your Kubernetes cluster with simple commands, making it much easier to install, update, and manage applications.

    Regular Updates: Bitnami keeps these charts up-to-date with the latest versions and security patches, so your deployments are more secure and stable.

Example Use:

If you want to deploy WordPress, you can add the Bitnami repository to Helm and then install it:

helm repo add bitnami https://charts.bitnami.com/bitnami
helm install my-wordpress bitnami/wordpress
	
Bitnami’s Helm charts simplify Kubernetes deployments by providing reliable, pre-configured setups that you can customize to suit your needs.

Helm repo, search, list, install and Uninstall
==============================================
List, Add and Search Helm Repository

Add bitnami helm repo to our local desktop
# List Helm Repositories
helm repo list

# Add Helm Repository
helm repo add <DESIRED-NAME> <HELM-REPO-URL>
helm repo add mybitnami https://charts.bitnami.com/bitnami

# List Helm Repositories
helm repo list

# Search Helm Repository
helm search repo <KEY-WORD>
helm search repo nginx
helm search repo apache
helm search repo wildfly


Installs the Helm Chart

# Update Helm Repo
helm repo update  # Make sure we get the latest list of charts

# Install Helm Chart
helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>
helm install mynginx mybitnami/nginx

List Helm Releases
This command lists all of the releases for a specified namespace

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

List Kubernetes Resources

# List Kubernetes Pods
kubectl get pods

# List Kubernetes Services
kubectl get svc
Observation: Review the EXTERNAL-IP field and you will see it as localhost. Access the nginx page from local desktop localhost

# Access Nginx Application on local desktop browser
http://localhost:80
http://127.0.0.1:80

# Access Application using curl command
curl http://localhost:80
curl http://127.0.0.1:80

Uninstall Helm Release - NO FLAGS

# List Helm Releases
helm ls

# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall mynginx 

Helm upgrade with --set Option
==============================

Add Custom Helm Repo

# List Helm Repositories
helm repo list

# Add Helm Repository
helm repo add <DESIRED-NAME> <HELM-REPO-URL>
helm repo add stacksimplify https://stacksimplify.github.io/helm-charts/

# List Helm Repositories
helm repo list

# Search Helm Repository
helm search repo <KEY-WORD>
helm search repo mychart1

Install Helm Chart from our Custom Helm Repository

# Install myapp1 Helm Chart
helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>
helm install myapp1 stacksimplify/mychart1 

List Resources and Access Application in Browser

# List Helm Release
helm ls 
or 
helm list

# List Pods
kubectl get pods

# List Services 
kubectl get svc

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231

Helm Upgrade

# Review the Docker Image Versions we are using
https://github.com/users/stacksimplify/packages/container/package/kubenginx
Image Tags: 1.0.0, 2.0.0, 3.0.0, 4.0.0

# Helm Upgrade
helm upgrade <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name> --set <OVERRIDE-VALUE-FROM-values.yaml>
helm upgrade myapp1 stacksimplify/mychart1 --set "image.tag=2.0.0"
or
helm upgrade myapp1 stacksimplify/mychart1 --set "replicaCount=4"

List Resources after helm upgrade

# List Helm Releases
helm list 
Observation: We should see Application Version: V2

# Additional List commands
helm list --superseded
The command helm list --superseded lists all Helm releases that have been superseded. These are releases that were previously installed but have since been upgraded, so they're no longer the latest version.
In short, this command helps you view old releases that have been replaced by newer versions, which can be useful for tracking release history or managing cleanup of outdated resources.

helm list --deployed
The command helm list --deployed lists all Helm releases in the Kubernetes cluster that are currently in a deployed state. This filters the list to show only releases that are active and running, excluding those that are pending, failed, or superseded by upgrades.
It's helpful for quickly viewing only the running applications managed by Helm

# List and Describe Pod
kubectl get pods
kubectl describe pod <POD-NAME> 
Observation: In the Pod Events you should find that "ghcr.io/stacksimplify/kubenginx:2.0.0" is pulled or if already exists on desktop 
it will be used to create this new pod.

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231
Observation: Application Version: V2 of application should be displayed 
 
Do two more helm upgrades - For practice purpose

# Helm Upgrade to 3.0.0
helm upgrade myapp1 stacksimplify/mychart1 --set "image.tag=3.0.0"
# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231

# Helm Upgrade to 4.0.0
helm upgrade myapp1 stacksimplify/mychart1 --set "image.tag=4.0.0"

# Access Application
http://localhost:<NODE-PORT>
http://localhost:31231

Helm History
History prints historical revisions for a given release

# helm history
helm history RELEASE_NAME
helm history myapp1

Helm Status

# Helm Status
helm status RELEASE_NAME
helm status myapp1

# Helm Status - Show Description (display the description message of the named release)
helm status myapp1 --show-desc  

# Helm Status - Show Resources (display the resources of the named release)
helm status myapp1  --show-resources

# Helm Status - revision (display the status of the named release with revision)
helm status RELEASE_NAME --revision int
helm status myapp1 --revision 2

Uninstall Helm Release

# Uninstall Helm Release
helm uninstall myapp1

helm install, upgrade with chart version
========================================

Search Helm Repo for mychart2
https://github.com/stacksimplify/helm-charts/tree/main
https://artifacthub.io/packages/helm/stacksimplify/mychart2/
mychart2 has 4 chart versions (0.1.0, 0.2.0, 0.3.0, 0.4.0)
mychart2 Chart Versions -> App Version
0.1.0 -> 1.0.0
0.2.0 -> 2.0.0
0.3.0 -> 3.0.0
0.4.0 -> 4.0.0

https://github.com/stacksimplify/helm-charts/tree/main/mychart2

# Search Helm Repo
helm search repo mychart2
Observation: Should display latest version of mychart2 from stacksimplify helm repo

# Search Helm Repo with --versions
helm search repo mychart2 --versions
Observation: Should display all versions of mychart2

PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> helm search repo mychart2 --versions
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
stacksimplify/mychart2  0.4.0           4.0.0           A Helm chart for Kubernetes
stacksimplify/mychart2  0.3.0           3.0.0           A Helm chart for Kubernetes
stacksimplify/mychart2  0.2.0           2.0.0           A Helm chart for Kubernetes
stacksimplify/mychart2  0.1.0           1.0.0           A Helm chart for Kubernetes

# Search Helm Repo with --version
helm search repo mychart2 --version "CHART-VERSIONS"
helm search repo mychart2 --version "0.2.0"
Observation: Should display specified version of helm chart 

PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> helm search repo mychart2 --version "0.2.0"
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
stacksimplify/mychart2  0.2.0           2.0.0           A Helm chart for Kubernetes

Install Helm Chart by specifying Chart Version

# Install Helm Chart by specifying Chart Version
helm install myapp101 stacksimplify/mychart2 --version "CHART-VERSION"
helm install myapp101 stacksimplify/mychart2 --version "0.1.0"

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:31232

# View Pod logs
kubectl get pods
kubectl logs -f POD-NAME

PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> kubectl logs -f myapp101-mychart2-779c8d5bc9-54slh
10.1.0.1 - - [25/Oct/2024:16:46:30 +0000] "GET / HTTP/1.1" 200 218 "-" "kube-probe/1.29" "-"
10.1.0.1 - - [25/Oct/2024:16:46:38 +0000] "GET / HTTP/1.1" 200 218 "-" "kube-probe/1.29" "-"
....

Helm Upgrade using Chart Version

# Helm Upgrade using Chart Version
helm upgrade myapp101 stacksimplify/mychart2 --version "0.2.0"

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:31232

# List Release History
helm history myapp101

Helm Upgrade without Chart Version

# Helm Upgrade using Chart Version
helm upgrade myapp101 stacksimplify/mychart2

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:31232
Observation: Should take the latest release which is Appversion 4.0.0, Chart Version 0.4.0 (Which is default or latest Chart version)
Application Version: V4

# List Release History
helm history myapp101

Helm Rollback
Roll back a release to a previous revision or a specific revision

# Rollback to previous version
helm rollback RELEASE-NAME 
helm rollback myapp101

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:31232
Observation: Should see V2 version of Application (Chart Version 0.2.0, AppVersion 2.0.0)
Application Version: V2

# List Release History
helm history myapp101

Helm Rollback to specific Revision

Roll back a release to a previous revision or a specific revision

# Rollback to previous version
helm rollback RELEASE-NAME REVISION
helm rollback myapp101 1

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:31232
Observation: Should see V1 version of Application (Chart Version 0.1.0, AppVersion 1.0.0)
Application Version: V1

# List Release History
helm history myapp101


NOTE : DO NOT UNINSTALL

Helm Uninstall with --keep-history flag
=======================================

We will learn to uninstall Helm Release in a most effective way (best practice) so that we don't loose the history of our Helm Release
Important Note: This demo is in continuation to previous release demo

Uninstall Helm Release with --keep-history Flag

# List Helm Releases
helm list
helm list --superseded
helm list --deployed

# List Release History
helm history myapp101

# Uninstall Helm Release with --keep-history Flag
helm uninstall <RELEASE-NAME> --keep-history
helm uninstall myapp101 --keep-history

# List Helm Releases which are uninstalled
helm list --uninstalled
Observation:
We should see uninstalled release
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> helm list --uninstalled
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART           APP VERSION
myapp101        default         5               2024-10-26 00:57:08.2393241 +0530 IST   uninstalled     mychart2-0.1.0  1.0.0

# helm status command
helm status myapp101
Observation:
1. works only when we use --keep-history flag
2. We can see all the details of release with "Status: Uninstalled"

Rollback Uninstalled Release

# List Release History
helm history myapp101

# Rollback Helm Uninstalled Release
helm rollback <RELEASE> [REVISION] [flags]
helm rollback myapp101 3
Observation: It should rollback to specific revision number from revision history
Application Version: V4

# List Helm Releases
helm list

# List Kubernetes Resources
kubectl get pods
kubectl get svc

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application 
http://localhost:31232

Uninstall Helm Release - NO FLAGS

# List Helm Releases
helm list

# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall myapp101

# List Helm Releases which are uninstalled
helm list --uninstalled
Observation:
We should not see uninstalled release, this command will completely remove the release and its all references
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> helm list --uninstalled
NAME    NAMESPACE       REVISION        UPDATED STATUS  CHART   APP VERSION

# helm status command
helm status myapp101
Observation:
As the release is permanently removed, we dont get an error "Error: release: not found"

# List Helm History
helm history myapp101
Observation:
As the release is permanently removed, we dont get an error "Error: release: not found"

Rollback Uninstalled Release

# Rollback Helm Uninstalled Release
helm rollback <RELEASE> [REVISION] [flags]
helm rollback myapp101 1 
Observation: 
Should throw error "Error: release: not found"

Best Practice for Helm Uninstall

It is recommended to always use --keep-history Flag for following reasons
Keeping Track of uninstalled releases
Quick Rollback if that Release is required

Helm Install with --generate-name flag 
======================================

Introduction

--generate-name flag for helm install is very very important option
This is one of the good to know option from helm install perspective
When we are implementing DevOps Pipelines, if we want to generate the names of our releases without throwing 
duplicate release errors we can use this setting.

Install helm with --generate-name flag

# Install helm with --generate-name flag
helm install <repo_name_in_your_local_desktop/chart_name> --generate-name
helm install stacksimplify/mychart1 --generate-name

# List Helm Releases
helm list
helm list --output=yaml
Observation:
We can see the name as "name: mychart1-1729885539" some auto-generated number

# Helm Status
helm status mychart1-1729885539
helm status mychart1-1729885539 --show-resources

PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass> helm status mychart1-1729885539 --show-resources
NAME: mychart1-1729885539
LAST DEPLOYED: Sat Oct 26 01:15:40 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
RESOURCES:
==> v1/Service
NAME                  TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)        AGE
mychart1-1729885539   NodePort   10.100.177.180   <none>        80:31231/TCP   89s

==> v1/Deployment
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
mychart1-1729885539   1/1     1            1           89s

==> v1/Pod(related)
NAME                                  READY   STATUS    RESTARTS   AGE
mychart1-1729885539-9587b86f7-fmxpb   1/1     Running   0          89s

# Access Application
http://localhost:31231

Uninstall Helm Release

# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall mychart1-1729885539

Helm Install with --atomic flag 
===============================
Introduction

We will learn to use --atomic flag when installing the Helm Release and also understand the importance of using it in a practical way

Install Helm Chart - Release: dev101

# Install Helm Chart 
helm install dev101 stacksimplify/mychart1

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status dev101 --show-resources

# Access Application
http://localhost:31231

Install Helm Chart - Release: qa101

# Install Helm Chart 
helm install qa101 stacksimplify/mychart1

# List Helm Release
helm list 
Observation: You should see qa101 release installed with FAILED status

Error: INSTALLATION FAILED: 1 error occurred:
	* Service "qa101-mychart1" is invalid: spec.ports[0].nodePort: Invalid value: 31231: provided port is already allocated

# Uninstall qa101 release which is in failed state
helm uninstall qa101

# List Helm Release
helm list 

Install Helm Chart - Release: qa101 with --atomic flag

when --atomic flagis set, the installation process deletes the installation on failure.
The --wait flag will be set automatically if --atomic is used
--wait will wait until all Pods, PVCs, Services, and minimum number of Pods of a Deployment, StatefulSet, or ReplicaSet are in a ready state before marking the release as successful. It will wait for as long as --timeout
--timeout time to wait for any individual Kubernetes operation (like Jobs for hooks) (default 5m0s)

# Install Helm Chart 
helm install qa101 stacksimplify/mychart1 --atomic

# List Helm Release
helm list 
Observation: We will not see qa101 FAILED release, --atomic flag deleted the release as soon as it is failed with error

Error: INSTALLATION FAILED: 1 error occurred:
	* Service "qa101-mychart1" is invalid: spec.ports[0].nodePort: Invalid value: 31231: provided port is already allocated
	
Uninstall dev101 Release

# Uninstall dev101 release
helm uninstall dev101

# List Helm Releases
helm list

Helm Install with Kubernetes namespace
======================================

Introduction

Any resource we manage using HELM are specific to Kubernetes Namespace
By default, Kubernetes resources deployed to k8s cluster using default namespace, so we don't need to specify namespace name explicitly
In the case if we want to deploy k8s resources to a namespace (other than default), then we need to specify that in helm install command with flag --namespace or -n
In addition, we can also create a namespace during helm install using flags --namespace --create-namespace

Install Helm Release by creating Kubernetes Namespace dev

# List Kubernetes Namespaces 
kubectl get ns

# Install Helm Release by creating Kubernetes Namespace
helm install dev101 stacksimplify/mychart2 --version "0.1.0" --namespace dev --create-namespace 

# List Kubernetes Namespaces 
kubectl get ns
Observation: Found the dev namespace created as part of `helm install`

# List Helm Release
helm list --> NO RELEASES in default namespace
helm list -n dev
helm list --namespace dev

# Helm Status
helm status dev101 --show-resources -n dev
helm status dev101 --show-resources --namespace dev

# List Kubernetes Pods
kubectl get pods -n dev
kubectl get pods --namespace dev

# List Services
kubectl get svc -n dev

# List Deployments
kubectl get deploy -n dev

# Access Application
http://localhost:31232

Application Version: V1

Run helm upgrade for resources present in dev namespace

# Helm Upgrade
helm upgrade dev101 stacksimplify/mychart2 --version "0.2.0" --namespace dev 
or
helm upgrade dev101 stacksimplify/mychart2 --version "0.2.0" -n dev

# List Helm Release
helm list -n dev
helm list --namespace dev

# Helm Status
helm status dev101 --show-resources -n dev
helm status dev101 --show-resources --namespace dev

# Access Application
http://localhost:31232

Application Version: V2

Uninstall Helm Release from dev Namespace

# Uninstall Helm Releas
helm uninstall dev101 --namespace dev
helm uninstall dev101 -n dev

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


Helm Overide Values --dry-run, --debug, get
===========================================

Introduction

We will learn the following in this section
        helm install --set
        helm upgrade -f myvalues.yaml
        --dry-run:
        --debug:
        helm get values
        helm get values --revision
        helm get manifest
        helm get manifest --revision
        helm get all
Discuss about Values Hierarchy

Override default NodePort 31231 with --set

==> Review our mychart1 Helm Chart values.yaml
https://github.com/stacksimplify/helm-charts/blob/main/mychart1/values.yaml
# This is a YAML-formatted file.
# Declare variables to be passed into your templates.
replicaCount: 1
image:
  repository: ghcr.io/stacksimplify/kubenginx
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: ""
nameOverride: ""
fullnameOverride: ""
podAnnotations: {}
service:
  type: NodePort
  port: 80
  nodePort: 31231

==> Learn about --dry-run and --debug flags for helm install command
Install Helm Chart by overriding NodePort 31231 with 31240
# Helm Install with --dry-run command
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 --dry-run 

# Helm Install with --dry-run and --debug command
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 --dry-run --debug

## THE BELOW IS THE SAMPLE OUTPUT WITH DEBUG ADDED
NAME: myapp901
NAMESPACE: default
STATUS: pending-install
REVISION: 1
	USER-SUPPLIED VALUES:
service:
  nodePort: 31240
COMPUTED VALUES:
fullnameOverride: ""
image:
  pullPolicy: IfNotPresent
  repository: ghcr.io/stacksimplify/kubenginx
  tag: ""
nameOverride: ""
podAnnotations: {}
replicaCount: 1
service:
  nodePort: 31240
  port: 80
  type: NodePort
  
==> helm install with --set and test
# Helm Install 
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 

# helm status --show-resources
helm status myapp901 --show-resources
Observation:
We can see that our NodePort service is running on port 31240

# Access Application
http://localhost:31240

Application Version: V1

==> Review myvalues.yaml 
created from https://github.com/stacksimplify/helm-charts/blob/main/mychart1/values.yaml
D:\Experiments\quarkus-microservice\Observability\myvalues.yaml
# Change-1: change replicas from 1 to 2
replicaCount: 2

# Change-2: Add tag as "2.0.0" which will override the default appversion "1.0.0" from our mychart1
image:
  repository: ghcr.io/stacksimplify/kubenginx
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: "2.0.0"

# Change-3: Change nodePort from 31240 to 31250
service:
  type: NodePort
  port: 80
  nodePort: 31250
  
==> Override NodePort 31240 with -f myvalues.yaml with
We can either -f  myvalues.yaml or --values myvalues.yaml. Both are valid inputs
# Verify if myvalues.yaml
cd 09-Helm-Override-Values
cat myvalues.yaml

# helm upgrade with --dry-run and --debug commands
helm upgrade myapp901 stacksimplify/mychart1 -f myvalues.yaml --dry-run --debug

# helm upgrade
helm upgrade myapp901 stacksimplify/mychart1 -f myvalues.yaml

# helm status
helm status myapp901 --show-resources
Observation: 
1. Two pods will be running as we changed replica count to 2
2. Service Node Port will be 31250 

# Access Application
http://localhost:31250
Observation: 
1. We should see V2 application because we have used the "image tag as 2.0.0"
Application Version: V2

==> helm get values command
helm get values: This command downloads a values file for a given release
# helm get values
helm get values RELEASE_NAME
helm get values myapp901
Observation:
1. Provides the values from current/latest release version 2 from Release myapp901

## Sample Output
PS D:\Experiments\quarkus-microservice\Observability> helm get values myapp901
USER-SUPPLIED VALUES:
image:
  pullPolicy: IfNotPresent
  repository: ghcr.io/stacksimplify/kubenginx
  tag: 2.0.0
replicaCount: 2
service:
  nodePort: 31250
  port: 80
  type: NodePort


# helm history (History prints historical revisions for a given release.)
helm history myapp901

# helm get values with --revision
helm get values RELEASE-NAME --revision int
helm get values myapp901 --revision 1

## Sample Output
PS D:\Experiments\quarkus-microservice\Observability> helm get values myapp901 --revision 1
USER-SUPPLIED VALUES:
service:
  nodePort: 31240
  
==> helm get manifest command
helm get manifest: This command fetches the generated manifest for a given release.

# helm get manifest
helm get manifest RELEASE-NAME
helm get manifest myapp901

# helm get manifest --revision
helm get manifest RELEASE-NAME --revision int
helm get manifest myapp901 --revision 1

==> helm get all command
helm get all: This command prints a human readable collection of information about the notes, hooks, supplied values, and generated manifest file of the given release.
This is a good way to see what templates are installed on the kubernetes cluster server.
helm get notes and helm get hooks: These two commmands we will explore when we are discussing about helm chart development.

# helm get all
helm get all RELEASE-NAME
helm get all myapp901

==> Uninstall Helm Release
# Uninstall Helm Release
helm uninstall myapp901

# List Helm Releases
helm list

==> Values Hierarchy
Sub chart values.yaml can be overriden by parents chart values.yaml
Parent charts values.yaml can be overriden by user-supplied value file (-f myvalues.yaml)
User-supplied value file (-f myvalues.yaml) can be overriden by --set parameters

==> Deleting a default Key by passing null
If you need to delete a key from the default values, you may override the value of the key to be null, 
in which case Helm will remove the key from the overridden values merge.

# Release: myapp901
helm install myapp901 stacksimplify/mychart1 --atomic
helm list
helm status myapp901 --show-resources
http://localhost:31231
Application Version: V1

# Release: myapp902
helm install myapp902 stacksimplify/mychart1 --atomic
helm list
Failed :  * Service "myapp902-mychart1" is invalid: spec.ports[0].nodePort: Invalid value: 31231: provided port is already allocated

# Option-1: Give desired port other than 31231
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=31232 

# Option-2: Pass null value to nodePort (service.nodePort=null)
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=null --dry-run --debug
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=null 

# Additional Notes for understanding
1. We will choose option-2 to demonstrate the concept "Deleting a default Key by passing null"
2. For NodePort Service, if we dont define the "nodePort" argument, it by default assigns a port dynamically from the port range 30000-32767. 
3. In our case already 31231 is used, other than that port it will allocate someother port when we pass null. 
4. In short, if we dont want to pass the default values present in values.yaml as-is, we dont need to change the complete chart with a new version, we can just pass null.

# Uninstall Releases
helm uninstall myapp901
helm uninstall myapp902
helm list





