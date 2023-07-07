from dataclasses import dataclass
from typing import List


@dataclass
class EventOption:
    id: str
    date_time: str
    vote_quantity: str

    def __init__(self, id: str = '', date_time: str = '', vote_quantity: str = ''):
        self.id = id
        self.date_time = date_time
        self.vote_quantity = vote_quantity


@dataclass
class User:
    username: str
    first_name: str
    last_name: str
    token: str

    def __init__(self, username: str = '', first_name: str = '', last_name: str = '', token: str = ''):
        self.username = username
        self.first_name = first_name
        self.last_name = last_name
        self.token = token


@dataclass
class Event:
    id: str
    name: str
    description: str
    status: str
    owner_user: User
    event_options: List[EventOption]
    registered_users: List[User]

    def __init__(self, id: str = '', name: str = '', description: str = '',
                 status: str = '', owner_user: User = None,
                 event_options: List[EventOption] = None,
                 registered_users: List[User] = None):
        self.id = id
        self.name = name
        self.description = description
        self.status = status
        self.owner_user = owner_user if owner_user is not None else User()
        self.event_options = event_options if event_options is not None else []
        self.registered_users = registered_users if registered_users is not None else []
