# Helm Install with Generate Name Flag



## Step-01: Introduction
- `--generate-name` flag for `helm install` is very very important option
- This is one of the good to know option from `helm install` perspective
- When we are implementing DevOps Pipelines, if we want to generate the names of our releases without throwing duplicate release errors we can use this setting. 



## Step-02: Install helm with --generate-name flag
```t
# Install helm with --generate-name flag
helm install <repo_name_in_your_local_desktop/chart_name> --generate-name
helm install stacksimplify/mychart1 --generate-name

The helm install --generate-name command installs a Helm chart with a system-generated, unique name instead of requiring you to specify a release name manually. This is helpful when you don’t need to control the release name and want Helm to automatically handle naming.
--generate-name Flag:
    When --generate-name is used, Helm creates a unique name for the release, combining a prefix (usually based on the chart name) with a random suffix.
    This is useful for testing or situations where you don’t need to manage release names explicitly.
Output:
    Helm will display the generated release name in the terminal output, which you can use to reference the release later.
Use Cases
    Automated Deployments: Useful in scripts or CI/CD pipelines where you may not need to assign specific release names.
    Testing and Development: For quick, repeated deployments without manually providing a name each time.

# List Helm Releases
helm list
helm list --output=yaml
Observation:
We can see the name as "name: mychart1-1689683948" some auto-generated number

# Helm Status
helm status mychart1-1689683948 
helm status mychart1-1689683948 --show-resources

# Access Application
http://localhost:31231
```



## Step-03: Uninstall Helm Release
```t
# Uninstall Helm Release
helm uninstall <RELEASE-NAME>
helm uninstall mychart1-1689683948
```

