# Sample REST API microservice using Spring Boot

This repo can be used as reference to developing Spring Boot based microservices under Kubernetes container environment, which address the following concerns in a project:

## Technical Highlights

### developer workflow
Developer should be able to
* Run the microservice locally without deploying into container environment by using regular Spring Boot Maven plugin, when activating profile ```boot```
* Continuous build and test microservice in container environment by using [Skaffold](https://skaffold.dev/), by using ```skaffold dev```  

```text
# local build and test
# which execute clean test goals be default
mvn 

# run service locally, which runs clean spring-boot:run goals by default 
# requires boot profile to work 
mvn -P boot

# build docker image TAR file using JIB plugin
mvn -P jib

```
### automated build and deploy
* Super-fast and minimal overhead build using [Google Java Image Builder, aka. JIB](https://github.com/GoogleContainerTools/jib) 
* [Jenkins Pipeline](https://jenkins.io/doc/book/pipeline/) which can be triggered by Git repository server for pipelined build
* Deploy to container environment using [kustomize](https://kubectl.docs.kubernetes.io/pages/app_management/apply.html), which separate environment specific settings from common settings for the microservice itself.
* Environment specific settings are externalized into configmap and secrets that are not repeated in K8S Yaml files.

### product ready  
* Reads configuration from K8S config map and reload automatically once the config map is changed

