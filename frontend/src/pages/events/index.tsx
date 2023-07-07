import { useState, useEffect } from "react";
import { Event } from "../../types/app"
import Link from "next/link"
import Header from "../../components/header/header-component"
import Card from 'react-bootstrap/Card';
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { getCookie } from "cookies-next";
import { useRouter } from "next/router";
import Modal from 'react-bootstrap/Modal';
import { FaCalendarAlt, FaListAlt, FaWindowClose, FaCheckCircle } from "react-icons/fa";
import pino from "pino";

export default function Events() {
    const logger = pino()
    const router = useRouter()
    const [events, setEvents] = useState<Event[]>([])
    const [showOpen] = useState(false)
    const [showClose] = useState(false)
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState(false)
    const username = getCookie('username')

    const fetchEvents = async () => {
        try {
            const response = await fetch("/api/events");
            const reply = await response.json();

            if (reply.message) {
                setAlertMessage(reply.message);
                setShowAlert(true);
                return;
            }

            setEvents(reply.events.sort((a: any, b: any) => a.id > b.id ? 1 : -1));
        } catch (error) {
            logger.error(error);
        }
    };

    const handleCloseAlert = () => {
        setShowAlert(false)
        fetchEvents()
    }

    const mapEventStatus = (status: string) => {
        switch (status) {
            case 'VOTE_CLOSED':
                return 'VOTE CLOSED';
            case 'VOTE_PENDING':
                return 'VOTE PENDING';
            default:
                return '';
        }
    };


    const handleChangeEventStatus = async (eventId: string, status: string) => {
        try {
            const response = await fetch(`/api/events/${eventId}?status=${status}`, {
                method: "PATCH",
            });
            const reply = await response.json();

            if (reply.message) {
                setAlertMessage(reply.message);
                setShowAlert(true);
                return;
            }

            fetchEvents();
        } catch (error) {
            logger.error(error);
        }
    };


    const getEventsColumnsForRow = () => {
        return events.map((event) => {
            const isOwner = username === event?.owner_user?.username;
            const isVoteClosed = event.status === "VOTE_CLOSED";
            const isVotePending = event.status === "VOTE_PENDING";
            const showCloseButton = showClose !== (isOwner && !isVoteClosed);
            const showOpenButton = showOpen !== (isOwner && !isVotePending);

            return (
                <Col md="auto" key={event.id}>
                    <Card bg="light" style={{ width: '18rem' }} className="mb-4">
                        <Card.Header>
                            <FaCalendarAlt className="inline mb-1" /> {event.name}
                        </Card.Header>
                        <Card.Body>
                            <Card.Text>Status: {mapEventStatus(event.status)}</Card.Text>
                            <Card.Text>Owner User: {event?.owner_user?.username}</Card.Text>
                        </Card.Body>
                        <Card.Footer>
                            <Link href={"/events/" + event.id}>
                                <Button variant="primary">
                                    <FaListAlt className="inline mb-1" /> Details
                                </Button>
                            </Link>
                            {showCloseButton && (
                                <Button
                                    variant="danger"
                                    className="float-right"
                                    onClick={() => handleChangeEventStatus(event.id, "VOTE_CLOSED")}
                                >
                                    <FaWindowClose className="inline mb-1" /> Close Vote
                                </Button>
                            )}
                            {showOpenButton && (
                                <Button
                                    variant="primary"
                                    className="float-right"
                                    onClick={() => handleChangeEventStatus(event.id, "VOTE_PENDING")}
                                >
                                    <FaCheckCircle className="inline mb-1" /> Open Vote
                                </Button>
                            )}
                        </Card.Footer>
                    </Card>
                </Col>
            );
        });
    };

    useEffect(() => {
        if (!username) {
            router.push("/")
            return
        }
        fetchEvents()
    }, [username, router])

    return (
        <main >
            <Header />
            <Container fluid="lg">
                <Row md={"auto"} >
                    {getEventsColumnsForRow()}
                </Row>
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
        </main >
    );
}
