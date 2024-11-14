# Helm Development - Flow Control If-Else




## Step-01: Introduction
-  We can use `if/else` for creating conditional blocks in Helm Templates
- **eq:** For templates, the operators (eq, ne, lt, gt, and, or and so on) are all implemented as functions. 
- In pipelines, operations can be grouped with parentheses ((, and )).
- [Additional Reference: Operators are functions](https://helm.sh/docs/chart_template_guide/functions_and_pipelines/#operators-are-functions)
### IF-ELSE Syntax
```t
{{ if PIPELINE }}
  # Do something
{{ else if OTHER PIPELINE }}
  # Do something else
{{ else }}
  # Default case
{{ end }}
```




## Step-02: Review values.yaml
```yaml
# If, else if, else
myapp:
  env: prod
```




## Step-03: Logic and Flow Control Function: and 
- [Logic and Flow Control Functions](https://helm.sh/docs/chart_template_guide/function_list/#logic-and-flow-control-functions)
- **eq:**  Returns the boolean equality of the arguments (e.g., Arg1 == Arg2).
```t
# and Syntax
eq .Arg1 .Arg2
```




## Step-04: Implement if-else for replicas
```yaml
deployment.yaml
...
spec:
  {{- if not .Values.autoscaling.enabled }}
  {{- if eq .Values.config.env "prod" }}
  replicas: 3
  {{- else if eq .Values.config.env "qa" }}  
  replicas: 2
  {{- else }}  
  replicas: {{ default 1 .Values.replicaCount }}
  {{- end }}  
  {{- end }}
  selector:
    matchLabels:
...

valuers.yaml
...
replicaCount: 1
...
config:
  pageColor: "black"
  env: "dev"  # prod-> replicas 3  ,  qa -> replicas 2 , dev -> replicas 1 (default)
...
```




## Step-05: Verify if-else
```t
# Change to Chart Directory
cd htmlpage

# Helm Template (when env: prod from values.yaml)
## TEST IF STATEMENT
helm template htmlpage .

# Helm Template (when env: qa using --set)
## TEST ELSE IF STATEMENT
helm template htmlpage . --set config.env=qa

Output:
...
spec:
  replicas: 2
  selector:
    matchLabels:
...    

# Helm Template (when env: dev or env: null using --set)
## TEST ELSE STATEMENT
helm template htmlpage . --set myapp.env=dev

Output:
...
spec:
  replicas: 1
  selector:
    matchLabels:
... 

NOTE: 
Readiness probe failed: Get "http://10.1.3.33:8080/": dial tcp 10.1.3.33:8080: connect: connection refused

Solution: 
Update values.yaml
livenessProbe:
  ...
  initialDelaySeconds: 5
  periodSeconds: 5
readinessProbe:
  ...
  initialDelaySeconds: 5
  periodSeconds: 5

# Helm Install Dry-run 
helm install htmlpage . --dry-run

# Helm Install
helm install htmlpage . --atomic

# Verify Pods
helm status htmlpage --show-resources

# Uninstall Release
helm uninstall htmlpage
```
