# Sample REST API microservice using Spring Boot

This repo can be used as reference to developing Spring Boot based microservices under Kubernetes container environment, which address the following concerns in a project:

## Technical Highlights

### developer workflow
Developer should be able to
* Run the microservice locally without deploying into container environment by using regular Spring Boot Maven plugin, when activating profile ```boot```

### automated build and deploy
* Super-fast and minimal overhead build using [Google Java Image Builder, aka. JIB](https://github.com/GoogleContainerTools/jib) 
* [Jenkins Pipeline](https://jenkins.io/doc/book/pipeline/) which can be triggered by Git repository server for pipelined build

