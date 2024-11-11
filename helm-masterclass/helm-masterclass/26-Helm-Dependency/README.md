# Helm Subcharts - Dependency Command




### Introduction
Here’s a simple example to explain Helm chart dependencies. Let’s say you have an application that depends on a Redis database. We’ll create a Helm chart for the main app that includes Redis as a dependency.




## Step 1: Set Up the Main App Chart
    Create the Main Chart:
helm create my-app
This will generate a chart directory named my-app.

    Edit Chart.yaml to Add Redis as a Dependency:
In my-app/Chart.yaml, add the Redis chart as a dependency under dependencies. Helm will fetch this dependency and package it with your chart.
apiVersion: v2
name: my-app
description: A Helm chart for my-app with Redis dependency
version: 1.0.0
appVersion: "1.0.0"
dependencies:
  - name: redis
    version: "20.2.1"  # Specify a compatible version of the Redis chart
    repository: "https://charts.bitnami.com/bitnami"
Here:
    name: The name of the dependency chart (redis).
    version: The version of the Redis chart.
    repository: The URL for the Redis chart’s repository.




## Step 2: Update Dependencies
To download the Redis dependency, run:
helm dependency update my-app
This command will add a charts/ directory inside my-app/ containing the Redis chart, and it will also create a Chart.lock file to lock dependency versions.




## Step 3: Use the Redis Dependency in values.yaml
Customize Redis settings in my-app/values.yaml by adding a redis section to control the Redis dependency:
redis:
  architecture: standalone
  auth:
    enabled: false
These values override the defaults in the Redis chart.




## Step 4: Reference Redis in deployment.yaml
In my-app/templates/deployment.yaml, you can use the Redis release name to connect the main app to Redis:
Customize Redis settings in my-app/templates/deployment.yaml by adding a env section
          env:
            - name: REDIS_HOST
              value: "{{ .Release.Name }}-redis"  # Refers to the Redis hostname
            - name: REDIS_PORT
              value: "6379"




## Step 5: Install the Chart
Now, install the my-app chart, which includes Redis as a dependency:

helm install my-app ./my-app

This command installs my-app and automatically deploys Redis with it.

Summary
    Chart.yaml: Defines Redis as a dependency.
    values.yaml: Configures Redis parameters.
    helm dependency update: Fetches Redis and packages it with the chart.
    Deployment: Connects the main app to Redis by referencing the Redis dependency’s hostname.

This approach allows you to manage and install all related services as a single, coherent deployment.




## Step 6: Helm Dependency list
helm dependency list my-app/
Observation: Should see status as "OK"





## Step 7: Helm Dependency Chart Version Ranges
- Updates to parent chart `Chart.yaml`




## Step 8-1: Basic Comparison Operators
- We can define the version constraints using basic comparison operators
- Where possible, use version ranges instead of pinning to an exact version.
```t
# Basic Comparison Operators
version: "= 9.10.8" 
version: "!= 9.10.8" 
version: ">= 9.10.8"
version: "<= 9.10.8"
version: "> 9.10.8"   
version: "< 9.10.8"
version: ">= 9.10.8 < 9.11.0"  
```
## Step 8-2: For Range Comparison Major: Caret Symbol(ˆ)
- `x` is a placeholder
- The caret (^) operator is for major level changes once a stable (1.0.0) release has occurred.
```t
# For Range Comparison Major: Caret Symbol(ˆ)
^9.10.1  is equivalent to >= 9.10.1, < 10.0.0
^9.10.x  is equivalent to >= 9.10.0, < 10.0.0   
^9.10    is equivalent to >= 9.10, < 10
^9.x     is equivalent to >= 9.0.0, < 10        
^0       is equivalent to >= 0.0.0, < 1.0.0
```
## Step 8-3: For Range Comparison Minor: Tilde Symbol(~)
- `x` is a placeholder
- The tilde (~) operator is for 
  - patch level ranges when a minor version is specified 
  - major level changes when the minor number is missing. 
- The suggested default is to use a patch-level version match which is first one in the below table 
```t
# For Range Comparison Major: Caret Symbol(ˆ)
~9.10.1  is equivalent to >= 9.10.1, < 9.11.0 # Patch-level version match
~9.10    is equivalent to >= 9.10, < 9.11
~9       is equivalent to >= 9, < 10
^9.x     is equivalent to >= 9.0.0, < 10        
^0       is equivalent to >= 0.0.0, < 1.0.0
```
## Step 8-4: Verify with some examples
```yaml
dependencies:
- name: mysql
  version:" "9.10.9"
  #version: ">=9.10.1" # Should dowload latest version available as on that day
  #version: "<=9.10.6" # Should download 9.10.6 mysql helm chart package
  #version: "~9.9.0" # Should download latest from 9.9.x (Patch version) 
  #version: "~9.9" # Should download latest from 9.9 
  #version: "~9" # Should download latest from 9.x 
  repository: "https://charts.bitnami.com/bitnami"
# helm dependency update
helm dependency update
This will delete the mysql-9.9.0.tgz and replace it with mysql-9.10.9.tgz
or
helm dep update  
```




## Step 9: Helm Dependency Build Command
- **helm dependency build:** rebuild the `charts/` directory based on the `Chart.lock` file
- In short `dep update` command will negotiate with version constraints defined in `Chart.yaml` where as `dep build` will try to build or download or update whatever version preset in `Chart.lock` file
- If no lock file is found, `helm dependency build` will mirror the behavior of `helm dependency update`.
```t
# helm dependency build
helm dependency build CHART-NAME
helm dependency build parentchart
```




## Step 10: Helm Dependency Repository @REPO vs REPO-URL
- When we are using Helm with DevOps pipelines across environments "@REPO" approach is not recommended
- REPO-URL approach (repository: "https://charts.bitnami.com/bitnami") is always recommended
```t
# With Repository URL (Recommended approach)
dependencies:
- name: mysql
  version: ">=9.10.8"
  repository: "https://charts.bitnami.com/bitnami"

# List Helm Repo
helm repo list
helm search repo bitnami/mysql --versions

# With @REPO (local repo reference - NOT RECOMMENDED)
dependencies:
- name: mysql
  version: ">=9.10.8"
  repository: "@bitnami"

# Clean-Up Charts folder and Chart.lock
rm parentchart/charts/*
rm parentchart/Chart.lock

# Ensure we are using repository: "@bitnami"
helm dependency update
ls parentchart/charts/
cat parentchart/Chart.lock
Observation: Should work as expected
```
