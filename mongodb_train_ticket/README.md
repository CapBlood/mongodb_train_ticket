# Оглавление

## Запуск MongoDB
```shell script
docker run --rm --name my-mongo -it -p 27017:27017 mongo:latest
```

## Скрипты
- init_db.py - заполнение MongoDB данными;
- clean_db.py - удаление БД из MongoDB.

### Пример очистки и заполнения
```shell script
./clean_db.py & ./init_db.py 
```