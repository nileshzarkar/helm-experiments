# Helm Development - Flow Control If-Else with Boolean Check and "AND Function"




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
  retail:
    enableFeature: true
```




## Step-03: Logic and Flow Control Function: and 
- [Logic and Flow Control Functions](https://helm.sh/docs/chart_template_guide/function_list/#logic-and-flow-control-functions)
- **and:**  Returns the boolean AND of two or more arguments (the first empty argument, or the last argument).
```t
# and Syntax
and .Arg1 .Arg2
```



## Step-04: Implement if-else for replicas with Boolean 

Usecase : 1
```yaml
deployment.yaml
...
spec:
  {{- if and (not .Values.autoscaling.enabled) (eq .Values.config.env "prod")  }}
  replicas: 3
  {{- else if eq .Values.config.env "qa" }}  
  replicas: 2
  {{- else }}  
  replicas: {{ default 1 .Values.replicaCount }}
  {{- end }}  
  selector:
...

values.yaml
...
config:
  pageColor: "black"
  env: "prod"  # prod-> replicas 3  ,  qa -> replicas 2 , dev -> replicas 1 (default)
...
autoscaling:
  enabled: true
...  

Output:
...
spec:
  replicas: 1
...
```


Usecase : 2
```yaml
deployment.yaml
...
spec:
  {{- if and (not .Values.autoscaling.enabled) (eq .Values.config.env "prod")  }}
  replicas: 3
  {{- else if eq .Values.config.env "qa" }}  
  replicas: 2
  {{- else }}  
  replicas: {{ default 1 .Values.replicaCount }}
  {{- end }}  
  selector:
...

values.yaml
...
config:
  pageColor: "black"
  env: "prod"  # prod-> replicas 3  ,  qa -> replicas 2 , dev -> replicas 1 (default)
...
autoscaling:
  enabled: false
...  

Output:
...
spec:
  replicas: 1
...
```


## Step-05: Verify if-else
```t
# Change to Chart Directory
cd htmlpage

# Helm Template 
helm template htmlpage . --set autoscaling.enabled=true
helm template htmlpage . --set autoscaling.enabled=false
helm template htmlpage . --set config.env=qa
helm template htmlpage . --set config.env=dev

# Helm Install Dry-run 
helm install htmlpage . --set autoscaling.enabled=true --dry-run

# Helm Install
helm install htmlpage . --set autoscaling.enabled=true --atomic

# Verify Pods
helm status htmlpage --show-resources

# Uninstall Release
helm uninstall htmlpage
```
