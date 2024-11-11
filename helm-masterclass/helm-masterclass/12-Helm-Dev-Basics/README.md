# Helm Template Functions and Pipelines

## Step-01: Introduction
1. Template Actions `{{ }}`
2. Action Elements `{{ .Release.Name }}`
3. Quote Function
4. Pipeline 
5. default Function
6. lower function
7. Controlling White Spaces `{{-  -}}`
7. indent function
8. nindent function
9. toYaml

## Step-02: Template Action "{{ }}"
- Anything in between Template Action `{{ .Chart.Name }}` is called Action Element
- Anything in between Template Action `{{ .Chart.Name }}` will be rendered by helm template engine and replace necessary values
- Anything outside of the template action will be printed as it is.
- Action elements defined inside the `{{ }}` will help us to retrieve data from other sources (example: `.Chart.Name`).

### Step-02-01: Valid Action Element
```t
# deployment.yaml file
apiVersion: apps/v1
kind: Deployment
metadata:
  # Template Action with Action Elements
  name: {{ .Release.Name }}-{{ .Chart.Name }}

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .
1. helm template command helps us to check the output of the chart in fully rendered Kubernetes resource templates. 
2. This will be very helpful when we are developing a new chart, making changes to the chart templates, for debugging etc.
```
### Step-02-02: Invalid Action Element 
```t
# deployment.yaml file
apiVersion: apps/v1
kind: Deployment
metadata:
  # Template Action with Action Elements
  name: {{ something }}-{{ .Chart.Name }}
# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .  
Observation:
1. Should fail with error
2. In short, inside Action Element we should have 

Error: parse error at (helmbasics/templates/deployment.yaml:10): function "something" not defined
```

## Step-03: Template Function: quote
```t

In Helm templates, quote functions are used to handle strings and variables to ensure they are correctly interpreted in Kubernetes manifests. Helm provides three main quote functions: quote, squote, and dqoute.
The quote function adds double quotes (" ") around a value, treating it as a single string
value: {{ .Values.myVar | quote }}
value: "hello world"
The squote function adds single quotes (' ') around a value, making it a single string with single quotes.
value: {{ .Values.myVar | squote }}
value: 'hello world'
The dquote function adds double quotes (" ") around a value, similar to quote, but explicitly for double quotes.
value: {{ .Values.myVar | dquote }}
value: "hello world"

# Add Quote Function 
  annotations:    
    app.kubernetes.io/managed-by: {{ .Release.Service }}
    # quote function
    app.kubernetes.io/managed-by: {{ quote .Release.Service }} 

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .
```

## Step-04: Pipeline
- Pipelines are an efficient way of getting several things done in sequence. 
- Inverting the order is a common practice in templates (.val | quote ) 
```t
In Helm, pipeline functions allow you to chain and transform data within templates, enabling you to modify or format values effectively. They’re a part of Helm’s templating system, which uses the Go template language. Pipelines let you pass the output of one function directly into another, allowing complex transformations in a readable, step-by-step manner.
Here's how a pipeline might look in a Helm template:
apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ .Release.Name | lower }}-config
data:
  message: {{ .Values.message | default "Hello, World!" | upper | quote }}

In this example:

    .Values.message is taken from the values file.
    If .Values.message is empty or undefined, "Hello, World!" is used as a default.
    The message is then converted to uppercase.
    Finally, it is wrapped in double quotes using quote.

# Add Quote Function with Pipeline
  annotations:    
    app.kubernetes.io/managed-by: {{ .Release.Service }}
    # quote function
    app.kubernetes.io/managed-by: {{ quote .Release.Service }} 
    # quote function with pipeline (here in pipeline order is from LEFT --> RIGHT)
    app.kubernetes.io/managed-by: {{ .Release.Service | quote }}               

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .
```

## Step-05: Template Function: default and lower
- [default function](https://helm.sh/docs/chart_template_guide/function_list/#default)
```t

In Helm, the default function is used within templates to provide a default value if a given value is not set or is empty. This is particularly useful for ensuring that templates render properly even when some values are missing or optional in your values.yaml file.
Syntax
{{ default <default-value> <value-to-check> }}
<default-value>: The value to use if <value-to-check> is empty, null, or undefined.
<value-to-check>: The value you want to check. If this value is missing, the function returns <default-value> instead.

Providing a Default Value for a Missing Configuration
apiVersion: v1
kind: Service
metadata:
  name: {{ .Release.Name }}-service
spec:
  type: {{ default "ClusterIP" .Values.service.type }}
  ports:
    - port: 80
      targetPort: 80
Here, if .Values.service.type is not specified in values.yaml, the service type will default to ClusterIP.

Setting a Default Label
metadata:
  labels:
    app: {{ default "my-app" .Values.appName }}
If .Values.appName is not set, the label app will default to "my-app".

Using Default with Optional Container Ports
containers:
  - name: my-container
    image: {{ .Values.image.repository }}:{{ .Values.image.tag }}
    ports:
      - containerPort: {{ default 80 .Values.containerPort }}
This example will use port 80 if .Values.containerPort is not defined in the values.yaml file.

# values.yaml
releaseName: "newrelease101"
replicaCount: 3

# Template Function default
  annotations:
    app.kubernetes.io/managed-by: {{ .Release.Service }}
    # Quote Function
    app.kubernetes.io/managed-by: {{ quote .Release.Service }}        
    # Pipeline
    app.kubernetes.io/managed-by: {{ .Release.Service | quote | upper | lower }}        
    # default Function
    app.kubernetes.io/name: {{ default "MYRELEASE101" .Values.releaseName | lower }}
spec:
  replicas: {{ default 1  .Values.replicaCount }}

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .
```

## Step-06: Controlling Whitespaces
- **{{- .Chart.name }}:**  If a hyphen is added before the statement, `{{- .Chart.name }}` then the leading whitespace will be ignored during the rendering
- **{{ .Chart.name -}}:** If a hyphen is added after the statement, `{{ .Chart.name -}}` then the trailing whitespace will be ignored during the rendering
```t
In Helm, whitespaces are controlled mainly through YAML templating and Go template syntax to ensure clean, readable, and correct output files. Here’s how whitespace handling works in Helm templates
Trimming Whitespaces with Go Template Syntax
Helm uses Go templating, where special syntax helps control whitespace. You can use - before or after {{ or }} to trim spaces:
{{- ... -}}  # Trims both leading and trailing spaces
{{- ... }}   # Trims leading spaces
{{ ... -}}   # Trims trailing spaces
spec:
  containers:
    - name: {{ .Chart.Name }}
      image: "{{ .Values.image.repository }}:{{ .Values.image.tag }}"
      {{- if .Values.serviceAccount }}
      serviceAccountName: {{ .Values.serviceAccount }}
      {{- end }}
Here, {{- if .Values.serviceAccount }} trims any extra whitespace before the serviceAccountName line if the condition is not met, resulting in a clean YAML file.
Managing Blank Lines
Helm automatically removes blank lines that result from unused or empty templates.
However, if you want to control it manually, using {{- or -}} around conditional statements or loops helps avoid blank lines when there’s no output.
{{- if .Values.resources }}
resources:
  {{- toYaml .Values.resources | nindent 2 }}
{{- end }}
This code block will only add the resources section if .Values.resources has content, without leaving unnecessary blank lines if the condition is not met.
Whitespace in YAML Syntax
YAML relies on consistent indentation, so Helm templates should be carefully structured. Avoid inconsistent indentations within templates to prevent YAML parsing errors.
```
```yaml
  annotations:
    app.kubernetes.io/managed-by: {{ .Release.Service }}
    # Quote Function
    app.kubernetes.io/managed-by: {{ quote .Release.Service }}        
    # Pipeline
    app.kubernetes.io/managed-by: {{ .Release.Service | quote | upper | lower }}        
    # default Function
    app.kubernetes.io/name: {{ default "MYRELEASE101" .Values.releaseName }}
    # Controlling Leading and Trailing White spaces 
    leading-whitespace: "   {{- .Chart.Name }}    kalyan"
    trailing-whitespace: "   {{ .Chart.Name -}}    kalyan"
    leadtrail-whitespace: "   {{- .Chart.Name -}}    kalyan"    

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .    
```


## Step-07: indent and nindent functions
- **indent:** The [indent function](https://helm.sh/docs/chart_template_guide/function_list/#indent) indents every line in a given string to the specified indent width. This is useful when aligning multi-line strings:
- **nindent:** The [nindent function](https://helm.sh/docs/chart_template_guide/function_list/#nindent) is the same as the indent function, but prepends a new line to the beginning of the string.
```t
Using nindent and indent Functions
The nindent function (newline + indent) adds indentation to a block while keeping newlines, which is especially helpful for nested structures.
The indent function adds a specific number of spaces to each line.
metadata:
  labels:
    {{- toYaml .Values.labels | nindent 6 }}
Here, nindent 6 indents the labels by 6 spaces and keeps proper alignment within the YAML structure.
```
```yaml
# indent and nindent functions
  annotations:
    app.kubernetes.io/managed-by: {{ .Release.Service }}
    # Quote Function
    app.kubernetes.io/managed-by: {{ quote .Release.Service }}        
    # Pipeline
    app.kubernetes.io/managed-by: {{ .Release.Service | quote | upper | lower }}        
    # default Function
    app.kubernetes.io/name: {{ default "MYRELEASE101" .Values.releaseName | lower }}
    # Controlling Leading and Trailing White spaces 
    leading-whitespace: "   {{- .Chart.Name }}    kalyan"
    trailing-whitespace: "   {{ .Chart.Name -}}    kalyan"
    leadtrail-whitespace: "   {{- .Chart.Name -}}    kalyan"  
    # indent function
    indenttest: "  {{- .Chart.Name | indent 4 -}}  "
    # nindent function
    nindenttest: "  {{- .Chart.Name | nindent 4 -}}  "  

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .    
```


## Step-08: Template Function: toYaml 
- **toYaml:** 
- We can use [toYaml function](https://helm.sh/docs/chart_template_guide/function_list/#type-conversion-functions) inside the helm template actions to convert an object into YAML.
- Convert list, slice, array, dict, or object to indented yaml. 
```t
# values.yaml
# Resources for testing Template Function: toYaml 
resources: 
  limits:
    cpu: 100m
    memory: 128Mi
  requests:
    cpu: 100m
    memory: 128Mi

# deployment.yaml
    spec:
      containers:
      - name: nginx
        image: ghcr.io/stacksimplify/kubenginx:4.0.0
        ports:
        - containerPort: 80
        resources: 
        {{- toYaml .Values.resources | nindent 10}}

# Change to CHART Directory
cd helmbasics

# Helm Template Command
helm template myapp101 .

# Helm Install with --dry-run
helm install myapp101 . --dry-run

# Helm Install
helm install myapp101 . --atomic

# List k8s Pods
kubectl get pods 

# Describe Pod
kubectl describe pod <POD-NAME>

# Helm Uninstall
helm uninstall myapp101
```