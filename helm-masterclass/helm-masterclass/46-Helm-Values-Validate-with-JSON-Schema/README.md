# Helm Values - Validate with JSON Schema




## Step-01: Introduction
- Helm Values - Validate with JSON Schema
Helm3 added support to validate values in values.yaml using Json schema

Requirement checks:
Example: Required fields in values.yaml

Constraint Validation: 
Example: pullPolicy should contain only values from below 3 items
  IfNotPresent, 
  Always, or 
  Never

Type validation: 
Example-1: Replica Count  is a number or integer and not a string
Example-2: Image Tag is a string such as “0.1.0" and not the number 0.1.0

Range validation: 
Example: The value for a CPU utilization percentage key is between 1 and 100
In short, we can add restrictions in our values.yaml using values.schema.json file




## Step-02: Review helmbasics Helm Chart
- Simple Helm Chart
- deployment.yaml
- Core focus will be on learning about `values.schema.json`




## Step-03: Convert values.yaml to json
-  [Use website json2yaml](https://www.json2yaml.com/)




## Step-04: Convert Json to Json Schema
- [Use website](https://transform.tools/json-to-json-schema)

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "Generated schema for Root",
  "type": "object",
  "properties": {
    "replicaCount": {
      "type": "number"
    },
    "image": {
      "type": "object",
      "properties": {
        "repository": {
          "type": "string"
        },
        "pullPolicy": {
          "type": "string"
        },
        "tag": {
          "type": "string"
        }
      },
      "required": [
        "repository",
        "pullPolicy",
        "tag"
      ]
    }
  },
  "required": [
    "replicaCount",
    "image"
  ]
}
```




## Step-05: Create file values.schema.json on Helm Chart Root Directory
- Create file `values.schema.json`
- Copy JSON content from previous step




## Step-06: Add Pattern for pullPolicy
```json
        "pullPolicy": {
          "type": "string",
          "pattern": "^(Always|Never|IfNotPresent)$"
        },
```




## Step-06: Verify values.schema.json
```t
# Change to Chart Directory
cd helmbasics

# Helm lint
helm lint .

# Required Test: Pass null value and verify
helm template myapp1 . --set replicaCount=""

# Integer Test: Provide replicaCount as String
helm template myapp1 . --set replicaCount=kalyan

# Constraint Validation Test: Provide invalid value instead of allowed values (Allowed Values: Always, Never, IfNotPresent)
helm template myapp1 . --set image.pullPolicy=kalyan
```