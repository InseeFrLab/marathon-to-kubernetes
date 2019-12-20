# Marathon to Kubernetes

A small application that converts `marathon.json` contracts (used by [Marathon](https://github.com/mesosphere/marathon)) to `deployment.yml` files (used by [Kubernetes](https://kubernetes.io/).  
Support is quite minimal and was primarily tailored for our internal use at [Insee](https://insee.fr/) but you may find it handy for your usage. Feel free to contribute, issues & pull requests are welcome !

[![Build Status](https://travis-ci.org/inseefrlab/marathon-to-kubernetes.svg?branch=master)](https://travis-ci.org/inseefrlab/marathon-to-kubernetes) [![Maintainability](https://api.codeclimate.com/v1/badges/18c5a167cf83d3778bd9/maintainability)](https://codeclimate.com/github/InseeFrLab/marathon-to-kubernetes/maintainability) [![Coverage Status](https://coveralls.io/repos/github/InseeFrLab/marathon-to-kubernetes/badge.svg?branch=master)](https://coveralls.io/github/InseeFrLab/marathon-to-kubernetes?branch=master)

## Usage

### Using CLI

CLI can be downloaded from the [Github releases page](https://github.com/InseeFrLab/marathon-to-kubernetes/releases)
You can run the converter by using the cli `jar` using Java (version 11+ required) :  
`java -jar marathon-to-kubernetes-cli-version.jar [-n=<name>] [-o=<outputPath>] <inputFile>`

### Usage roadmap

In the future we intend to support the following additional usages :

- Docker run (something like `cat marathon.json | docker run -i image)
- Java library that you can include within your app
- Maybe a simple webapp UI

## Compatibility

| Feature         | Status      | Comment                                                                           |
| --------------- | ----------- | --------------------------------------------------------------------------------- |
| Single app      | Ok          |                                                                                   |
| Multiple apps   | Ok          |                                                                                   |
| Group           | In progress | Need to decide how to properly map Marathon group to kubernetes pods / containers |
| Instances       | Ok          |                                                                                   |
| CPU / Mem       | In progress |                                                                                   |
| PORT_MAPPINGS   | Ok          |                                                                                   |
| Env             | Ok          |                                                                                   |
| HAPROXY_X_VHOST | Ok          |                                                                                   |
| Fetch           | In progress |                                                                                   |
| Healthcheck     |             |                                                                                   |
