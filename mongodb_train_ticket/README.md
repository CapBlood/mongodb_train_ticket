## Установка MongoDB
```shell
brew tap mongodb/brew
```

```shell script
brew install mongodb-community@5.0
```

## Запуск MongoDB

Создаем первый набор реплик:
```shell script
mkdir -p ~/mongod_db/n1/ ~/mongod_db/n2/ ~/mongod_db/n3/
```

```shell
mongod --port 27018 --configsvr --dbpath ~/mongod_db/n1/ --replSet rs0
```

```shell
mongod --port 27019 --configsvr --dbpath ~/mongod_db/n2/ --replSet rs0
```

```shell
mongod --port 27020 --configsvr --dbpath ~/mongod_db/n3/ --replSet rs0
```

Запускаем `mongosh --port 27018` и в нём:
```shell
rs.initiate( {
   _id : "rs0",
   members: [
      { _id: 0, host: "127.0.0.1:27018" },
      { _id: 1, host: "127.0.0.1:27019" },
      { _id: 2, host: "127.0.0.1:27020" }
   ]
})
```

Создаем второй набор реплик для шардинга:
```shell
mkdir -p ~/mongod_db_2/n1/ ~/mongod_db_2/n2/ ~/mongod_db_2/n3/
```

```shell
mongod --port 27021 --shardsvr --dbpath ~/mongod_db_2/n1/ --replSet rs1
```

```shell
mongod --port 27022 --shardsvr --dbpath ~/mongod_db_2/n2/ --replSet rs1
```

```shell
mongod --port 27023 --shardsvr --dbpath ~/mongod_db_2/n3/ --replSet rs1
```

Запускаем `mongosh --port 27021` и в нём:
```shell
rs.initiate( {
   _id : "rs1",
   members: [
      { _id: 0, host: "127.0.0.1:27021" },
      { _id: 1, host: "127.0.0.1:27022" },
      { _id: 2, host: "127.0.0.1:27023" }
   ]
})
```

Запускаем `mongos` с указанием серверов конфигурации (первый набор реплик):
```shell
mongos --configdb rs0/127.0.0.1:27018,127.0.0.1:27019,127.0.0.1:27020 --port 27017
```

Подключаемся к нему через `mongosh`:
```shell
mongosh --port 27017
```

Добавляем реплики сегментов (второй набор реплик):
```shell
sh.addShard("rs1/127.0.0.1:27021,127.0.0.1:27022,127.0.0.1:27023")
```

Активируем шардинг:
```shell
sh.enableSharding("train_test")
```

Создаём индексы (в данном случае будем использовать хэширование):
```shell
db.train.createIndex({ _id: "hashed" })
```

```shell
db.station.createIndex({ _id: "hashed" })
```

```shell
db.ticket.createIndex({ _id: "hashed" })
```

```shell
db.route.createIndex({ _id: "hashed" })
```

Задаем ключи шардинга:
```shell
sh.shardCollection( "train_test.train", { _id : "hashed" } )
```
```shell
sh.shardCollection( "train_test.route", { _id : "hashed" } )
```
```shell
sh.shardCollection( "train_test.ticket", { _id : "hashed" } )
```
```shell
sh.shardCollection( "train_test.station", { _id : "hashed" } )
```

Теперь подключаться необходимо как обычно к MongoDB, но подключение уже будет через mongos.

## Скрипты
- init_db.py - заполнение MongoDB данными;
- clean_db.py - удаление БД из MongoDB.

### Пример очистки и заполнения
```shell script
./clean_db.py & ./init_db.py 
```
