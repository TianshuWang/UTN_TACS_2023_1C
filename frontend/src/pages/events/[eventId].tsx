import { useState, useEffect } from "react";
import { useRouter } from "next/router";
import { Event } from "../../types/app"
import Header from "../../components/header/header-component"
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import { getCookie } from "cookies-next";
import moment from 'moment'
import Modal from 'react-bootstrap/Modal';
import { FaCalendarAlt, FaUserAlt, FaCalendarCheck, FaUserPlus } from "react-icons/fa";
import { AiFillLike } from "react-icons/ai";
import pino from "pino";

export default function EventById() {
    const logger = pino()
    const router = useRouter()
    const eventId = router.query.eventId
    const [event, setEvent] = useState<Event>()
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState(false)
    const username = getCookie('username')

    const fetchEvent = async () => {
        try {
            const response = await fetch(`/api/events/${eventId}`);
            const reply = await response.json();

            if (reply.message) {
                setAlertMessage(reply.message);
                setShowAlert(true);
                return;
            }

            setEvent(reply);
        } catch (error) {
            logger.error(error);
        }
    };


    const handleCloseAlert = () => {
        setShowAlert(false)
        fetchEvent()
    }

    const handleVoteOption = async (optionId: string) => {
        try {
            const response = await fetch(`/api/events/${router.query.eventId}/options/${optionId}/vote`);
            const reply = await response.json();

            if (reply.message) {
                setAlertMessage(reply.message);
                setShowAlert(true);
                return;
            }

            fetchEvent();
        } catch (error) {
            logger.error(error);
        }
    };


    const handleRegisterUser = async () => {
        try {
            const response = await fetch(`/api/events/${router.query.eventId}/user`);
            const reply = await response.json();

            if (reply.message) {
                setAlertMessage(reply.message);
                setShowAlert(true);
                return;
            }

            fetchEvent();
        } catch (error) {
            logger.error(error);
        }
    };


    const mapEventStatus = (status: string | undefined) => {
        switch (status) {
            case 'VOTE_CLOSED':
                return 'VOTE CLOSED';
            case 'VOTE_PENDING':
                return 'VOTE PENDING';
            default:
                return '';
        }
    };


    const getRegisteredUsersColumnsForRow = () => {
        if (!event || !event.registered_users) {
            return null;
        }

        return event.registered_users.map((user) => (
            <Col key={user.username}>
                <Card bg="light" style={{ width: '15rem' }} className="mb-1">
                    <Card.Body>
                        <Card.Text>
                            <FaUserAlt className="inline mb-1" /> Username: {user.username}
                        </Card.Text>
                    </Card.Body>
                </Card>
            </Col>
        ));
    };


    const getOptionsColumnsForRow = () => {
        if (!event || !event.event_options) {
            return null;
        }
        return event.event_options.map((option) => (
            <Col sm={"auto"} key={option.id}>
                <Card bg="light" key={option.id} style={{ width: '15rem' }} className="mb-4">
                    <Card.Header><FaCalendarCheck className="inline mb-1" />  Option</Card.Header>
                    <Card.Body>
                        <Card.Text>
                            Date: {moment(option.date_time).format("YYYY/MM/DD")}
                        </Card.Text>
                        <Card.Text>
                            Time: {moment(option.date_time).format("kk:mm")}
                        </Card.Text>
                        <Card.Text>
                            Vote Quantity: {option.vote_quantity}
                        </Card.Text>
                    </Card.Body>
                    <Card.Footer>
                        <Button disabled={("VOTE_CLOSED" === event.status)} variant="primary" onClick={() => handleVoteOption(option.id)}><AiFillLike className="inline mb-1" />  Vote</Button>                        
                        <Button disabled={event.registered_users.map(u => u.username).includes(username as string)} variant="primary" className="float-right" onClick={handleRegisterUser}><FaUserPlus className="inline" />  Register</Button>
                    </Card.Footer>
                </Card>
            </Col>
        ));
    }

    useEffect(() => {
        if (!getCookie('username')) {
            router.push("/")
            return
        }
        if (!eventId) {
            return
        }
        fetchEvent()
    }, [eventId, router])


    return (
        <main>
            <Header />
            <Container>
                <div>
                    <span >
                        <h5 className="mb-2 text-2xl font-bold tracking-tight text-gray-900 dark:text-white">
                            <FaCalendarAlt className="inline mb-1" />  {event?.name}
                        </h5>
                    </span>

                    <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">Description: {event?.description}</p>
                    <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">Status: {mapEventStatus(event?.status)}</p>
                </div>
                <div className="mb-3">
                    <p className="mb-2 font-normal text-gray-700 dark:text-gray-400">Registered Users: </p>
                    <Row md={"auto"}>
                        {getRegisteredUsersColumnsForRow()}
                    </Row>
                </div>
                <div className="mb-3">
                    <p className="mb-3 font-normal text-gray-700 dark:text-gray-400">Options: </p>
                    <Row md={"auto"}>
                        {getOptionsColumnsForRow()}
                    </Row>
                </div>
            </Container>
            <Modal show={showAlert}>
                <Modal.Header closeButton>
                    <Modal.Title>Error!</Modal.Title>
                </Modal.Header>
                <Modal.Body>{alertMessage}</Modal.Body>
                <Modal.Footer>
                    <Button variant="danger" onClick={handleCloseAlert}>
                        Close
                    </Button>
                </Modal.Footer>
            </Modal>
        </main>
    );
}