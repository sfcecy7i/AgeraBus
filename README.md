# AgeraBus
An event bus implements with Agera
###使用方法:
```
Repository<Event> mEventRepo = AgeraBus.repository(MyEvent.class);
```
```
mEventRepo.addUpdatable(()-> {
        Event event = mEventRepo.get();
        if (event instanceof MyEvent) {
            Toast.makeText(this, "我收到消息了", Toast.LENGTH_SHORT).show();
        }
});
```
```
mEventRepo.removeUpdatable();
```
```
AgeraBus.post(new MyEvent());
```
