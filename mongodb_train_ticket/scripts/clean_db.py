#!/usr/bin/env python3

import pathlib
import configparser

from pymongo import MongoClient


root_path = pathlib.Path("..")
config_path = root_path / "configs" / "scripts.ini"
config = configparser.ConfigParser()
config.read(config_path.resolve())


if __name__ == "__main__":
    client = MongoClient()
    name = config["Init"]["name_db"]
    client.drop_database(client[name])
