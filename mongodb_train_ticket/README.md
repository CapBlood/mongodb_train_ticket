# Оглавление

## Установка MongoDB
```shell
brew install mongodb-community@5.0
```

## Запуск MongoDB

Создаем первый набор реплик:
```shell
mkdir -p ~/mongod_db/n1/ ~/mongod_db/n2/ ~/mongod_db/n3/
```

```shell
mongod --port 27017 --dbpath ~/mongod_db/n1/ --replSet rs0
```

```shell
mongod --port 27018 --dbpath ~/mongod_db/n2/ --replSet rs0
```

```shell
mongod --port 27019 --dbpath ~/mongod_db/n3/ --replSet rs0
```

Запускаем `mongo` и в нём:
```shell
rs.initiate( {
   _id : "rs0",
   members: [
      { _id: 0, host: "127.0.0.1:27017" },
      { _id: 1, host: "127.0.0.1:27018" },
      { _id: 2, host: "127.0.0.1:27019" }
   ]
})
```

## Скрипты
- init_db.py - заполнение MongoDB данными;
- clean_db.py - удаление БД из MongoDB.

### Пример очистки и заполнения
```shell script
./clean_db.py & ./init_db.py 
```

### Шардинг
После заполнения данными активируем шардинг (в `mongosh`):

[comment]: <> (```shell)

[comment]: <> (sh.enableSharding&#40;"train_test"&#41;)

[comment]: <> (```)

[comment]: <> (```shell)

[comment]: <> (sh.addShard&#40;"rs0/127.0.0.1:27017,127.0.0.1:27018,127.0.0.1:27019"&#41;)

[comment]: <> (```)

[comment]: <> (```shell)

[comment]: <> (sh.shardCollection&#40; "database.collection", { "_id" : "hashed" } &#41;)

[comment]: <> (```)