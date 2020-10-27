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
  - [ ] Create local docker images and test with K8
  - [ ] Create images with github actions
- [ ] Configure with Skaffold


Next steps:
- Create local docker images and test with K8


---

### Solution overview
- __reactive-greeting__ : _Root project_
- __rest-greeting__ : _Standalone reactive rest spring boot client which user services lib_
- __rsocket-client-greeting__ : _Spring boot Rest RSocket client, connects to rsocket-greeting server_
- __rsocket-greeting__ : _Spring boot RSocket server_
- __services__ : _library project_

---

###Resources

Reactive
- https://docs.spring.io/spring-framework/docs/current/spring-framework-reference/web-reactive.html

RSocket
- https://dzone.com/articles/reactive-service-to-service-communication-with-rso-3
- http://www.vinsguru.com/rsocket-integrating-with-spring-boot/

RSocket Protobuf
- https://docs.netifi.com/1.5.3/protobuf_rsocket/

Jacoco config:
- https://medium.com/swlh/measuring-unit-test-coverage-in-multi-module-android-projects-using-jacoco-part-2-352ef949ecfb
- https://gist.github.com/fraggjkee/50f351f6ef4b75fea6d20d87dbba7f85

Unit tests
- https://josdem.io/techtalk/spring/spring_webflux_web_testing/

Jib
- https://github.com/GoogleContainerTools/jib/tree/master/examples/multi-module

GitHub
 - https://github.com/features/packages

echo $PAT | docker login ghcr.io --username phanatic --password-stdin

docker tag app ghcr.io/phanatic/app:1.0.0

docker push ghcr.io/phanatic/app:1.0.0
