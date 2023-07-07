import json
import telebot
from telebot import types
import api
import model
import information
import utils
from config import BOT_TOKEN

bot = telebot.TeleBot(BOT_TOKEN)
users = dict()


@bot.message_handler(commands=['start'])
def start(message):
    print(f'User: {message.from_user.id} /start')
    text = f'😊 Hello {message.from_user.first_name} !\n' \
           + 'If this is your first time using the bot, please /sign_up\n' \
           + "If not, please /login\n"
    bot.send_message(message.chat.id, text=text, parse_mode="HTML")


def command_list(from_user, chat_id):
    text = f'{from_user.first_name}!\nPlease select a command:\n' \
           "/all_events\n/event_by_id\n" \
           "/vote_event_option\n/register_to_event\n/change_event_status\n/new_event\n" \
           "/monitoring_report\n"

    try:
        user = users.get(from_user.id)
        if user and user.token:
            text += "🛑 /logout\n"
        bot.send_message(chat_id, text=text, parse_mode="HTML")
    except KeyError:
        bot.send_message(chat_id, text, parse_mode="HTML")


@bot.message_handler(commands=['login'])
def login_handler(message):
    print(f'User: {message.from_user.id} /login')
    sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_USERNAME, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, ask_for_password)


def ask_for_password(message):
    username = message.text
    text = "😶 Please input your password:"
    sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: fetch_login(msg, username))


def fetch_login(message, username):
    password = message.text
    bot.delete_message(message.chat.id, message.id)
    user = model.User()
    result = utils.convert_result(api.authenticate(username, password, user))
    if "Login successfully" not in result:
        bot.send_message(message.chat.id, text=result + ". Please /login again", parse_mode="HTML")
    else:
        users[message.from_user.id] = user
        bot.send_message(message.chat.id, text=result, parse_mode="HTML")
        command_list(message.from_user, message.chat.id)


@bot.message_handler(commands=['logout'])
def logout_handler(message):
    print(f'User: {message.from_user.id} /logout')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            result = api.logout(user)
            if "Logout successfully" in result:
                users.pop(message.from_user.id)
                bot.send_message(message.chat.id, result, parse_mode="HTML")
                start(message)
            else:
                result += ' Please /logout again'
                bot.send_message(message.chat.id, result, parse_mode="HTML")
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


@bot.message_handler(commands=['sign_up'])
def sign_up_handler(message):
    print(f'User: {message.from_user.id} /sign_up')
    sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_USERNAME, parse_mode="Markdown")
    user = model.User()
    bot.register_next_step_handler(sent_msg, lambda msg: ask_for_first_name(msg, user))


def ask_for_first_name(message, user):
    username = message.text
    user.username = username
    text = "😆 Please input you first name:"
    sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: ask_for_last_name(msg, user))


def ask_for_last_name(message, user):
    first_name = message.text
    user.first_name = first_name
    text = "😆 Please input you last name:"
    sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: ask_for_password_sign_up(msg, user))


def ask_for_password_sign_up(message, user):
    last_name = message.text
    user.last_name = last_name
    sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_PASSWORD, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: ask_for_password_confirmation(msg, user))


def ask_for_password_confirmation(message, user):
    password = message.text
    bot.delete_message(message.chat.id, message.id)
    text = "😶 Please repeat you password:"
    sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: fetch_sign_up(msg, user, password))


def fetch_sign_up(message, user, password):
    password_confirmation = message.text
    bot.delete_message(message.chat.id, message.id)
    if password != password_confirmation:
        bot.send_message(message.chat.id, "😢 Passwords do not match. Please /sign_up again", parse_mode="HTML")
    else:
        result = utils.convert_result(api.register(message, password, user))
        if "already exists" in result:
            bot.send_message(message.chat.id, text=result + ". Please /login", parse_mode="HTML")
        else:
            if "Sign up successfully" in result:
                users[message.from_user.id] = user
                bot.send_message(message.chat.id, text=result, parse_mode="HTML")
                command_list(message.from_user, message.chat.id)
            else:
                bot.send_message(message.chat.id, text=result + "\n" + "Please /sign_up again", parse_mode="HTML")


@bot.message_handler(commands=['all_events'])
def all_events_handler(message):
    print(f'User: {message.from_user.id} /all_events')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            result = utils.format_events(api.get_all_events(user))
            bot.send_message(message.chat.id, result, parse_mode="HTML")
            command_list(message.from_user, message.chat.id)
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


@bot.message_handler(commands=['event_by_id'])
def event_by_id_handler(message):
    print(f'User: {message.from_user.id} /event_by_id')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_EVENT_ID, parse_mode="Markdown")
            bot.register_next_step_handler(sent_msg, lambda msg: fetch_event(msg, user))
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


def fetch_event(message, user):
    event_id = message.text
    result = api.get_event_by_id(user, event_id)
    bot.send_message(message.chat.id, utils.format_event(result), parse_mode="HTML")
    command_list(message.from_user, message.chat.id)


@bot.message_handler(commands=['register_to_event'])
def register_to_event_handler(message):
    print(f'User: {message.from_user.id} /register_to_event')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_EVENT_ID, parse_mode="Markdown")
            bot.register_next_step_handler(sent_msg, lambda msg: fetch_register_to_event(msg, user))
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


def fetch_register_to_event(message, user):
    event_id = message.text
    result = api.register_to_event(user, event_id)
    bot.send_message(message.chat.id, utils.format_event(result), parse_mode="HTML")
    command_list(message.from_user, message.chat.id)


@bot.message_handler(commands=['change_event_status'])
def change_event_status_handler(message):
    print(f'User: {message.from_user.id} /change_event_status')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_EVENT_ID, parse_mode="Markdown")
            bot.register_next_step_handler(sent_msg, ask_for_status)
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


def ask_for_status(message):
    event_id = message.text
    text = "Please select the status."
    vote_closed = {"status": "VOTE_CLOSED", "id": event_id}
    vote_pending = {"status": "VOTE_PENDING", "id": event_id}
    button_yes = types.InlineKeyboardButton('❌ VOTE CLOSED', callback_data=json.dumps(vote_closed))
    button_no = types.InlineKeyboardButton('⭕ VOTE PENDING', callback_data=json.dumps(vote_pending))
    keyboard = types.InlineKeyboardMarkup()
    keyboard.add(button_yes)
    keyboard.add(button_no)
    bot.send_message(message.chat.id, text, reply_markup=keyboard)


@bot.callback_query_handler(lambda call: 'status' in call.data)
def fetch_change_event_status(call):
    user = users.get(call.from_user.id)
    if user and user.token:
        status = json.loads(call.data)['status']
        result = api.change_event_status(user, json.loads(call.data)['id'], status)
        bot.send_message(call.message.chat.id, utils.format_event(result), parse_mode="HTML")
        command_list(call.from_user, call.message.chat.id)
    else:
        bot.send_message(call.message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


@bot.message_handler(commands=['vote_event_option'])
def vote_event_option_handler(message):
    print(f'User: {message.from_user.id} /vote_event_option')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_EVENT_ID, parse_mode="Markdown")
            bot.register_next_step_handler(sent_msg, ask_for_option_id)
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


def ask_for_option_id(message):
    event_id = message.text
    sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_OPTION_ID, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, lambda msg: fetch_vote_event_option(msg, event_id))


def fetch_vote_event_option(message, event_id):
    user = users.get(message.from_user.id)
    if user and user.token:
        option_id = message.text
        result = api.vote_event_option(user, event_id, option_id)
        bot.send_message(message.chat.id, utils.format_event(result), parse_mode="HTML")
        command_list(message.from_user, message.chat.id)
    else:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


@bot.message_handler(commands=['monitoring_report'])
def events_marketing_report(message):
    print(f'User: {message.from_user.id} /monitoring_report')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            marketing_report = api.get_events_marketing_report(user)
            options_report = api.get_options_report(user)
            bot.send_message(message.chat.id, utils.format_monitoring_report(marketing_report, options_report),
                             parse_mode="HTML")
            command_list(message.from_user, message.chat.id)
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


@bot.message_handler(commands=['new_event'])
def new_event_handler(message):
    print(f'User: {message.from_user.id} /new_events')
    try:
        user = users.get(message.from_user.id)
        if user and user.token:
            model.Event.event_options = list()
            text = "📝 Please input the event name:"
            sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
            bot.register_next_step_handler(sent_msg, ask_for_event_description)
        else:
            bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")
    except KeyError:
        bot.send_message(message.chat.id, information.Message.LOGIN_FIRST, parse_mode="Markdown")


def ask_for_event_description(message):
    event = model.Event()
    event.name = message.text
    text = "📄 Please input the event description:"
    sent_msg = bot.send_message(message.chat.id, text, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, ask_for_first_date_time, event)


def ask_for_first_date_time(message, event):
    event.description = message.text
    ask_for_date_time(message, event)


def ask_for_date_time(message, event):
    sent_msg = bot.send_message(message.chat.id, information.Message.INPUT_DATE_TIME, parse_mode="Markdown")
    bot.register_next_step_handler(sent_msg, option_handler, event)


def option_handler(message, event):
    date_time = message.text
    if utils.is_valid_date(date_time):
        event.event_options.append({"date_time": date_time})
        ask_for_more_option(message, event)
    else:
        bot.send_message(message.chat.id, "😢 Date time invalid.", parse_mode="Markdown")
        ask_for_date_time(message, event)


def ask_for_more_option(message, event):
    reply = types.ForceReply()
    text = "You need to add more event option❓ ✅ ❌. Please input: " + '✅Y' + ' or  ❌N'
    sent_msg = bot.send_message(message.chat.id, text, reply_markup=reply)
    bot.register_next_step_handler(sent_msg, process_option, event)


def process_option(message, event):
    if 'Y' in message.text or 'y' in message.text:
        ask_for_date_time(message, event)
    elif 'N' in message.text or 'n' in message.text:
        fetch_create_event(message, event)
    else:
        bot.send_message(message.chat.id, "😢 Answer invalid.", parse_mode="Markdown")
        ask_for_more_option(message, event)


def fetch_create_event(message, event):
    print(event.name)
    user = users[message.from_user.id]
    result = api.create_event(user, event)
    bot.send_message(message.chat.id, utils.format_event(result), parse_mode="HTML")
    command_list(message.from_user, message.chat.id)


def main():
    print("Bot starting...")
    bot.infinity_polling()
    print("Bot stopped")


if __name__ == "__main__":
    main()
