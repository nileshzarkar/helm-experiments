# Helm Upgrade with set option

## Step-01: Introduction
- We are going to upgrade the HELM RELEASE using `helm upgrade` command in combination with `--set "image.tag=<DOCKER-IMAGE-TAGS>`
- We will use the following Helm Commands in this demo.
- helm repo 
- helm search repo
- helm install
- helm upgrade
- helm history
- helm status




## Step-02: Custom Helm Repo
### Step-02-01: Review our Custom Helm Repo
- https://nileshzarkar.github.io/helm-charts/
- https://hub.docker.com/repository/docker/nileshzarkar/htmlpage/tags




### Step-02-02: Add Custom Helm Repo
```t
# List Helm Repositories
helm repo list

# Add Helm Repository
helm repo add <DESIRED-NAME> <HELM-REPO-URL>
D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option> helm repo add helm-repo https://nileshzarkar.github.io/helm-charts/

# List Helm Repositories
helm repo list

# Search Helm Repository
helm search repo <KEY-WORD>
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option> helm search repo htmlpage
NAME                    CHART VERSION   APP VERSION     DESCRIPTION
helm-repo/htmlpage      4.0.0           4.0.0           A Helm chart for Kubernetes
```




## Step-03: Install Helm Chart from our Custom Helm Repository
```t
# Install myapp1 Helm Chart
helm install <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name>
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option> helm install htmlpage helm-repo/htmlpage
NAME: htmlpage
LAST DEPLOYED: Sun Nov 10 17:30:56 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT
```




## Step-04: List Resources and Access Application in Browser
```t
# List Helm Release
helm ls 
or 
helm list
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option> helm list
NAME            NAMESPACE       REVISION        UPDATED                                 STATUS          CHART           APP VERSION
htmlpage        default         1               2024-11-10 21:47:05.3256899 +0530 IST   deployed        htmlpage-2.0.0  2.0.0

# List Pods
kubectl get pods

# List Services 
kubectl get svc

# Access Application
http://localhost:<NODE-PORT>
http://localhost:30082/page
```




## Step-04: Helm Upgrade
- [kubenginx Docker Image with 1.0.0, 2.0.0, 3.0.0, 4.0.0](https://hub.docker.com/repository/docker/nileshzarkar/htmlpage/tags)
```t

# Review the Docker Image Versions we are using
https://hub.docker.com/repository/docker/nileshzarkar/htmlpage/tags
Image Tags: 1.0.0, 2.0.0, 3.0.0, 4.0.0

# Helm Upgrade
helm upgrade <RELEASE-NAME> <repo_name_in_your_local_desktop/chart_name> --set <OVERRIDE-VALUE-FROM-values.yaml>
helm upgrade htmlpage helm-repo/htmlpage --set "config.pageColor=orange"

The --set flag allows you to override chart values directly from the command line. In this case, it’s setting the config.pageColor value to orange. This means that the updated release will deploy update the text colour.
```




## Step-05: List Resources after helm upgrade
```t
# List Helm Releases
helm list 
Observation: We should see Revision as 2

# Additional List commands
helm list --superseded

# List and Describe Pod
kubectl get pods
kubectl describe pod <POD-NAME> 
Observation: 
In the Pod Events you should find that "nileshzarkar/htmlpage" is pulled or if already exists on desktop it will be used to create this new pod

# Access Application
http://localhost:<NODE-PORT>
http://localhost:30028/page
The config color is blue
```

## Step-06: Do two more helm upgrades - For practice purpose
```t
# Helm Upgrade to 3.0.0
helm upgrade htmlpage helm-repo/htmlpage --set "image.tag=3.0.0"
The config color is green
But Image tag is 3.0.0

# Access Application
http://localhost:<NODE-PORT>
http://localhost:30082/page

# Helm Upgrade to 4.0.0
helm upgrade htmlpage helm-repo/htmlpage --set "image.tag=4.0.0"
The config color is green
But Image tag is 4.0.0

# Access Application
http://localhost:<NODE-PORT>
http://localhost:30082/page
```

## Step-07: Helm History
- History prints historical revisions for a given release.
```t
# helm history
helm history RELEASE_NAME
helm history htmlpage

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option>helm history htmlpage
REVISION        UPDATED                         STATUS          CHART           APP VERSION     DESCRIPTION
1               Mon Nov 11 09:34:06 2024        superseded      htmlpage-1.0.0  1.0.0           Install complete
2               Mon Nov 11 09:36:47 2024        superseded      htmlpage-4.0.0  4.0.0           Upgrade complete
3               Mon Nov 11 09:38:10 2024        deployed        htmlpage-4.0.0  4.0.0           Upgrade complete

```

## Step-08: Helm Status
- This command shows the status of a named release. 
```t
# Helm Status
helm status RELEASE_NAME
helm status htmlpage

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option>helm status htmlpage
NAME: htmlpage
LAST DEPLOYED: Mon Nov 11 09:38:10 2024
NAMESPACE: default
STATUS: deployed
REVISION: 3
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# Helm Status - Show Description (display the description message of the named release)
helm status htmlpage --show-desc    

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option>helm status htmlpage --show-desc
NAME: htmlpage
LAST DEPLOYED: Mon Nov 11 09:38:10 2024
NAMESPACE: default
STATUS: deployed
REVISION: 3
DESCRIPTION: Upgrade complete
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# Helm Status - Show Resources (display the resources of the named release)
helm status htmlpage  --show-resources   

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option>helm status htmlpage  --show-resources
NAME: htmlpage
LAST DEPLOYED: Mon Nov 11 09:38:10 2024
NAMESPACE: default
STATUS: deployed
REVISION: 3
RESOURCES:
==> v1/Service
NAME       TYPE       CLUSTER-IP       EXTERNAL-IP   PORT(S)          AGE
htmlpage   NodePort   10.104.168.158   <none>        8080:30082/TCP   6m58s

==> v1/Deployment
NAME       READY   UP-TO-DATE   AVAILABLE   AGE
htmlpage   1/1     1            1           6m58s

==> v1/Pod(related)
NAME                       READY   STATUS    RESTARTS   AGE
htmlpage-7dfb8ccff-979hp   1/1     Running   0          2m54s


NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# Helm Status - revision (display the status of the named release with revision)
helm status RELEASE_NAME --revision int
helm status htmlpage --revision 2

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\03-Helm-Upgrade-with-set-option>helm status htmlpage --revision 2
NAME: htmlpage
LAST DEPLOYED: Mon Nov 11 09:36:47 2024
NAMESPACE: default
STATUS: superseded
REVISION: 2
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT
```

## Step-09: Uninstall Helm Release
```t
# Uninstall Helm Release
helm uninstall htmlpage
```



