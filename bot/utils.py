import json
from datetime import datetime
import model


def convert_result(result):
    if isinstance(result, dict):
        return json.dumps(result, indent=1)
    return result


def is_valid_date(date):
    if date:
        try:
            datetime.fromisoformat(date)
            return True
        except ValueError:
            pass
    return False


def format_event(event_dict: dict):
    if isinstance(event_dict, dict) and event_dict:
        event = get_event(model.Event(), event_dict)
        username = event.owner_user['username'] if event.owner_user else ''

        registered_users = '\n'.join([f'      😁 {u["username"]}' for u in event.registered_users])

        options = '\n'.join([f'      📅 Option id: {op["id"]} | Date time: {op["date_time"]} '
                             f'| Vote counts: {op["vote_quantity"]}' for op in event.event_options])

        return f'🆔 Event id: {event.id}\n' \
               f'Event name: {event.name}\n' \
               f'Event description: {event.description}\n' \
               f'Event status: {event.status}\n' \
               f'Owner user: 😎 {username}\n' \
               f'Registered users:\n' \
               f'{registered_users}\n' \
               f'Event options:\n' \
               f'{options}'

    return event_dict


def format_events(events_dict: dict):
    if isinstance(events_dict, dict) and len(events_dict.get('events', [])) == 0:
        return "🙅 There's no events."
    elif isinstance(events_dict, dict):
        events = '\n'.join([format_event(e) for e in events_dict['events']])
        return f'Events:\n{events}'
    else:
        return events_dict


def format_monitoring_report(marketing_report: dict, options_report: dict):
    result = 'Monitoring report:\n'
    if isinstance(marketing_report, dict):
        events_count = marketing_report.get('events_count', 0)
        options_count = marketing_report.get('options_count', 0)
        result += f'New events created count: {events_count}\n' \
                  + f'Options voted count: {options_count}\n'
        if options_count > 0 and isinstance(options_report, dict):
            result += "Options voted:\n"
            options = '\n'.join([f"       📅  Date time: {op['date_time']} | Vote counts: {op['vote_quantity']}"
                                 for op in options_report.get('options_report', [])])
            result += options
    else:
        return marketing_report

    return result


def get_event(event: model.Event, my_dict: dict):
    for key, value in my_dict.items():
        setattr(event, key, value)
    return event
