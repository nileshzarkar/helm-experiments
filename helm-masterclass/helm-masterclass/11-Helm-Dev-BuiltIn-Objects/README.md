# Helm Builtin Objects




## Step-01: Introduction
- Objects are passed into a helm template from the helm template engine. 
- Objects can be simple, and have just one value or they can contain other objects or functions. 
- For example: the Release object contains several objects (like .Release.Name) and the Files object has a few functions.
- Root Object do or Period (.)
  - Top level Helm Object
  - At the top-level Helm initializes dot (.) to an object with keys like below
    - .Release 
    - .Value  
    - .Chart
    - .Capabilities  
    - .Template 
    - .File

### Helm Builtin Objects
- Release 
- Chart 
- Values 
- Capabilities 
- Template 
- Files 




## Step-02: Create a simple chart and clean-up NOTES.txt
```t
# Create Helm Chart
helm create CHART-NAME
# helm create builtinobjects
helm create htmlpage

Update values.yaml
...
image:
  repository: nileshzarkar/htmlpage
  pullPolicy: Always
  tag: "1.0.0"
...
serviceAccount:
  create: false
...
service:
  type: NodePort
  port: 8080
  targetPort: 8080
  nodePort: 30082
...  
config:
  pageColor: "green"
...

Update deployment.yaml
...
         ports:
            - name: http
              containerPort: {{ .Values.service.port }}
              protocol: TCP
          env:
            - name: page.color
              value: {{ .Values.config.pageColor }}        
          livenessProbe:
...

Update service.yaml
...
spec:
  type: {{ .Values.service.type }}
  ports:
    - port: {{ .Values.service.port }}
      targetPort: {{ .Values.service.targetPort }}
      nodePort: {{ .Values.service.nodePort }}
      protocol: TCP
      name: http
  selector:
...

Update Chart.yaml
apiVersion: v2
name: htmlpage
description: A Helm chart for Kubernetes
type: application
version: 1.0.0
appVersion: "1.0.0"

# Remove all content from NOTES.txt (Empty the file)
cd htmlpage/templates
>NOTES.txt

# Change to Chart Directory
cd htmlpage

# helm install --dry-run
helm install htmlpage . --dry-run
```






## Step-03: Helm Object: Root or dot or Period (.)
```t
# Update NOTES.txt
{{/* Root or Dot or Period Object */}}
Root Object: {{ . }}

# Change to Chart Directory
cd htmlpage
# helm install with --dry-run
helm install htmlpage . --dry-run
```




## Step-04: Helm Object: Release
- This object describes the Helm release. 
- It has several objects inside it related to Helm Release.
- Put the below in `NOTES.txt` and test it
```t
{{/* Release Object */}}
Release Name: {{ .Release.Name }}
Release Namespace: {{ .Release.Namespace }}
Release IsUpgrade: {{ .Release.IsUpgrade }}
Release IsInstall: {{ .Release.IsInstall }}
Release Revision: {{ .Release.Revision }}
Release Service: {{ .Release.Service }}

# Change to CHART Directory 
cd builtinobjects 
# Helm Install with --dry-run
helm install myapp101 . --dry-run

# Sample Output
NOTES:
Root Object:
Release Name: htmlpage
Release Namespace: default
Release IsUpgrade: false
Release IsInstall: true
Release Revision: 1
Release Service: Helm
```




## Step-05: Helm Object: Chart
- Any data in Chart.yaml will be accessible using Chart Object. 
- For example {{ .Chart.Name }}-{{ .Chart.Version }} will print out the builtinobjects-0.1.0.
- [Complte Chart.yaml Objects for reference](https://helm.sh/docs/topics/charts/#the-chartyaml-file)
- Put the below in `NOTES.txt` and test it
```t
{{/* Chart Objet */}}
Chart Name: {{ .Chart.Name }}
Chart Version: {{ .Chart.Version }}
Chart AppVersion: {{ .Chart.AppVersion }}
Chart Type: {{ .Chart.Type }}
Chart Name and Version: {{ .Chart.Name }}-{{ .Chart.Version }}

# Change to CHART Directory 
cd builtinobjects 
# Helm Install with --dry-run
helm install myapp101 . --dry-run

# Sample Output
Chart Name: htmlpage
Chart Version: 1.0.0
Chart AppVersion: 1.0.0
Chart Type: application
Chart Name and Version: htmlpage-1.0.0
```




## Step-06: Helm Objects: Values, Capabilities, Template
- **Values Object:** Values passed into the template from the values.yaml file and from user-supplied files (-f or --set). By default, Values is empty.
- **Capabilities Object:** This provides information about what capabilities the Kubernetes cluster supports
- **Template Object:** Contains information about the current template that is being executed
- Put the below in `NOTES.txt` and test it
```t


{{/* Values Object */}}
Replica Count: {{ .Values.replicaCount }}
Image Repository: {{ .Values.image.repository }}
Service Type: {{ .Values.service.type }}

# Sample Output
Replica Count: 1
Image Repository: nileshzarkar/htmlpage
Config PageColor: black
Service Type: NodePort


{{/* Capabilities Object */}}
Kubernetes Cluster Version Major: {{ .Capabilities.KubeVersion.Major }}
Kubernetes Cluster Version Minor: {{ .Capabilities.KubeVersion.Minor }}
Kubernetes Cluster Version: {{ .Capabilities.KubeVersion }} and {{ .Capabilities.KubeVersion.Version }}
Helm Version: {{ .Capabilities.HelmVersion }}
Helm Version Semver: {{ .Capabilities.HelmVersion.Version }}

# Sample Output
Kubernetes Cluster Version Major: 1
Kubernetes Cluster Version Minor: 29
Kubernetes Cluster Version: v1.29.2 and v1.29.2
Helm Version: {v3.15.2 1a500d5625419a524fdae4b33de351cc4f58ec35 clean go1.22.4}
Helm Version Semver: v3.15.2


{{/* Template Object */}}
Template Name: {{ .Template.Name }} 
Template Base Path: {{ .Template.BasePath }}

# Sample Output
Template Name: htmlpage/templates/NOTES.txt
Template Base Path: htmlpage/templates


# Change to CHART Directory 
cd htmlpage 

# Helm Install with --dry-run
helm install htmlpage . --dry-run
```




## Step-07: Helm Objects: Files
- **Files Object:** 
- Put the below in `NOTES.txt` and test it
- This provides access to all non-special files in a chart.
- We cannot use it to access templates
- [Additional Reference: Access Files Inside Templates](https://helm.sh/docs/chart_template_guide/accessing_files/)
```t

helm-masterclass/helm-masterclass/11-Helm-Dev-BuiltIn-Objects/htmlpage/myconfig1.toml

{{/* File Object */}}
File Get: {{ .Files.Get "myconfig1.toml" }}

File Glob as Config: {{ (.Files.Glob "config-files/*").AsConfig }}

File Glob as Secret: {{ (.Files.Glob "config-files/*").AsSecrets }}

File Lines: {{ .Files.Lines "myconfig1.toml" }}

File Lines: {{ .Files.Lines "config-files/myconfig2.toml" }}

File Glob: {{ .Files.Glob "config-files/*" }}

# Change to CHART Directory 
cd htmlpage 

# Helm Install with --dry-run
helm install myapp101 . --dry-run

# Sample Output
File Get: message1 = Hello from config 1 line1
message2 = Hello from config 1 line2
message3 = Hello from config 1 line3


File Glob as Config: myconfig1.toml: |
  message1 = Hello from config 1 line1
  message2 = Hello from config 1 line2
  message3 = Hello from config 1 line3
myconfig2.toml: |-
  appName: myapp2
  appType: db
  appConfigEnable: true

File Glob as Secret: myconfig1.toml: bWVzc2FnZTEgPSBIZWxsbyBmcm9tIGNvbmZpZyAxIGxpbmUxCm1lc3NhZ2UyID0gSGVsbG8gZnJvbSBjb25maWcgMSBsaW5lMgptZXNzYWdlMyA9IEhlbGxvIGZyb20gY29uZmlnIDEgbGluZTMK
myconfig2.toml: YXBwTmFtZTogbXlhcHAyCmFwcFR5cGU6IGRiCmFwcENvbmZpZ0VuYWJsZTogdHJ1ZQ==

File Lines: [message1 = Hello from config 1 line1 message2 = Hello from config 1 line2 message3 = Hello from config 1 line3]

File Lines: [appName: myapp2 appType: db appConfigEnable: true]

File Glob: map[config-files/myconfig1.toml:[109 101 115 115 97 103 101 49 32 61 32 72 101 108 108 111 32 102 114 111 109 32 99 111 110 102 105 103 32 49 32 108 105 110 101 49 10 109 101 115 115 97 103 101 50 32 61 32 72 101 108 108 111 32 102 114 111 109 32 99 111 110 102 105 103 32 49 32 108 105 110 101 50 10 109 101 115 115 97 103 101 51 32 61 32 72 101 108 108 111 32 102 114 111 109 32 99 111 110 102 105 103 32 49 32 108 105 110 101 51 10] config-files/myconfig2.toml:[97 112 112 78 97 109 101 58 32 109 121 97 112 112 50 10 97 112 112 84 121 112 101 58 32 100 98 10 97 112 112 67 111 110 102 105 103 69 110 97 98 108 101 58 32 116 114 117 101]]}
```



## Additional Reference
- [Helm Built-In Objects](https://helm.sh/docs/chart_template_guide/builtin_objects/)
- [Helm Chart.yaml Fields](https://helm.sh/docs/chart_template_guide/builtin_objects/)


