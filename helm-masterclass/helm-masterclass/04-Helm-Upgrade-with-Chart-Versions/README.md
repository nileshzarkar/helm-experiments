# Helm Upgrade with Chart Versions



## Step-01: Introduction
- We are going to learn some additional flags for `helm search repo` command
- We are going to Install and Upgrade Helm Releases using Chart Versions
- In addition, we are going to learn about Helm Rollback 
- helm install
- helm search repo
- helm status
- helm upgrade
- helm rollback
- helm history




## Step-02: Search Helm Repo for htmlpage
- [Review htmlpage in Github Repo](hhttps://github.com/nileshzarkar/helm-charts/tags)
- htmlpage has 4 chart versions (1.0.0, 2.0.0, 3.0.0, 4.0.0)
- htmlpage Chart Versions -> App Version
- 1.0.0 -> 1.0.0
- 2.0.0 -> 2.0.0
- 3.0.0 -> 3.0.0
- 4.0.0 -> 4.0.0
```t

# Search Helm Repo
helm search repo htmlpage

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm search repo htmlpage
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
helm-repo/htmlpage      4.0.0           4.0.0           A Helm chart for Kubernetes

Observation: Should display latest version of htmlpage from htmlpage helm repo

# Search Helm Repo with --versions
helm search repo htmlpage --versions

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm search repo htmlpage --versions
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
helm-repo/htmlpage      4.0.0           4.0.0           A Helm chart for Kubernetes
helm-repo/htmlpage      3.0.0           3.0.0           A Helm chart for Kubernetes
helm-repo/htmlpage      2.0.0           2.0.0           A Helm chart for Kubernetes
helm-repo/htmlpage      1.0.0           1.0.0           A Helm chart for Kubernetes

Observation: Should display all versions of htmlpage

# Search Helm Repo with --version
helm search repo htmlpage --version <CHART-VERSIONS>
helm search repo htmlpage --version "2.0.0"

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm search repo htmlpage --version "2.0.0"
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
helm-repo/htmlpage      2.0.0           2.0.0           A Helm chart for Kubernetes

Observation: Should display specified version of helm chart 
```




## Step-03: Install Helm Chart by specifying Chart Version
```t
# Install Helm Chart by specifying Chart Version
helm install htmlpgae helm-repo/htmlpage --version <CHART-VERSION>
helm install htmlpage helm-repo/htmlpage --version "1.0.0"

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status htmlpage --show-resources

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm status htmlpage --show-resources
NAME: htmlpage
LAST DEPLOYED: Mon Nov 11 17:05:46 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
RESOURCES:
==> v1/Service
NAME       TYPE       CLUSTER-IP      EXTERNAL-IP   PORT(S)          AGE
htmlpage   NodePort   10.109.211.78   <none>        8080:30082/TCP   30s

==> v1/Deployment
NAME       READY   UP-TO-DATE   AVAILABLE   AGE
htmlpage   1/1     1            1           29s

==> v1/Pod(related)
NAME                        READY   STATUS    RESTARTS   AGE
htmlpage-86cc96899d-6rvnn   1/1     Running   0          29s


NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# Access Application
http://localhost:30082/page

# View Pod logs
kubectl get pods
kubectl logs -f POD-NAME
```




## Step-04: Helm Upgrade using Chart Version
```t
# Helm Upgrade using Chart Version
helm upgrade myapp101 stacksimplify/mychart2 --version "0.2.0"

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status htmlpage --show-resources

# Access Application
http://localhost:31232

# List Release History
helm history htmlpage

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm history h
tmlpage
REVISION        UPDATED                         STATUS          CHART           APP VERSION     DESCRIPTION
1               Mon Nov 11 17:05:46 2024        deployed        htmlpage-1.0.0  1.0.0           Install complete
```




## Step-05: Helm Upgrade without Chart Version
```t
# Helm Upgrade using Chart Version
helm upgrade htmlpage helm-repo/htmlpage

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status htmlpage --show-resources

# Access Application
http://localhost:30082/page
Observation: Should take the latest release which is Appversion 4.0.0, Chart Version 0.4.0 (Which is default or latest Chart version)

# List Release History
helm history htmlpage
```




## Step-06: Helm Rollback
- Roll back a release to a previous revision or a specific revision
```t
# Rollback to previous version
helm rollback RELEASE-NAME 
helm rollback htmlpage

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status htmlpage --show-resources

# Access Application
http://localhost:30082/page
Observation: Should see V2 version of Application (Chart Version 0.2.0, AppVersion 2.0.0)

# List Release History
helm history htmlpage
```




## Step-07: Helm Rollback to specific Revision
- Roll back a release to a previous revision or a specific revision
```t
# Rollback to previous version
helm rollback RELEASE-NAME REVISION
helm rollback htmlpage 1

# List Helm Release
helm list 

# List Kubernetes Resources Deployed as part of this Helm Release
helm status myapp101 --show-resources

# Access Application
http://localhost:30082/page
Observation: Should see V1 version of Application (Chart Version 0.1.0, AppVersion 1.0.0)

# List Release History
helm history htmlpage

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\04-Helm-Upgrade-with-Chart-Versions>helm history htmlpage
REVISION        UPDATED                         STATUS          CHART           APP VERSION     DESCRIPTION
1               Mon Nov 11 17:05:46 2024        superseded      htmlpage-1.0.0  1.0.0           Install complete
2               Mon Nov 11 17:09:40 2024        superseded      htmlpage-4.0.0  4.0.0           Upgrade complete
3               Mon Nov 11 17:10:36 2024        deployed        htmlpage-1.0.0  1.0.0           Rollback to 1

```



