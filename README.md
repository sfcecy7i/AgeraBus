# AgeraBus
An event bus implements with Agera
###使用方法:
```
Repository<Event> mEventRepo = AgeraBus.repository(MyEvent.class);
```
```
Updatable updatable = ()-> {
                              Event event = mEventRepo.get();
                              if (event instanceof MyEvent) {
                                  Toast.makeText(this, "我收到消息了", Toast.LENGTH_SHORT).show();
                              }
                      };
```
```
mEventRepo.addUpdatable(updatable);
```
```
mEventRepo.removeUpdatable(updatable);
```
```
AgeraBus.post(new MyEvent());
```
