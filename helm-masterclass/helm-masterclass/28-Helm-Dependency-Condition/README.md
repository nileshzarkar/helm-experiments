# Helm Dependency - Condition



## Step-01: Introduction
In Helm, a dependency condition allows you to control whether a chart dependency is enabled or disabled based on values provided in values.yaml. This is particularly useful when you want to include optional dependencies, like a Redis instance, that can be toggled on or off depending on your environment.

Using the my-app example from above, where my-app has dependencies on two Redis instances (redis-cache and redis-session), we’ll set up conditions to control whether each Redis instance is deployed.

    Create the Main Chart:
helm create my-app




## Step-02: Set Up Dependency Conditions in Chart.yaml
In Chart.yaml, add a condition field for each dependency. This field specifies a path in values.yaml that determines whether the dependency should be enabled.

Chart.yaml:
```yaml
apiVersion: v2
name: my-app
version: 1.0.0
dependencies:
  - name: redis
    version: "16.8.0"
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-cache
    condition: redis-cache.enabled
  - name: redis
    version: "16.8.0"
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-session
    condition: redis-session.enabled

```
Here:
- condition: redis-cache.enabled: Enables or disables the redis-cache instance based on the value of redis-cache.enabled in values.yaml.
- condition: redis-session.enabled: Controls the redis-session instance similarly.




Updates for NodePort
values.yaml
===========
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

service.yaml
============
...
spec:
  {{- with .Values.service }}
  type: {{ .type }}
  ports:
    - port: {{ .port }}
      targetPort: {{ .targetPort }}
      nodePort: {{ .nodePort }}
      protocol: TCP
      name: http
  {{- end }}  
...  




## Step-03: Define the Conditions in values.yaml
In values.yaml, add the enabled keys under each dependency’s section. You can set these to true or false to control whether each Redis instance is deployed.

values.yaml:
```t
...
redis-cache:
  enabled: true
  architecture: standalone
  auth:
    enabled: false
  persistence:
    enabled: false

redis-session:
  enabled: false  # Set to true if you want to deploy this dependency
  architecture: standalone
  auth:
    enabled: true
  authPassword: "sessionpassword"
  persistence:
    enabled: true
...
```
- redis-cache.enabled: true: Enables the redis-cache dependency, so it will be deployed.
- redis-session.enabled: false: Disables the redis-session dependency, so it will not be deployed.




## Step-04: Helm Dependency Build Command
- **helm dependency build:** rebuild the `charts/` directory based on the `Chart.lock` file
- In short `dep update` command will negotiate with version constraints defined in `Chart.yaml` where as `dep build` will try to build or download or update whatever version preset in `Chart.lock` file
- If no lock file is found, `helm dependency build` will mirror the behavior of `helm dependency update`.
```t
# helm dependency build
helm dependency build CHART-NAME
helm dependency build my-app
```




## Step-05: Update and Deploy the Chart
After setting the conditions, update the dependencies and install the chart:
```t
helm dependency update my-app
helm install my-app ./my-app
```
With these settings:
- Only redis-cache will be deployed since redis-cache.enabled is set to true.
- redis-session will be skipped because redis-session.enabled is set to false.




## Step-06: Toggling Dependencies Dynamically
You can override these conditions at install or upgrade time to enable or disable dependencies dynamically:
```t
# Enable both redis-cache and redis-session
helm upgrade my-app ./my-app --set redis-cache.enabled=true --set redis-session.enabled=true
```

Summary
- condition in Chart.yaml: Defines a toggle path in values.yaml for each dependency.
- enabled in values.yaml: Controls whether each dependency is deployed.
- This approach makes the chart flexible, allowing you to enable or disable dependencies based on environment needs without modifying the main chart templates.