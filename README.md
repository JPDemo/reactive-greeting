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
- [ ] Multi project/module package created (via GitHub actions)
- [ ] Multi project/module docker creation 
- [ ] Skaffold


Next steps:
- Create packages via Github actions

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
