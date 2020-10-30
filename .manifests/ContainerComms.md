## Container communication problem

RSocket-client-greeting (client) cant access rsocket-greeting (the server) container out of the box because not on same network.

Possible options:
1. Move containers into same pod
2. Configuration network
   - Resources on topic
        - https://dzone.com/articles/how-to-understand-and-setup-kubernetes-networking
        - https://www.mirantis.com/blog/multi-container-pods-and-container-communication-in-kubernetes/
        - https://maximorlov.com/4-reasons-why-your-docker-containers-cant-talk-to-each-other/
        - https://medium.com/google-cloud/understanding-kubernetes-networking-services-f0cb48e4cc82

For now I have option 1 working with manifests under 'sharedpods' folder. 

In the future I hope to enhance the manifests under 'individualpods' and add network configuration 