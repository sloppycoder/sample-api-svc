# Sample REST API microservice using Spring Boot

This repo can be used as reference to developing Spring Boot based microservices under Kubernetes container environment, which address the following concerns in a project:

## Technical Highlights
* Super-fast and minimal overhead build using [Google Java Image Builder, aka. JIB](https://github.com/GoogleContainerTools/jib) 
* [Jenkins Pipeline](https://jenkins.io/doc/book/pipeline/) which can be triggered by Git repository server for pipelined build
* Deploy to container environment using [kustomize](https://kubectl.docs.kubernetes.io/pages/app_management/apply.html), which separate environment specific settings from common settings for the microservice itself.
* Environment specific settings are externalized into configmap and secrets that are not repeated in K8S Yaml files.

### developer workflow
Developer should be able to
* Run the microservice locally without deploying into container environment by using regular Spring Boot Maven plugin, when activating profile ```boot```
* Continuous build and test microservice in container environment by using [Skaffold](https://skaffold.dev/), by using ```skaffold dev``` using a local or remote container runtime environment.  

```text
# local build and test
# which execute clean test goals be default
mvn 

# run service locally, which runs clean spring-boot:run goals by default 
# requires boot profile to work 
mvn -P boot

# build docker image and push image to a remote registry using JIB plugin
mvn -Dpush.image.tag=dev -Dpush.image.base=localhost:32000/dev -P jib

# continuous dev and test using skaffold
# make sure that you have access to a container runtime environment
# see below microk8s section if you don't have one already
skaffold dev
```

### use microk8s for local development
Use [this script](misc/setup_microk8s.sh) to install microk8s and tools if needed.

[microk8s](https://microk8s.io/) is a light weight Kubernetes runtime for Debian/Ubuntu Linux developed by [Canonical](https://canonical.com/). 
Follow the [quick start](https://microk8s.io/docs/) to get an enviornment up and running locally.


### use minikube for local development
Install [Minikube](https://minikube.sigs.k8s.io/), and have docker binary available locally. The default configuration uses docker but use podman also works. 
Then enable to private registry

```shell script
minikube addons enable registry
```

more instructions coming soon...


### use [Redhat CodeReady Container](https://developers.redhat.com/products/codeready-containers/overview) for local development

By default the built-in registry is enabled, and accessible from ```image-registry.openshift-image-registry.svc:5000``` from within the cluster, and ```default-route-openshift-image-registry.apps-crc.testing``` from outside, so the deployment yaml files needs to be adjusted.

skaffold is not yet working. see [this github issue](https://github.com/GoogleContainerTools/skaffold/issues/3530)

```
# login to CRC and get token 
oc login -u <user> -p <password> https://api.crc.testing:6443   
oc whoami -t

# the above command will print out a token, which will be used as password to container registry
mvn -P jib -Dpush.image.base=default-route-openshift-image-registry.apps-crc.testing/default -Djib.auth.to.username=kubeadmin -Djib.auth.to.password=<toke
oc apply -k k8s/overlays/crc

# test the api, should get some response
curl http://sample-svc-default.apps-crc.testing

# if you have ~/.docker/config.json file, meaning you have a docker client, 
# the above -Djib.auth will not work, do a docker login instead

oc login -u <your_user_name> -p <your_password>  https://api.crc.testing:6443
oc whoami -t | docker login -u <your_user_name> --password-stdin  https://default-route-openshift-image-registry.apps-crc.testing

```

