# Helm Install with Generate Name Flag



## Step-01: Introduction
- `--generate-name` flag for `helm install` is very very important option
- This is one of the good to know option from `helm install` perspective
- When we are implementing DevOps Pipelines, if we want to generate the names of our releases without throwing duplicate release errors we can use this setting. 



## Step-02: Install helm with --generate-name flag
```t
# Install helm with --generate-name flag
helm install <repo_name_in_your_local_desktop/chart_name> --generate-name
helm install helm-repo/htmlpage --generate-name

# List Helm Releases
helm list
helm list --output=yaml
Observation:
PS D:\Experiments\helm-experiments> helm install helm-repo/htmlpage --generate-name
NAME: htmlpage-1731334059
LAST DEPLOYED: Mon Nov 11 19:37:41 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage-1731334059)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT
PS D:\Experiments\helm-experiments> helm list --output=yaml
- app_version: 4.0.0
  chart: htmlpage-4.0.0
  name: htmlpage-1731334059
  namespace: default
  revision: "1"
  status: deployed
  updated: 2024-11-11 19:37:41.1415635 +0530 IST


# Helm Status
helm status htmlpage-1731334059

PS D:\Experiments\helm-experiments> helm status htmlpage-1731334059
NAME: htmlpage-1731334059
LAST DEPLOYED: Mon Nov 11 19:37:41 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage-1731334059)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

helm status htmlpage-1731334059 --show-resources

PS D:\Experiments\helm-experiments> helm status htmlpage-1731334059 --show-resources
NAME: htmlpage-1731334059
LAST DEPLOYED: Mon Nov 11 19:37:41 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
RESOURCES:
==> v1/Service
NAME                  TYPE       CLUSTER-IP    EXTERNAL-IP   PORT(S)          AGE
htmlpage-1731334059   NodePort   10.99.51.78   <none>        8080:30082/TCP   79s

==> v1/Deployment
NAME                  READY   UP-TO-DATE   AVAILABLE   AGE
htmlpage-1731334059   1/1     1            1           79s

==> v1/Pod(related)
NAME                                   READY   STATUS    RESTARTS   AGE
htmlpage-1731334059-7f99fbdc68-tt9sg   1/1     Running   0          79s


NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services htmlpage-1731334059)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT

# Access Application
http://localhost:30082/page
```



## Step-03: Uninstall Helm Release
```t
# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall htmlpage-1731334059
```

