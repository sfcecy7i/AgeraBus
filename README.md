# AgeraBus
An event bus implements with Agera
### Usage:
Add dependency using maven:
```
<dependency>
  <groupId>com.cmos</groupId>
  <artifactId>agerabus</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>
```
#### Or
add dependency using gradle:
```
compile 'com.cmos:agerabus:1.0.1'
```
#### Sample
```
Repository<Event> mEventRepo = AgeraBus.repository(MyEvent.class);
```
```
Updatable updatable = ()-> { // receive the event
                              Event event = mEventRepo.get();
                              if (event instanceof MyEvent) {
                                  Toast.makeText(this, "我收到消息了", Toast.LENGTH_SHORT).show();
                              }
                      };
```
```
mEventRepo.addUpdatable(updatable); // register
```
```
mEventRepo.removeUpdatable(updatable); // unregister
```
```
AgeraBus.post(new MyEvent()); // post an event
```
