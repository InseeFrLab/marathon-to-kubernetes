# Marathon to Kubernetes

A small application that converts `marathon.json` contracts (used by [Marathon](https://github.com/mesosphere/marathon)) to `deployment.yml` files (used by [Kubernetes](https://kubernetes.io/).  
Support is quite minimal and was primarily tailored for our internal use at [Insee](https://insee.fr/) but you may find it handy for your usage. Feel free to contribute, issues & pull requests are welcome !

[![Build Status](https://travis-ci.org/inseefrlab/marathon-to-kubernetes.svg?branch=master)](https://travis-ci.org/inseefrlab/marathon-to-kubernetes)  
[![Maintainability](https://api.codeclimate.com/v1/badges/18c5a167cf83d3778bd9/maintainability)](https://codeclimate.com/github/InseeFrLab/marathon-to-kubernetes/maintainability)  
[![Coverage Status](https://coveralls.io/repos/github/InseeFrLab/marathon-to-kubernetes/badge.svg?branch=master)](https://coveralls.io/github/InseeFrLab/marathon-to-kubernetes?branch=master)

## Usage

Packaging is still a work in progress.  
At the moment the only way to run the app is to to run it from source (either by writing more tests or by creating a main function) but we intend to support the following usages :

- Java CLI (something like `java -jar jeanmichel.jar marathon.json`)
- Docker run (something like `cat marathon.json | docker run -i image)
- Java library that you can include within your app
- Maybe a simple webapp UI

## Compatibility

| Feature         | Status      | Comment |
| --------------- | ----------- | ------- |
| Single app      | Ok          |         |
| Group           | In progress |         |
| Instances       | Ok          |         |
| CPU / Mem       | In progress |         |
| PORT_MAPPINGS   | Ok          |         |
| Env             | Ok          |         |
| HAPROXY_X_VHOST | Ok          |         |
| Fetch           | In progress |         |
| Healthcheck     |             |         |
