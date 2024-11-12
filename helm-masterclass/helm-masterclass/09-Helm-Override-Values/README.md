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
- [htmlpagechart values.yaml](https://github.com/nileshzarkar/helm-charts/blob/main/charts/htmlpagechart/values.yaml)




### Step-02-02: Learn about --dry-run and --debug flags for helm install command
- Install Helm Chart by overriding NodePort 30082 to 30092
```t
# Helm Install with --dry-run command
helm install htmlpage helm-repo/htmlpage --set service.nodePort=30092 --dry-run 

# Helm Install with --dry-run and --debug command
helm install htmlpage helm-repo/htmlpage --set service.nodePort=30092 --dry-run --debug

## THE BELOW IS THE SAMPLE OUTPUT WITH DEBUG ADDED
NAME: htmlpage
LAST DEPLOYED: Tue Nov 12 05:58:55 2024
NAMESPACE: default
STATUS: pending-install
REVISION: 1
USER-SUPPLIED VALUES:
service:
  nodePort: 30092

COMPUTED VALUES:
affinity: {}
autoscaling:
  enabled: false
  maxReplicas: 100
  minReplicas: 1
  targetCPUUtilizationPercentage: 80
config:
  pageColor: green
fullnameOverride: ""
image:
  pullPolicy: Always
  repository: nileshzarkar/htmlpage
  tag: 4.0.0
imagePullSecrets: []
```




### Step-02-03: helm install with --set and test
```t
# Helm Install 
helm install htmlpage helm-repo/htmlpage --set service.nodePort=30092 

# helm status --show-resources
helm status htmlpage --show-resources
Observation:
We can see that our NodePort service is running on port 30092

# Access Application
http://localhost:30092/page
```




## Step-03: Review myvalues.yaml
- **myvalues.yaml file location:** 09-Helm-Override-Values/myvalues.yaml
```yaml
# Change-1: change replicas from 1 to 2
replicaCount: 2
# Change-2: Add tag as "2.0.0" which will override the default appversion "1.0.0" from our mychart1
image:
  repository: nileshzarkar/htmlpage
  pullPolicy: IfNotPresent
  # Overrides the image tag whose default is the chart appVersion.
  tag: "2.0.0"
# Change-3: Change nodePort from 30092 to 30082
service:
  type: NodePort
  nodePort: 30092
```




## Step-04: Override NodePort 31240 with -f myvalues.yaml with 
- We can either `-f  myvalues.yaml` or `--values myvalues.yaml`.  Both are valid inputs
```t
# Verify if myvalues.yaml
cd 09-Helm-Override-Values
cat myvalues.yaml

# helm upgrade with --dry-run and --debug commands
helm upgrade htmlpage helm-repo/htmlpage -f myvalues.yaml --dry-run --debug

# helm upgrade
helm upgrade htmlpage helm-repo/htmlpage -f myvalues.yaml

# helm status
helm status myapp901 --show-resources
Observation: 
1. Two pods will be running as we changed replicacount to 2
2. Service Node Port will be 30092 

# Access Application
http://localhost:30092/page
Observation: 
1. We should see V2 application because we have used the "image tag as 2.0.0"
```




## Step-05: helm get values command
- **helm get values:** This command downloads a values file for a given release
```t
# helm get values
helm get values RELEASE_NAME
helm get values htmlpage

Observation:
1. Provides the values from current/latest release version 2 from Release htmlpage

## Sample Output
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm get values myapp901
USER-SUPPLIED VALUES:
image:
  pullPolicy: Always
  repository: nileshzarkar/htmlpage
  tag: 2.0.0
replicaCount: 2
service:
  nodePort: 30092
  type: NodePort


# helm history (History prints historical revisions for a given release.)
helm history htmlpage

PS D:\Experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm history htmlpage
REVISION        UPDATED                         STATUS          CHART           APP VERSION     DESCRIPTION
1               Tue Nov 12 06:01:06 2024        superseded      htmlpage-4.0.0  4.0.0           Install complete
2               Tue Nov 12 06:03:58 2024        deployed        htmlpage-4.0.0  4.0.0           Upgrade complete

# helm get values with --revision
helm get values RELEASE-NAME --revision int
helm get values htmlpage --revision 1

## Sample Output
PS D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm get values htmlpage --revision 1
USER-SUPPLIED VALUES:
service:
  nodePort: 30092

PS D:\Experiments\helm-experiments\helm-masterclass\helm-masterclass\09-Helm-Override-Values> helm get values htmlpage --revision 2
USER-SUPPLIED VALUES:
image:
  pullPolicy: Always
  repository: nileshzarkar/htmlpage
  tag: 2.0.0
replicaCount: 2
service:
  nodePort: 30092
  type: NodePort  
```




## Step-06: helm get manifest command 
- **helm get manifest:** This command fetches the generated manifest for a given release.
```t
# helm get manifest
helm get manifest RELEASE-NAME
helm get manifest htmlpage

# helm get manifest --revision
helm get manifest RELEASE-NAME --revision int
helm get manifest htmlpage --revision 1
```




## Step-07: helm get all command
- **helm get all:** This command prints a human readable collection of information about the notes, hooks, supplied values, and generated manifest file of the given release.
- This is a good way to see what templates are installed on the kubernetes cluster server.
- **helm get notes and helm get hooks:** These two commmands we will explore when we are discussing about helm chart development. 
```t
# helm get all
helm get all RELEASE-NAME
helm get all htmlpage
```




## Step-08: Uninstall Helm Release
```t


# Uninstall Helm Release
helm uninstall htmlpage

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
# Release: htmlpage
helm install htmlpage helm-repo/htmlpage --atomic
helm list
helm status htmlpage --show-resources
http://localhost:30082/page

# Option-1: Give desired port other than 31231
helm install htmlpage2 helm-repo/htmlpage --set service.nodePort=31232 

# Option-2: Pass null value to nodePort (service.nodePort=null)
helm install htmlpage3 helm-repo/htmlpage --set service.nodePort=null 
Note: Now the nodeport will be dynamic

http://localhost:32659/page

# Additional Notes for understanding
1. We will choose option-2 to demonstrate the concept "Deleting a default Key by passing null"
2. For NodePort Service, if we dont define the "nodePort" argument, it by default assigns a port dynamically from the port range 30000-32767. 
3. In our case already 31231 is used, other than that port it will allocate someother port when we pass null. 
4. In short, if we dont want to pass the default values present in values.yaml as-is, we dont need to change the complete chart with a new version, we can just pass null.

# Uninstall Releases
helm uninstall htmlpage
helm uninstall htmlpage2
helm uninstall htmlpage3
helm list
```