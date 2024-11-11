# Helm Starters

C:\Users\niles\AppData\Roaming\helm\starters>helm create mystarterchart
Creating mystarterchart

Update values.yaml
...
image:
  repository: nileshzarkar/htmlpage
  pullPolicy: Always
  # Overrides the image tag whose default is the chart appVersion.
  tag: "1.0.0"
...
service:
  type: NodePort
  port: 8082
  nodePort: 30082
  targetPort: 8082
  name: my-app-service
...
serviceAccount:
  create: false
...


Update Chart.yaml
apiVersion: v2
name: mystarterchart
description: A Helm chart for Kubernetes
type: application
version: 0.1.0
appVersion: "1.0.0"

C:\Users\niles\AppData\Roaming\helm\starters\mystarterchart>helm lint
==> Linting .
[INFO] Chart.yaml: icon is recommended

1 chart(s) linted, 0 chart(s) failed

C:\Users\niles\AppData\Roaming\helm\starters\mystarterchart>helm install myapp .
NAME: myapp
LAST DEPLOYED: Wed Nov  6 08:24:55 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services myapp-mystarterchart)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT


http://localhost:30082/page




Replace "mystarterchart" with `<CHARTNAME>` in all files
**Important Note:**  All occurrences of `<CHARTNAME>` will be replaced with the specified chart name so that starter charts can be used as templates.
1. _helpers.tpl
2. deployment.yaml
3. service.yaml
4. NOTES.txt
5. Chart.yaml
6. values.yaml (Here just in comment)



# COPY mystarterchart folder 
Copy mystarterchart C:\Users\niles\AppData\Roaming\helm\starters


# Helm Create using starter chart
helm create myapp --starter=mystarterchart

# Review mychart9 files
1. Chart.yaml
2. deployment.yaml - Review it
3. service.yaml - Review it
4. values.yaml - Review it
5. NOTES.txt - Review it
6. _helpers.tpl - Review it
```

PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\35-Helm-Starters> helm lint .\myapp\
==> Linting .\myapp\
[INFO] Chart.yaml: icon is recommended

1 chart(s) linted, 0 chart(s) failed


PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\35-Helm-Starters> helm install myapp .\myapp\
NAME: myapp
LAST DEPLOYED: Wed Nov  6 08:37:58 2024
NAMESPACE: default
STATUS: deployed
REVISION: 1
TEST SUITE: None
NOTES:
1. Get the application URL by running these commands:
  export NODE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].nodePort}" services myapp)
  export NODE_IP=$(kubectl get nodes --namespace default -o jsonpath="{.items[0].status.addresses[0].address}")
  echo http://$NODE_IP:$NODE_PORT



http://localhost:30082/page








