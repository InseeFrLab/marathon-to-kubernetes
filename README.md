# Marathon to Kubernetes

A small application that converts `marathon.json` contracts (used by [Marathon](https://github.com/mesosphere/marathon)) to `deployment.yml` files (used by [Kubernetes](https://kubernetes.io/).  
Support is quite minimal and was primarily tailored for our internal use at [Insee](https://insee.fr/) but you may find it handy for your usage. Feel free to contribute, issues & pull requests are welcome !

[![Build Status](https://travis-ci.org/inseefrlab/marathon-to-kubernetes.svg?branch=master)](https://travis-ci.org/inseefrlab/marathon-to-kubernetes) [![Maintainability](https://api.codeclimate.com/v1/badges/18c5a167cf83d3778bd9/maintainability)](https://codeclimate.com/github/InseeFrLab/marathon-to-kubernetes/maintainability) [![Coverage Status](https://coveralls.io/repos/github/InseeFrLab/marathon-to-kubernetes/badge.svg?branch=master)](https://coveralls.io/github/InseeFrLab/marathon-to-kubernetes?branch=master)

## Usage

### Using API

API can be run using either Java or Docker.  
Java release is available in the [Github releases page](https://github.com/InseeFrLab/marathon-to-kubernetes/releases)
You can run the API using Java (version 11+ required) :  
`java -jar marathon-to-kubernetes-web-version.jar`

Docker release is available at [Dockerhub](https://hub.docker.com/r/inseefrlab/marathon-to-kubernetes)  
`docker run -p 8080:8080 inseefrlab/marathon-to-kubernetes`

API will then be accessible at  
`http://localhost:8080/api/convert`  
OpenAPI description is available at  
`http://localhost:8080/api/`

### Including it in your Java project

The core `jar` is not (yet ?) available on maven central. For now you have to manually include it in your project.  
The `jar` file can be downloaded from the [Github releases page](https://github.com/InseeFrLab/marathon-to-kubernetes/releases).  
You can then install it locally or within your organisation :

```
mvn install:install-file -Dfile=./marathon-to-kubernetes-web-version.jar -DgroupId=fr.insee.innovation -DartifactId=marathon-to-kubernetes-core -Dversion=version -Dpackaging=jar
```

### Using CLI

CLI can be downloaded from the [Github releases page](https://github.com/InseeFrLab/marathon-to-kubernetes/releases)
You can run the converter using Java (version 11+ required) :  
`java -jar marathon-to-kubernetes-cli-version.jar [-n=<name>] [-o=<outputPath>] <inputFile>`

### Usage roadmap

In the future we intend to create a small web app with a JSON Editor (contributions welcome !)

## Compatibility

| Feature         | Status      | Comment                                                  |
| --------------- | ----------- | -------------------------------------------------------- |
| Single app      | Ok          |                                                          |
| Group           | In progress |                                                          |
| Instances       | Ok          |                                                          |
| CPU / Mem       | Ok          | Resources are defined as "resources requests" not limits |
| PORT_MAPPINGS   | Ok          |                                                          |
| Env             | Ok          |                                                          |
| HAPROXY_X_VHOST | Ok          |                                                          |
| Fetch           | In progress |                                                          |
| Healthcheck     |             |                                                          |
