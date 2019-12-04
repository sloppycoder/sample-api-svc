## Sample REST API microservice using Spring Boot


* Reads configuration from K8S config map and reload automatically once the config map is changed
* Service specific configuration can refer to external configmap and secret for common parameters, i.e. database URL
* Use to verify JWT token and control access to methods in controllers
* Use [Jib](https://github.com/GoogleContainerTools/jib) to compile service for faster build and app startup time
* Use custom base image with Jib, e.g. Redhat Universal Image
* Use [Skaffold](https://skaffold.dev/) to automate build and deployment for development cycles

### Prerequisites
Install (Minikube)[https://minikube.sigs.k8s.io/], and have docker binary available locally. The default configuration uses docker but use podman also works. 
Then enable to private registry

```shell script
minikube addons enable registry

```

Add output of ```minikube ip``` to hosts file,

```shell script
192.168.39.142 minikube minikube.local

```

Add the following to /etc/hosts inside the *Minikube VM*

```shell script
minikube ssh
sudo -s

echo '127.0.0.1 minikube.local' >> /etc/hosts
```

this hostname is referred to various places in the configurations.


### Test with Minikube
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
If the Minikube is configured to use podman instead of docker, add ```'minikube.local'``` to ```/etc/containers/registries.conf``` file under the ```[registries.insecure]``` section inside the *Minikube VM*. The result should look like this
```text
[registries.insecure]
registries = ['localhost:5000', 'minikube.local:5000']

```


docker binary is not so this it should work with Redhat Openshift 4.x but I haven't tested it yet.

```shell script
skaffold dev

# log should come out here...skaffold will automatically rebuild and deploy once the source code is changed

```

### Test with Redhat Code Ready container (4.2.4)
Follow the Minikube step until step mvn build step, at this point the image should be build and exist in local storage. Then proceed to push the same image to docker.io. This step is just a workaround to sidestep the need to setup another registory in Code Ready.
These steps should be rewritten to not rely on external docker registry.

```shell script
# podman and docker command are exchangable in this context
podman login -u <user_id> -p <password> index.docker.io 

# run the following to get image ID for use in the step that pushes the image 
podman images
podman push <image id> docker.io/<user_id>/sample-api-svc
# update the ocp/web.yaml file with image name above

# now run it with CodeReady, developer account does not have the privlege for creating RBAC roles, use admin instead
oc login -u admin -p <password>  https://api.crc.testing:6443 
oc apply -f ocp/
oc expose svc api
oc get route
# output similiar to below
NAME   HOST/PORT                      PATH   SERVICES   PORT   TERMINATION   WILDCARD
api    api-default.apps-crc.testing          api        http                 None

# try the service from outside 
curl http://api-default.apps-crc.testing/actuator/info
# should get some json output here

```

### Test with Redhat Universal Image
run build script in ubi directory. It uses podman and buildah, but can work with docker too.

```shell script
cd ubi
./build

```

