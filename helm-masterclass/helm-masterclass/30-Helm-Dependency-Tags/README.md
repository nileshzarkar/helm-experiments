# Helm Dependency - Using Tags




## Step-01: Introduction
In Helm, dependency tags provide another way to control whether dependencies are enabled or disabled. Tags can be used to group dependencies and enable or disable multiple dependencies simultaneously. This is especially useful when you have several dependencies and want to control them as a group rather than individually.

Continuing with the my-app example where the application depends on two Redis instances (redis-cache and redis-session), we can use tags to control both Redis instances together or individually.



    Create the Main Chart:
helm create my-app



 ## Step-02: Define Dependencies with Tags in Chart.yaml
In Chart.yaml, add a tags field for each dependency. Tags can be used to label dependencies, allowing you to enable or disable dependencies based on these labels.
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
    tags:
      - redis
      - cache
  - name: redis
    version: "16.8.0"
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-session
    condition: redis-session.enabled
    tags:
      - redis
      - session
 ```
Here:

    tags assigns one or more tags to each dependency. In this example:
        redis-cache is tagged with redis and cache.
        redis-session is tagged with redis and session.


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


 ## Step-03: Configure Tags in values.yaml
 In values.yaml, define a tags section. Here, you can set each tag to true or false to enable or disable all dependencies that have that tag.
 ```t
 tags:
  redis: true      # Enables all dependencies with the "redis" tag
  cache: true      # Enables dependencies tagged as "cache"
  session: false   # Disables dependencies tagged as "session"
```
In this example:

    tags.redis: true: Enables all dependencies tagged with redis, so both redis-cache and redis-session would be enabled based on this tag alone.
    tags.cache: true: Enables dependencies tagged with cache, which applies specifically to redis-cache.
    tags.session: false: Disables all dependencies tagged with session, so redis-session would not be deployed if this tag is prioritized.



How Dependency Tags Work with Conditions

When both tags and conditions are specified, both must be true for the dependency to be enabled. In this example:

    redis-cache will be deployed if tags.redis and tags.cache are both true, and redis-cache.enabled is also true.
    redis-session will only be deployed if tags.redis, tags.session, and redis-session.enabled are all true.




## Step-04: Helm Dependency Build Command
- **helm dependency build:** rebuild the `charts/` directory based on the `Chart.lock` file
- In short `dep update` command will negotiate with version constraints defined in `Chart.yaml` where as `dep build` will try to build or download or update whatever version preset in `Chart.lock` file
- If no lock file is found, `helm dependency build` will mirror the behavior of `helm dependency update`.
```t
# helm dependency build
helm dependency build CHART-NAME
helm dependency build my-app
```



 ## Step-05: Control Tags at Install or Upgrade Time
 You can also set tag values dynamically using --set when running helm install or helm upgrade:
 ```t

# Disable all Redis dependencies by setting the "redis" tag to false
helm install my-app ./my-app --set tags.redis=false

# Enable only the "cache" instance by setting the "session" tag to false
helm install my-app ./my-app --set tags.cache=true --set tags.session=false

# Enable only the "session" instance by setting the "cache" tag to false
helm install my-app ./my-app --set tags.cache=false --set tags.session=true
```
Summary
    Tags: Allow grouping and controlling multiple dependencies at once.
    Configuring Tags in values.yaml: Sets each tag to true or false to enable or disable dependencies.
    Tags + Conditions: Both the tag and condition must be true for a dependency to be enabled.

Using tags in Helm gives you greater flexibility, allowing you to control dependencies as a group or individually based on deployment needs.