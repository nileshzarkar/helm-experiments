# Helm Override default values from values.yaml



## Step-01: Introduction
- We will learn the following in this section
    - helm install --set 
    - helm upgrade -f myvalues.yaml
    - **--dry-run:**
    - **--debug:** 
    - helm get values
    - helm get values --revision
    - helm get manifest
    - helm get manifest --revision
    - helm get all
- Discuss about Values Hierarchy




## Step-02: Override default NodePort 31231 with --set


### Step-02-01: Review our mychart1 Helm Chart values.yaml
- [mychart1 values.yaml](https://github.com/stacksimplify/helm-charts/blob/main/mychart1/values.yaml)




### Step-02-02: Learn about --dry-run and --debug flags for helm install command
- Install Helm Chart by overriding NodePort 31231 with 31240
```t
# Helm Install with --dry-run command
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 --dry-run 

The helm install --dry-run command simulates the installation of a Helm chart without actually deploying it to the Kubernetes cluster. It’s useful for checking what will happen during an installation, allowing you to preview the resources and configuration.
--dry-run Flag:
    When --dry-run is used, Helm goes through the release installation process but doesn’t actually deploy anything.
    Instead, it renders the Kubernetes manifests, processes any template functions, and displays the generated YAML files in the terminal.
What Information --dry-run Provides:
    Generated YAML: Shows the fully rendered Kubernetes resources (like Deployments, Services, ConfigMaps) as they would appear when actually deployed.
    Error Checking: Identifies template syntax errors or misconfigurations in your Helm chart before deployment.
    Variables & Overrides: Reflects any custom values you pass with --set or --values, allowing you to verify the exact configuration.
Use Case
--dry-run is helpful when you want to:
    Preview what resources will be created and their configurations.
    Test changes to a Helm chart without affecting the cluster.
    Validate the syntax and parameters of Helm charts before deployment.

# Helm Install with --dry-run and --debug command
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 --dry-run --debug

## THE BELOW IS THE SAMPLE OUTPUT WITH DEBUG ADDED
NAME: myapp901
NAMESPACE: default
STATUS: pending-install
REVISION: 1
	USER-SUPPLIED VALUES:
service:
  nodePort: 31240
COMPUTED VALUES:
fullnameOverride: ""
image:
  pullPolicy: IfNotPresent
  repository: ghcr.io/stacksimplify/kubenginx
  tag: ""
nameOverride: ""
podAnnotations: {}
replicaCount: 1
service:
  nodePort: 31240
  port: 80
  type: NodePort
```



### Step-02-03: helm install with --set and test
```t
# Helm Install 
helm install myapp901 stacksimplify/mychart1 --set service.nodePort=31240 

# helm status --show-resources
helm status myapp901 --show-resources
Observation:
We can see that our NodePort service is running on port 31240

# Access Application
http://localhost:31240
```



## Step-03: Review myvalues.yaml
- **myvalues.yaml file location:** 09-Helm-Override-Values/myvalues.yaml
```yaml
# Change-1: change replicas from 1 to 2
replicaCount: 2
# Change-2: Add tag as "2.0.0" which will override the default appversion "1.0.0" from our mychart1
image:
  repository: ghcr.io/stacksimplify/kubenginx
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: "2.0.0"
# Change-3: Change nodePort from 31240 to 31250
service:
  type: NodePort
  port: 80
  nodePort: 31250
```



## Step-04: Override NodePort 31240 with -f myvalues.yaml with 
- We can either `-f  myvalues.yaml` or `--values myvalues.yaml`.  Both are valid inputs
```t
# Verify if myvalues.yaml
cd 09-Helm-Override-Values
cat myvalues.yaml

# helm upgrade with --dry-run and --debug commands
helm upgrade myapp901 stacksimplify/mychart1 -f myvalues.yaml --dry-run --debug

# helm upgrade
helm upgrade myapp901 stacksimplify/mychart1 -f myvalues.yaml

# helm status
helm status myapp901 --show-resources
Observation: 
1. Two pods will be running as we changed replicacount to 2
2. Service Node Port will be 31250 

# Access Application
http://localhost:31250
Observation: 
1. We should see V2 application because we have used the "image tag as 2.0.0"
```



## Step-05: helm get values command
- **helm get values:** This command downloads a values file for a given release
```t
# helm get values
helm get values RELEASE_NAME
helm get values myapp901

The helm get values command retrieves the configuration values for a specific Helm release. This includes all of the values that were used during the installation or last upgrade of the release, helping you understand or debug its current configuration.
Optional Flags:
    --all: When this flag is included, it shows all values, including default values provided by the chart and any custom values passed by the user. Without --all, only user-supplied values (overrides) are shown.
    -o <format>: Allows you to specify the output format, like json or yaml, making it easier to use the data in scripts or tools that require specific formats.
What Information helm get values Provides:
    User-Supplied Values: Shows only the values that were explicitly overridden or customized at the time of installation or last upgrade.
    Chart Defaults (with --all): Shows all values, including those set by default in the Helm chart, providing a complete view of the configuration.
    Current Configuration: Shows the effective configuration for the release, useful for troubleshooting or validating the deployed settings.

Observation:
1. Provides the values from current/latest release version 2 from Release myapp901

## Sample Output
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm get values myapp901
USER-SUPPLIED VALUES:
image:
  pullPolicy: IfNotPresent
  repository: ghcr.io/stacksimplify/kubenginx
  tag: 2.0.0
replicaCount: 2
service:
  nodePort: 31250
  port: 80
  type: NodePort


# helm history (History prints historical revisions for a given release.)
helm history myapp901


# helm get values with --revision
helm get values RELEASE-NAME --revision int
helm get values myapp901 --revision 1

## Sample Output
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm get values myapp901 --revision 1
USER-SUPPLIED VALUES:
service:
  nodePort: 31240
```



## Step-06: helm get manifest command 
- **helm get manifest:** This command fetches the generated manifest for a given release.
```t
# helm get manifest
helm get manifest RELEASE-NAME
helm get manifest myapp901

The helm get manifest command retrieves the full manifest (all rendered Kubernetes YAML resources) for a specified Helm release. This lets you view the exact configuration that Helm deployed to Kubernetes based on the chart templates and values used.
Output:
    This command outputs the complete set of Kubernetes resource definitions (e.g., Deployments, Services, ConfigMaps, Secrets) as YAML files.
    It shows the rendered YAML after all values and templates are processed, exactly as they were applied to the cluster.
What Information helm get manifest Provides:
    Rendered Resources: The YAML configuration of each Kubernetes resource created by the release. This includes details like labels, annotations, and values used for various fields.
    Template Processing: Shows the output after Helm processes chart templates and values, which is particularly useful for debugging template issues.
    Effective State: Displays what is currently deployed, so you can cross-check it with your expectations or compare it against future upgrades

# helm get manifest --revision
helm get manifest RELEASE-NAME --revision int
helm get manifest myapp901 --revision 1
```



## Step-07: helm get all command
- **helm get all:** This command prints a human readable collection of information about the notes, hooks, supplied values, and generated manifest file of the given release.
- This is a good way to see what templates are installed on the kubernetes cluster server.
- **helm get notes and helm get hooks:** These two commmands we will explore when we are discussing about helm chart development. 
```t
# helm get all
helm get all RELEASE-NAME
helm get all myapp901
```



## Step-08: Uninstall Helm Release
```t
# Uninstall Helm Release
helm uninstall myapp901

# List Helm Releases
helm list
```



## Step-09: Values Hierarchy
1. Sub chart `values.yaml` can be overriden by parents chart `values.yaml`
2. Parent charts `values.yaml` can be overriden by user-supplied value file `(-f myvalues.yaml)`
3. User-supplied value file `(-f myvalues.yaml)` can be overriden by `--set` parameters




## Step-10: Deleting a default Key by passing null
- If you need to delete a key from the default values, you may override the value of the key to be null, in which case Helm will remove the key from the overridden values merge.
```t
# Release: myapp901
helm install myapp901 stacksimplify/mychart1 --atomic
helm list
helm status myapp901 --show-resources
http://localhost:31231

# Release: myapp902
helm install myapp902 stacksimplify/mychart1 --atomic
helm list

# Option-1: Give desired port other than 31231
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=31232 

# Option-2: Pass null value to nodePort (service.nodePort=null)
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=null --dry-run --debug
helm install myapp902 stacksimplify/mychart1 --set service.nodePort=null 

# Additional Notes for understanding
1. We will choose option-2 to demonstrate the concept "Deleting a default Key by passing null"
2. For NodePort Service, if we dont define the "nodePort" argument, it by default assigns a port dynamically from the port range 30000-32767. 
3. In our case already 31231 is used, other than that port it will allocate someother port when we pass null. 
4. In short, if we dont want to pass the default values present in values.yaml as-is, we dont need to change the complete chart with a new version, we can just pass null.

# Uninstall Releases
helm uninstall myapp901
helm uninstall myapp902
helm list
```