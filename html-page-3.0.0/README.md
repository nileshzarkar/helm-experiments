# html-page-3.0.0

=================================================================

Creating a docker image of these microservices without creating a docker file 

Alternatively, if you're using Maven, add the extension manually to your pom.xml:
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-container-image-docker</artifactId>
</dependency>

If you prefer to use other tools like Jib or Buildah, replace container-image-docker with container-image-jib or container-image-buildah

To configure how your Docker image will be generated, you need to define some properties in the application.properties file.

Open the src/main/resources/application.properties file and add the following configurations:

# Application name and version (for tagging the Docker image)
quarkus.application.name=htmlpage
quarkus.application.version=1.0.0

# Docker image build settings
quarkus.container-image.build=true
quarkus.container-image.name=htmlpage
quarkus.container-image.tag=1.0.0

# Set Quarkus to expose port 8081 or what port you want to expose in the Dockerfile
quarkus.container-image.expose=true
quarkus.container-image.ports=8080

Once you have added the extension and configured the properties, you can build the Quarkus application and the Docker image using the following Maven command:
./mvnw clean package
or 
./mvnw clean package -Dquarkus.container-image.build=true

Once the build is complete, verify that the Docker image was created successfully:
docker images

You can now run the Docker image locally:
docker run -i --rm -p 8080:8080 niles/htmlpage:1.0.0

If you want to push the Docker image to a container registry (e.g., Docker Hub or a private registry),
you can configure Quarkus to do so by specifying the registry details in the application.properties:

quarkus.container-image.registry=docker.io
quarkus.container-image.group=my-dockerhub-username
or
push the image using:
./mvnw clean package -Dquarkus.container-image.push=true
or

docker tag niles/htmlpage:1.0.0 nileshzarkar/htmlpage:1.0.0
docker push nileshzarkar/htmlpage:1.0.0

docker tag niles/htmlpage:2.0.0 nileshzarkar/htmlpage:2.0.0
docker push nileshzarkar/htmlpage:2.0.0

docker tag niles/htmlpage:3.0.0 nileshzarkar/htmlpage:3.0.0
docker push nileshzarkar/htmlpage:3.0.0

docker tag niles/htmlpage:4.0.0 nileshzarkar/htmlpage:4.0.0
docker push nileshzarkar/htmlpage:4.0.0

============================================================

Creating the helm chart of htmlpage microservice

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\html-page-3.0.0>helm create htmlpage
Creating htmlpage


Update Chart.yaml
apiVersion: v2
name: htmlpage
description: A Helm chart for Kubernetes
type: application
version: 3.0.0
appVersion: "3.0.0"


Update values.yaml for below sections
...
image:
  repository: nileshzarkar/htmlpage
  pullPolicy: Always
  tag: "3.0.0"
...
serviceAccount:
  create: false
...
service:
  type: NodePort
  port: 8080
  targetPort: 8080
  nodePort: 30082
...
config:
  pageColor: "blue"
...


Update deployment.yaml below section to add the env as below
...
              protocol: TCP
          env:
            - name: page.color
              value: {{ .Values.config.pageColor }}        
          livenessProbe:
...


Update service.yaml sections as below to add nodeport config
...
spec:
  type: {{ .Values.service.type }}
  ports:
    - port: {{ .Values.service.port }}
      targetPort: {{ .Values.service.targetPort }}
      nodePort: {{ .Values.service.nodePort }}
      protocol: TCP
      name: http
  selector:
...


# helm dependency build htmlpage  - This only if there is dependency chart

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\html-page-3.0.0>helm install htmlpage htmlpage
or
D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\html-page-3.0.0\htmlpage>helm install htmlpage .

Open Lens/kubernetes UI and see if the kubernetes resource are installed/deployed
deployments,pods,service

Check the nodeport in services for htmlpage service
http://localhost:30082/page

D:\Experiments\helm-experiments\helm-experiments\helm-masterclass\html-page-3.0.0>helm uninstall htmlpage

=======================================================================
