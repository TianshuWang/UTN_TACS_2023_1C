import requests
from telebot import types
import model
from config import SCHEME_DOMAIN


def authenticate(username: str, password: str, user: model.User) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/auth/authentication'
    body = {"username": username, "password": password}

    try:
        response = requests.post(url, json=body)
        json = response.json()

        if response.status_code == 200:
            user.username = json.get('username')
            user.first_name = json.get('first_name')
            user.last_name = json.get('last_name')
            user.token = json.get('access_token')

            result = f'🎉 Login successfully with username: {user.username}'
        else:
            result = '😢 ' + json.get('message')
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def logout(user: model.User) -> str:
    url = f'{SCHEME_DOMAIN}/v1/auth/logout'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.post(url, headers=headers)

        if response.status_code == 200:
            result = "🎉 Logout successfully."
        else:
            result = "😢  Logout failed."
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def register(message: types.Message, password: str, user: model.User) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/auth/register'
    body = {
        "first_name": user.first_name,
        "last_name": user.last_name,
        "username": user.username,
        "password": password,
        "password_confirmation": password
    }

    try:
        response = requests.post(url, json=body)
        json = response.json()

        if response.status_code == 200:
            user.token = json['access_token']

        result = f'🎉 Sign up successfully with username: {user.username}' \
            if response.status_code == 200 else '😢 ' + json['message']
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def get_all_events(user: model.User) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.get(url, headers=headers)

        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def get_event_by_id(user: model.User, event_id: str) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events/{event_id}'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.get(url, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def register_to_event(user: model.User, event_id: str) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events/{event_id}/user'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.patch(url, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def vote_event_option(user: model.User, event_id: str, option_id: str) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events/{event_id}/options/{option_id}/vote'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.patch(url, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def change_event_status(user: model.User, event_id: str, status: str) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events/{event_id}'
    params = {"status": status}
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.patch(url, params=params, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def create_event(user: model.User, new_event) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/events'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        body = {
            "name": new_event.name,
            "description": new_event.description,
            "event_options": new_event.event_options
        }
        response = requests.post(url, json=body, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def get_events_marketing_report(user: model.User) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/monitor/ratios'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.get(url, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def get_options_report(user: model.User) -> dict:
    url = f'{SCHEME_DOMAIN}/v1/monitor/options'
    headers = {"Authorization": f'Bearer {user.token}'}

    try:
        response = requests.get(url, headers=headers)
        result = get_result(response)
    except requests.exceptions.RequestException as e:
        result = "Connection error"

    return result


def get_result(response):
    if response.status_code == 200 or response.status_code == 201:
        result = response.json()
    else:
        result = f'😢 {response.json()["message"]}'
    return result
