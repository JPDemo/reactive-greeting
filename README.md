# reactive-greeting

Demo reactive rsocket spring boot project. 


### Goal

The goal of this project is to allow me to experiment with:

Primarily:
- [x] Gradle
- [x] Reactive framework
- [x] Protobuf
- [x] RSocket

Secondary: 
- [x] Multi project/module code coverage
- [x] Multi project/module docker creation (via Jib)
  - [x] Create docker containers locally
  - [x] Publish docker containers to github container registry
  - [ ] Create release github action that publishes containers
- [x] Configure with Skaffold
- [x] Configure with Istio gateway


Next steps:
- Unable to automate release. 
Getting error 'You have uncommitted files'. 
Requires investigation - may need to change release plugin.  



---

### Solution overview
- __reactive-greeting__ : _Root project_
- __rest-greeting__ : _Standalone reactive rest spring boot client which user services lib_
- __rsocket-client-greeting__ : _Spring boot Rest RSocket client, connects to rsocket-greeting server_
- __rsocket-greeting__ : _Spring boot RSocket server_
- __services__ : _library project_

---

### Resources/Notes

#### ReadME
- https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet#images

#### Reactive
- https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html

#### RSocket
- https://dzone.com/articles/reactive-service-to-service-communication-with-rso-3
- http://www.vinsguru.com/rsocket-integrating-with-spring-boot/

#### RSocket Protobuf
- https://docs.netifi.com/1.5.3/protobuf_rsocket/

#### Jacoco config:
- https://medium.com/swlh/measuring-unit-test-coverage-in-multi-module-android-projects-using-jacoco-part-2-352ef949ecfb
- https://gist.github.com/fraggjkee/50f351f6ef4b75fea6d20d87dbba7f85

#### Unit tests
- https://josdem.io/techtalk/spring/spring_webflux_web_testing/

#### Jib
- https://github.com/GoogleContainerTools/jib/tree/master/examples/multi-module

#### GitHub Container Registry/Packages
 - https://github.com/features/packages

echo $PAT | docker login ghcr.io --username phanatic --password-stdin

docker tag app ghcr.io/phanatic/app:1.0.0

docker push ghcr.io/phanatic/app:1.0.0

#### K8
-https://kubernetes.io/docs/reference/kubectl/cheatsheet/

#### Skaffold
- https://skaffold.dev/
- https://skaffold.dev/docs/references/yaml/

| Commands                        | Description                             |
|---------------------------------|-----------------------------------------|
| skaffold init --XXenableJibInit | Initialise                              |
| skaffold dev                    | Apply K8s and monitor code for changes  |

#### Istio

- https://istio.io/latest/docs/reference/config/networking/virtual-service/
- Dashboad: istioctl dashboard kiali

#### Docker
Container comms issue:
 - https://maximorlov.com/4-reasons-why-your-docker-containers-cant-talk-to-each-other/

 
#### Gradle
 - https://github.com/jaredsburrows/cs-interview-questions
 - Common gradle scripts
   - https://github.com/tomasbjerre/gradle-scripts/tree/master/src/main/resources
   - https://github.com/tomasbjerre/violations-lib 
 
#### Release commands 
 - Plugin: https://github.com/researchgate/gradle-release
 - Works locally, but not in git update actions. Get You have uncommitted files error.  
 - Commands:
    - gradlew release -Prelease.useAutomaticVersion=true
    - gradlew release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=1.0.0 -Prelease.newVersion=1.1.0-SNAPSHOT
