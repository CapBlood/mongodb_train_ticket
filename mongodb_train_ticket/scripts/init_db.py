#!/usr/bin/env python3

import json
import pathlib
import random
import configparser
import datetime

from pymongo import MongoClient


FORMAT_DATE = '%d/%m/%y'

# TODO:
# Написать try-except для config.

root_path = pathlib.Path("..")
config_path = root_path / "configs" / "scripts.ini"
config = configparser.ConfigParser()
config.read(config_path.resolve())

client = MongoClient(replicaset='rs0')
name = config["Init"]["name_db"]
db = client[name]

# TODO:
# Написать побольше городов в датасете stations.json.


def init_collection_station(path):
    """Инициализация коллекции станций в MongoDB

    Parameters
    ----------
    path : str
        Путь к json-файлу с данными о станциях.
    """

    with open(path) as file:
        stations = json.load(file)
    station_collection = db["station"]
    station_collection.insert_many(stations)


def generate_routes(path_stations, l_route=4, n_routes=5):
    """Генерация случайных путей по городам.

    Parameters
    ----------
    path_stations : str
        Путь к json-файлу с данными о станциях.
    l_route : int
        Длины генерируемых путей.
    n_routes : int
        Кол-во генерируемых путей.

    Returns
    -------
    list
        Список словарей с двумя полями
        (number - номер пути, route - список станций-городов).

    Notes
    -----
    Пример словаря в возвращаемом списке:
    {'number': 0,
     'route': ['Санкт-Петербург', 'Владивосток', 'Бийск', 'Москва']}
    """

    # TODO:
    # Надо ли генерировать номер пути, если MongoDB автоматом делает
    # суррогатный ключ?
    #
    # Пример:
    # {'_id': ObjectId('61574a637c62bef6f05bd613'),
    #  'number': 0,
    #  'route': ['Санкт-Петербург', 'Владивосток', 'Бийск', 'Москва']}

    with open(path_stations) as file:
        stations = json.load(file)

    list_station = [station["name"] for station in stations]
    routes = []
    for i in range(n_routes):
        route = random.sample(list_station, l_route)
        routes.append({
            "route": route
        })

    return routes


def init_collection_routes(routes):
    """Инициализация коллекции путей в MongoDB

    Parameters
    ----------
    routes : list
        Список словарей с полями number (номер пути) и route (список строк городов-станций).

    Notes
    -----
    Пример аргумента routes:
    [{"name": "1", "route": ["Москва", "Новгород"]}]
    """

    route_collection = db["route"]
    route_collection.insert_many(routes)


def generate_trains(routes,
                    end_date,
                    start_date=datetime.datetime.now()
                    .date().strftime(FORMAT_DATE),
                    n_trains=10):
    """Генерация поездов.

    Parameters
    ----------
    routes : list
        Список словарей с двумя полями
        (_id - идентификатор пути, route - список станций-городов).
    start_date : str
        Начальная дата для генерации дат для поездов.
    end_date : str
        Конечная дата для генерации дат для поездов.
    n_trains : int
        Количество генерируемых поездов.

    Returns
    -------
    list
        Список словарей с полями date (дата отправки поезда) и
        route (его маршрут).

    Notes
    -----
    Пример аргумента begin_date и end_date (день/месяц/год):
    '23/02/2012'

    See Also
    --------
    generate_routes
    """

    # TODO:
    # У поезда должен быть идентификатор?

    trains = []
    for i in range(n_trains):
        s_date = datetime.datetime.strptime(start_date, FORMAT_DATE)
        e_date = datetime.datetime.strptime(end_date, FORMAT_DATE)

        time_between_dates = e_date - s_date
        days_between_dates = time_between_dates.days
        random_number_of_days = random.randrange(days_between_dates)
        random_date = s_date + datetime.timedelta(days=random_number_of_days)

        trains.append({
            "date": random_date.strftime(FORMAT_DATE),
            "route": random.choice(routes)["_id"]
        })

    return trains


def init_collection_train(trains):
    # TODO:
    # Написать в Notes пример trains.

    """Инициализация коллекции поездов в MongoDB

    Parameters
    ----------
    trains : list
        Список словарей с полями date (дата отправки) и route (путь поезда).

    """

    train_collection = db["train"]
    train_collection.insert_many(trains)


def generate_tickets(trains,
                     n_tickets=1000000):
    """Генерация билетов.

    Parameters
    ----------
    trains : list
        Список словарей с полями _id (номер поезда), date (дата отбытия), route (id маршрута).
    n_tickets : int
        Количество генерируемых билетов.

    Returns
    -------
    list
        Список словарей с полями bought (куплен билет или свободен) и train (номер поезда).
    """

    tickets = []
    for i in range(n_tickets):
        tickets.append({
            "bought": random.choice([True, False]),
            "train": random.choice(trains)["_id"]
        })

    return tickets


def init_collection_ticket(tickets):
    """Инициализация коллекции билетов в MongoDB

    Parameters
    ----------
    tickets : list
        Список словарей с полями bought (куплен билет или свободен) и train (номер поезда).

    """

    ticket_collection = db["ticket"]
    ticket_collection.insert_many(tickets)


def create_db():
    stations_path = root_path / config["Init"]["path_dataset_stations"]
    init_collection_station(stations_path.resolve())

    if config["Init"].getboolean("random_generate"):
        routes = generate_routes(stations_path.resolve())
        init_collection_routes(routes)
        l_routes = list(db["route"].find())
        trains = generate_trains(l_routes, "20/03/25")
        init_collection_train(trains)
        l_trains = list(db["train"].find())
        tickets = generate_tickets(l_trains)
        init_collection_ticket(tickets)
    else:
        route_path = config["Init"]["path_dataset_routes"]
        with open(root_path / route_path) as file:
            routes = json.load(file)
        init_collection_routes(routes)
        trains_path = config["Init"]["path_dataset_trains"]
        with open(root_path / trains_path) as file:
            trains = json.load(file)
        init_collection_train(trains)
        tickets_path = config["Init"]["path_dataset_tickets"]
        with open(root_path / tickets_path) as file:
            tickets = json.load(file)
        init_collection_ticket(tickets)


if __name__ == "__main__":
    create_db()
