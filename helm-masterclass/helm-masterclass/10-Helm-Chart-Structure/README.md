# Understand Helm Chart Folder Structure



## Step-01: Introduction
- Understand Helm Chart Folder Structure



## Step-02: Helm Create Chart
```t
# Helm Create Chart
helm create <CHART-NAME>
helm create basechart
Observation: 
1. It will create a Helm Chart template 
2. We can call it like a helm chart created from a default starter chart

The command helm create basechart is used to create a new Helm chart directory with the name basechart. Helm automatically generates a template for this chart, providing a starting structure for developing a new Helm chart.
Command Breakdown
    helm create: This command generates the scaffolding for a new Helm chart.
    basechart: This is the name of the chart that Helm will create. You can replace basechart with any other name you want for your chart.
What Happens When You Run This Command?
When you execute helm create basechart, Helm will create a new directory called basechart with the following structure:
```



## Step-03: Helm Chart Structure
```
└── basechart
    ├── .helmignore
    ├── Chart.yaml
    ├── LICENSE
    ├── README.md
    ├── charts
    ├── templates
    │   ├── NOTES.txt
    │   ├── _helpers.tpl
    │   ├── deployment.yaml
    │   ├── hpa.yaml
    │   ├── ingress.yaml
    │   ├── service.yaml
    │   ├── serviceaccount.yaml
    │   └── tests
    │       └── test-connection.yaml
    └── values.yaml



```t
Explanation of the Structure
    Chart.yaml: Contains metadata about the chart, such as its name, description, version, and API version.
    values.yaml: The default configuration values for the chart, allowing users to override settings when deploying.
    templates/: Contains YAML templates for Kubernetes resources, such as deployments and services. Helm replaces placeholders in these templates with values from values.yaml and other sources.
    charts/: A folder for including any dependent charts.
    .helmignore: Specifies files or directories Helm should ignore when packaging the chart.
```



```t
Chart.yaml
This YAML file is the Chart.yaml file for a Helm chart named basechart. It provides essential metadata about the chart, including its version, application version, description, and type.
Explanation of Each Field
    apiVersion: v2:
        Specifies the API version for Helm. Version v2 is required for Helm 3 and includes support for features like dependency management and chart hooks.
    name: basechart:
        The name of the chart. In this case, it’s basechart. This is the name that will appear when you run helm list or view the chart in other Helm commands.
    description: A Helm chart for Kubernetes:
        A brief description of the chart. It helps provide context about the purpose of the chart.
    type: application:
        Specifies the type of chart:
            application: A deployable collection of Kubernetes resources, which is the standard type for charts.
            library: Provides reusable utilities or functions for chart developers but cannot be deployed on its own. It can be used as a dependency in other charts to add utilities to the rendering process.
        Here, type: application means this chart can be deployed to Kubernetes.
    version: 0.1.0:
        This is the version number for the chart itself, following Semantic Versioning (https://semver.org/).
        Each time you make changes to the chart’s templates or configuration, this version should be incremented.
        The format is typically major.minor.patch (e.g., 1.2.3).
    appVersion: "1.16.0":
        This specifies the version of the actual application being deployed, which can differ from the chart version.
        Unlike version, appVersion does not have to follow Semantic Versioning.
        Typically, you increment this version each time the underlying application (e.g., a Docker image) is updated. The quotes around the version (e.g., "1.16.0") help avoid issues with YAML parsers that might misinterpret numeric versions.
Summary
This Chart.yaml file serves as the configuration metadata for the Helm chart basechart, version 0.1.0, which deploys an application with version 1.16.0. The type field indicates this is an application chart, meaning it contains templates and resources meant for deployment, as opposed to a library chart, which would only provide reusable utilities for other charts.
```



```t
When do you typically increment the Chart version ?
The Chart version (specified as version in Chart.yaml) is typically incremented whenever you make a change to the Helm chart itself, even if the application version (appVersion) or Docker image tag hasn’t changed. This practice allows you to keep track of the evolution of the chart’s configurations, templates, and features independently of the application code.
Common Scenarios for Incrementing Chart Version
    Updating Chart Templates or Configurations:
        Any change to the YAML templates or configuration files within the Helm chart requires a version increment.
        Example: Adding new resource definitions (like ConfigMaps, Secrets), changing values.yaml structure, or modifying existing Kubernetes resources (e.g., modifying a Deployment’s resource limits).
    Bug Fixes or Improvements in the Chart:
        If you fix a bug in the Helm chart (e.g., correcting an incorrect label or setting) or make an improvement (e.g., adding a liveness probe), incrementing the chart version allows users to distinguish between the old and new configurations.
        This also applies to any Helm logic changes (like conditions, loops, or default values).
    Adding New Configuration Options in values.yaml:
        If you add new configurable parameters in values.yaml (like additional environment variables, resource limits, or replicas), you should increment the chart version to reflect this change.
        This helps indicate that the chart now supports more customization.
    Updating Dependencies:
        If your Helm chart relies on other charts as dependencies (e.g., a Redis chart dependency), updating those dependencies would also warrant a chart version increment.
        This signals to users that the dependency versions have changed, which could impact the overall deployment.
    Compatibility Changes:
        If there are changes to the chart that impact compatibility with previous configurations, Kubernetes versions, or dependent resources, you should increment the chart version.
        For example, if a change requires users to set new values in values.yaml or updates the minimum Kubernetes version.
Example of Versioning
In Chart.yaml:
apiVersion: v2
name: my-app
version: 1.1.0       # Incremented chart version
appVersion: "1.0.0"  # Application version remains the same

Versioning Conventions
Helm charts commonly follow semantic versioning (MAJOR.MINOR.PATCH):
    PATCH (X.X.1): Increment for minor fixes or small, backward-compatible changes.
    MINOR (X.1.X): Increment for new features or configurations that are backward-compatible.
    MAJOR (1.X.X): Increment for significant changes that may be incompatible with previous versions.

Summary
You should increment the chart version anytime you make changes to the Helm chart itself, regardless of the application code or Docker image version. This practice keeps your chart versions aligned with the changes, allowing users to track, audit, and revert to specific chart configurations as needed.
```



```t
values.yaml
In Helm, the values.yaml file is a configuration file that defines the default values for variables in a Helm chart. This file allows you to customize the behavior of the charts templates by specifying the values of various configuration parameters. When you deploy a Helm chart, Helm uses these values (along with any user-provided overrides) to render the templates and create the necessary Kubernetes resources.
Purpose of values.yaml
    Centralized Configuration: It provides a central place to define all default configuration values for a chart.
    Customizability: Users can override values in values.yaml by providing their own custom values.yaml file or by using the --set flag during helm install or helm upgrade commands.
    Dynamic Template Rendering: Templates in the chart directory (usually in the templates/ folder) reference values from values.yaml, allowing the chart to adapt to different configurations without modifying the templates directly.
Structure of values.yaml
The structure of values.yaml is YAML-based and typically includes key-value pairs that map to variables used in the templates. It may contain default settings for resources like replicas, image versions, resource limits, service types, and custom application settings.
Example of a values.yaml File

replicaCount: 3
image:
  repository: nginx
  tag: "1.19.0"
  pullPolicy: IfNotPresent
service:
  type: ClusterIP
  port: 80
resources:
  limits:
    cpu: "500m"
    memory: "128Mi"
  requests:
    cpu: "250m"
    memory: "64Mi"

In this example:
    replicaCount is set to 3, meaning the deployment will start with 3 replicas by default.
    The image section specifies the default container image configuration.
    The service section configures the Kubernetes service type and port.
    The resources section defines CPU and memory limits and requests.

How to Override Values

Using a Custom values.yaml File:
helm install my-release ./mychart -f custom-values.yaml
This will use the values in custom-values.yaml to override the defaults in values.yaml.

Using the --set Flag:
helm install my-release ./mychart --set replicaCount=5,image.tag=1.19.1
This directly overrides specific values on the command line.

Example Use in a Template
In the deployment.yaml template, you might see:
replicas: {{ .Values.replicaCount }}
image: {{ .Values.image.repository }}:{{ .Values.image.tag }}
This pulls values from values.yaml (or any user-provided overrides) to configure the deployment.
```


```t
charts folder
In a Helm chart, the charts folder is a directory that contains any dependent charts or subcharts required by the main chart. It allows you to package and manage dependencies alongside the primary chart, enabling modular and reusable configurations.
Purpose of the charts Folder
    Manage Dependencies: The charts folder holds other Helm charts that the main chart depends on. For example, if your application requires a database, you could include a PostgreSQL chart as a dependency.
    Version Control: By storing specific versions of dependencies, the charts folder ensures compatibility and consistency, especially when deploying the chart across different environments.
    Encapsulate Functionality: Each subchart in the charts folder provides specific functionality (e.g., a caching layer, a database) that can be configured independently from the main chart.
How Dependencies Work
    Defining Dependencies: Dependencies for a Helm chart are typically listed in the Chart.yaml file under the dependencies section, specifying each dependency’s name, version, and repository.
    Download Dependencies: When you run helm dependency update, Helm downloads the specified dependencies and places them into the charts folder.
    Nested Configuration: Each subchart has its own values.yaml file and configurations, but you can override these values in the main charts values.yaml file under the name of the dependency.
Example Chart.yaml with Dependencies
In Chart.yaml, you might see dependencies listed like this:

apiVersion: v2
name: mychart
version: 0.1.0
dependencies:
  - name: redis
    version: 6.0.0
    repository: https://charts.bitnami.com/bitnami
  - name: postgresql
    version: 10.3.11
    repository: https://charts.bitnami.com/bitnami

In this example:
    The main chart depends on two charts, redis and postgresql.
    These dependencies will be fetched and stored in the charts folder when you run helm dependency update.

Usage and Configuration
    Download Dependencies:
    helm dependency update
This command downloads the listed dependencies and places them in the charts folder.

Overriding Values:
    You can override values for dependencies in the main charts values.yaml file under the dependency’s name, such as:
redis:
  replicaCount: 2
postgresql:
  image:
    tag: "13.3"

Summary
The charts folder is essential for managing and packaging dependencies in Helm. By allowing dependencies to be stored alongside the main chart, Helm simplifies complex applications by managing their entire dependency stack as a single deployable package.
```



```t
templates folder
In a Helm chart, the templates folder contains all the Kubernetes resource templates that Helm will use to generate manifests for deployment. These templates are written in YAML and contain placeholders and logic (using the Go templating language) that dynamically render configurations based on values provided in the chart’s values.yaml file or any user-provided overrides.
Purpose of the templates Folder
    Dynamic Resource Generation: The templates use variables and conditional logic to customize Kubernetes manifests (like Deployments, Services, ConfigMaps, etc.) based on environment-specific or user-specific requirements.
    Separation of Configuration: The templates folder allows Helm charts to adapt easily to different environments by adjusting values instead of rewriting manifests.
    Modularity: Different templates can define separate Kubernetes resources, enabling flexible configurations and easy updates.
Typical Contents of the templates Folder
    Resource Manifests: YAML files defining Kubernetes resources, such as:
        deployment.yaml: Configures deployments.
        service.yaml: Defines services.
        configmap.yaml: Configures ConfigMaps.
        ingress.yaml: Configures Ingress resources.
    Helper Files:
        _helpers.tpl: A file that defines reusable template functions and labels, often used to simplify and organize repeated content across multiple templates.
    Test Templates (optional):
        tests/: A folder with templates for test resources (like test-connection.yaml), which Helm can use to verify that the deployment is working as expected after installation.
Example of a Template
In deployment.yaml, you might see:

apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ .Release.Name }}-app
  labels:
    app: {{ .Release.Name }}
spec:
  replicas: {{ .Values.replicaCount }}
  selector:
    matchLabels:
      app: {{ .Release.Name }}
  template:
    metadata:
      labels:
        app: {{ .Release.Name }}
    spec:
      containers:
        - name: app
          image: {{ .Values.image.repository }}:{{ .Values.image.tag }}
          ports:
            - containerPort: 80

In this example:
    {{ .Release.Name }} inserts the release name, allowing unique names based on the release.
    {{ .Values.replicaCount }} and {{ .Values.image.repository }} use values from values.yaml, making it easy to adjust these settings without changing the template.

How Templates Work with values.yaml
Templates pull data from values.yaml, allowing you to modify key settings (like image tags or replica counts) through the values file. For example, setting replicaCount: 3 in values.yaml will adjust the replicas in deployment.yaml accordingly.

Summary
The templates folder in Helm is crucial for defining the Kubernetes resources that make up an application, using a dynamic approach that allows configuration based on reusable values and conditions. This flexibility makes Helm charts versatile and adaptable for a wide range of environments and use cases.
```



```t
_helpers.tpl
In a Helm chart, the _helpers.tpl file is a special template file used to define reusable template functions, variables, and helpers. These functions can be referenced throughout other templates in the chart, making it easier to manage and standardize commonly used snippets, labels, or formatting within a Helm chart.
Purpose of _helpers.tpl
    Reusability: Helps avoid redundancy by defining commonly used values, labels, or logic once and using it multiple times in different templates.
    Consistency: Ensures that labels, annotations, and other settings are applied consistently across all Kubernetes resources within the chart.
    Readability: Improves readability by moving complex logic or repeated values out of individual templates, making the primary templates more concise.
Example Contents of _helpers.tpl
Typically, _helpers.tpl contains helper templates for labels, resource names, and common annotations. These helpers are defined using Go template syntax.
Example _helpers.tpl:

{{- define "mychart.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "mychart.labels" -}}
app.kubernetes.io/name: {{ include "mychart.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
app.kubernetes.io/version: {{ .Chart.AppVersion }}
helm.sh/chart: {{ .Chart.Name }}-{{ .Chart.Version }}
{{- end -}}

Explanation of Example
    "mychart.fullname":
        Defines a template that generates a full name for a resource by combining the release name and chart name.
        The function trunc 63 limits the name to 63 characters (the maximum length for Kubernetes names), and trimSuffix "-" removes any trailing hyphens.
    "mychart.labels":
        Defines common labels to be applied across multiple resources.
        The labels use built-in Helm variables like .Release.Name (release name) and .Chart.AppVersion (application version).

How to Use Helpers in Other Templates
To use a helper function defined in _helpers.tpl in another template, you use the include function. For example, in deployment.yaml:
apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "mychart.fullname" . }}
  labels:
    {{ include "mychart.labels" . | nindent 4 }}
spec:
  replicas: {{ .Values.replicaCount }}
  template:
    metadata:
      labels:
        {{ include "mychart.labels" . | nindent 8 }}

In this example:
    {{ include "mychart.fullname" . }} calls the mychart.fullname helper to generate a consistent name.
    {{ include "mychart.labels" . | nindent 4 }} applies consistent labels to the deployment and indents them properly with nindent.
    
Summary
The _helpers.tpl file in Helm provides a convenient way to define reusable functions, variables, and labels, making Helm charts more modular, maintainable, and consistent. By centralizing common patterns, _helpers.tpl helps you avoid redundancy and enhances the readability of your Kubernetes manifests.
```

