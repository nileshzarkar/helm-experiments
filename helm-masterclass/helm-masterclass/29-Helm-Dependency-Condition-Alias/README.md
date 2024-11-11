# Helm Dependency - Condition with Alias




## Step-01: Introduction
In Helm, the condition alias allows you to use both alias and condition together for dependencies, giving you more control over deploying multiple instances of the same dependency chart with different configurations. Using an alias alongside a condition is helpful when you want to enable or disable each instance independently.

Let’s continue with the my-app example where the application depends on two Redis instances: redis-cache and redis-session. Here’s how we can configure alias with condition to manage both Redis instances separately.


    Create the Main Chart:
helm create my-app




## Step-02: Define Dependencies with Alias and Condition in Chart.yaml
In Chart.yaml, use both alias and condition fields for each Redis dependency.

```yaml
apiVersion: v2
name: my-app
version: 1.0.0
dependencies:
  - name: redis
    version: "20.2.1"               # Redis chart version
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-cache              # Alias for the cache instance
    condition: redis-cache.enabled  # Condition for enabling/disabling redis-cache
  - name: redis
    version: "20.2.1"               # Redis chart version
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-session            # Alias for the session instance
    condition: redis-session.enabled  # Condition for enabling/disabling redis-session

```
Here:
    alias gives each Redis instance a unique name (redis-cache and redis-session).
    condition points to a specific key in values.yaml (e.g., redis-cache.enabled), allowing you to enable or disable each instance separately.


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




## Step-03: Set Up Condition Aliases in values.yaml
Define the enabled key under each section in values.yaml to control whether each Redis instance should be deployed.

values.yaml:
```yaml
redis-cache:
  enabled: true            # Set to true to deploy the redis-cache instance
  architecture: standalone
  auth:
    enabled: false
  persistence:
    enabled: false

redis-session:
  enabled: false           # Set to false to skip deploying the redis-session instance
  architecture: standalone
  auth:
    enabled: true
  authPassword: "sessionpassword"
  persistence:
    enabled: true
```
redis-cache.enabled: true: This enables the redis-cache instance, so it will be deployed.
redis-session.enabled: false: This disables the redis-session instance, so it will not be deployed.




## Step-04: Helm Dependency Build Command
- **helm dependency build:** rebuild the `charts/` directory based on the `Chart.lock` file
- In short `dep update` command will negotiate with version constraints defined in `Chart.yaml` where as `dep build` will try to build or download or update whatever version preset in `Chart.lock` file
- If no lock file is found, `helm dependency build` will mirror the behavior of `helm dependency update`.
```t
# helm dependency build
helm dependency build CHART-NAME
helm dependency build my-app
```




## Step-05: Update Dependencies and Deploy the Chart
With these settings in place, update the dependencies and deploy the chart:
```t
helm dependency update my-app
helm install my-app ./my-app
```
With the enabled values above, only redis-cache will be deployed, and redis-session will be skipped. This allows you to independently control each dependency without changing the core chart.




## Step-06: Toggle Dependencies Dynamically with --set
You can also enable or disable dependencies dynamically at install or upgrade time by setting the enabled flag:
```t
# Enable both redis-cache and redis-session instances during upgrade
helm upgrade my-app ./my-app --set redis-cache.enabled=true --set redis-session.enabled=true --set global.redis.password=sessionpassword
```
This command temporarily overrides the values in values.yaml, deploying both Redis instances (redis-cache and redis-session).

Summary
- alias: Gives each dependency a unique name, allowing multiple instances of the same chart.
- condition: Allows each instance to be enabled or disabled based on a specific key in values.yaml.
- condition alias: Combines both alias and condition for independent control of each dependency instance, making the chart more flexible and adaptable to
  different deployment needs.