## Sample REST API microservice using Spring Boot


* Reads configuration from K8S config map and reload automatically once the config map is changed
* Service specific configuration can refer to external configmap and secret for common parameters, i.e. database URL
* Use to verify JWT token and control access to methods in controllers
* Use [Jib](https://github.com/GoogleContainerTools/jib) to compile service for faster build and app startup time
* Use custom base image with Jib, e.g. Redhat Universal Image
* Use [Skaffold](https://skaffold.dev/) to automate build and deployment for development cycles

### Prerequisites
Install (minikube)[https://minikube.sigs.k8s.io/], and have docker binary available locally. Docker binary is only required for skaffold and does not require the docker daemon to be running. Then enable to private registry

```shell script
minikube addons enable registry

```

Add output of ```minikube ip``` to hosts file,

```shell script
192.168.39.142 minikube minikube.local

```
this hostname is referred to various places in the configurations.

### Build and run the service
docker, podman are not required for this step

```shell script

# build the image to push to docker registry
mvn clean package jib:build -P jib-build

# run the service
kubeclt apply -f k8s/

# check pod log
kubectl logs -f api-xxxxx

# you should output similiar to the following
...
net.vino9.sample.ApiController           : my name is Name From Common Config Map, my pass is my-super-secret-from


```

### Even simpler workflow with Skaffold
docker binary is required and currently does not work with podman, thus won't work with Redhat Openshift 4.x

```shell script
skaffold dev

# log should come out here...skaffold will automatically rebuild and deploy once the source code is changed

```

### Test with Redhat Universal Image
run build script in ubi directory. It uses podman and buildah, but can work with docker too.

```shell script
cd ubi
./build

```

