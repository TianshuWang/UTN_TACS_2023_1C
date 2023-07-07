export interface AuthenticationRequest {
    username: string
    password: string
}

export interface RegisterRequest {
    first_name: string,
    last_name: string,
    username: string,
    password: string,
    password_confirmation: string
}

export interface EventOption {
    id: string,
    date_time: Date,
    vote_users: User[],
    vote_quantity: number
    update_time: string,
}

export interface EventOptionRequest {
    date_time: string,
}

export interface EventRequest {
    name: string,
    description: string,
    event_options: EventOptionRequest[],
}

export interface User {
    id: string,
    first_name: string,
    last_name: string,
    username:string
}

export interface Event {
    id: string,
    name: string,
    description: string,
    status: string,
    event_options: EventOption[],
    owner_user: User ,
    registered_users: User[],
    create_date: string
}

export interface Report {
    events_count: number,
    options_count: number,
}

export interface EventOptionReport {
    date_time: string,
    vote_quantity: number,
    event_name: string
}