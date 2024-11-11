# Helm Dependency - Alias




## Step-01: Introduction
In Helm, dependency aliasing allows you to include the same chart dependency multiple times in a single Helm chart under different names or configurations. This is useful when you need multiple instances of the same dependency (e.g., Redis) with different settings.

Continuing with our my-app example, let’s say you want your app to use two different Redis instances: one as a cache and another as a session store. We can achieve this with dependency aliasing.

    Create the Main Chart:
helm create my-app

## Step-02: Define Aliased Dependencies in Chart.yaml
In Chart.yaml, you can define multiple entries for the Redis chart with different names by using alias.
```yaml
apiVersion: v2
name: my-app
version: 1.0.0
dependencies:
  - name: redis
    version: "16.8.0"  # Specify Redis chart version
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-cache
  - name: redis
    version: "16.8.0"
    repository: "https://charts.bitnami.com/bitnami"
    alias: redis-session
```    
Here:

    alias: redis-cache: Creates an instance of Redis named redis-cache.
    alias: redis-session: Creates another instance of Redis named redis-session.

Each aliased dependency can be configured separately in values.yaml.




## Step-03: Configure Each Redis Instance in values.yaml
In values.yaml, you can provide individual configurations for each Redis instance.
```yaml
redis-cache:
  architecture: standalone
  auth:
    enabled: false
  persistence:
    enabled: false

redis-session:
  architecture: standalone
  auth:
    enabled: true
  authPassword: "sessionpassword"
  persistence:
    enabled: true
```
redis-cache: Configures the Redis instance used for caching, without authentication or persistence.
redis-session: Configures the Redis instance used for sessions, with authentication and persistence.




## Step-04: Reference Aliased Dependencies in Templates
In your deployment.yaml, you can now refer to each instance by its alias name. This way, you can configure environment variables or service names for each Redis instance independently. Add the env section to the deployment.yaml
```yaml
          env:
            - name: REDIS_CACHE_HOST
              value: "{{ .Release.Name }}-redis-cache"
            - name: REDIS_CACHE_PORT
              value: "6379"
            - name: REDIS_SESSION_HOST
              value: "{{ .Release.Name }}-redis-session"
            - name: REDIS_SESSION_PORT
              value: "6379"
            - name: REDIS_SESSION_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: "{{ .Release.Name }}-redis-session"
                  key: redis-password  # Reference the password from redis-session's secret

```




## Step-05: Helm Dependency Build Command
- **helm dependency build:** rebuild the `charts/` directory based on the `Chart.lock` file
- In short `dep update` command will negotiate with version constraints defined in `Chart.yaml` where as `dep build` will try to build or download or update whatever version preset in `Chart.lock` file
- If no lock file is found, `helm dependency build` will mirror the behavior of `helm dependency update`.
```t
# helm dependency build
helm dependency build CHART-NAME
helm dependency build my-app
```




## Step-06: Install the Chart
Now you can install the chart, and Helm will deploy both Redis instances (redis-cache and redis-session) with their separate configurations:
```t
helm install my-app ./my-app
```
Summary

    Aliasing allows multiple instances of the same chart dependency with unique names and configurations.
    alias in Chart.yaml creates unique names for each dependency.
    Separate configurations in values.yaml let you customize each instance as needed.
    This pattern is useful when you need multiple instances of the same service, like Redis, for different purposes in your application.