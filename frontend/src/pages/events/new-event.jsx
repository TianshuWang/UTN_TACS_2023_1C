import Header from "../../components/header/header-component"
import Button from 'react-bootstrap/Button';
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import { getCookie } from "cookies-next";
import Form from 'react-bootstrap/Form';
import { useEffect, useState } from "react";
import { useRouter } from "next/router"
import { FaCalendarAlt, FaListAlt, FaClock, FaPlus, FaMinus, FaFileUpload } from "react-icons/fa";
import Modal from 'react-bootstrap/Modal';
import pino from "pino";

export default function NewEvent() {
    const logger = pino()
    const router = useRouter()
    const [showAlert, setShowAlert] = useState(false);
    const [alertMessage, setAlertMessage] = useState(false)

    const [newEvent, setNewEvent] = useState({
        name: "",
        description: "",
        event_options: []
    })
    const [fields, setFields] = useState([{
        id: 1,
        date_time: "",
    }])

    const handleChangeEvent = (event) => {
        setNewEvent({ ...newEvent, [event.target.name]: event.target.value });
    };

    const handleChangeInput = (i, e) => {
        const values = [...fields]
        values[i][e.target.name] = e.target.value
        setFields(values)
    }

    const handleAdd = (id) => {
        setFields([...fields, { id: id + 2, date_time: '' }])
    }

    const handleSubtract = (i) => {
        const values = [...fields]
        values.splice(i, 1)
        setFields([...values])
    }

    async function createNewEvent() {
        try {
            const response = await fetch("/api/events", {
                method: "POST",
                body: JSON.stringify(newEvent),
                headers: {
                    'Content-Type': 'application/json',
                },
            });
            const userData = await response.json()
            if (response.status == 201) {
                router.push("/events")
            }
            else {
                setAlertMessage(userData.message)
                setShowAlert(true)
                return
            }
        } catch (error) {
            logger.error(error);
        }
    };

    const handleCloseAlert = () => {
        setShowAlert(false)
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
      
        const eventOptions = fields
          .filter((field) => field.date_time !== "")
          .map((field) => ({ date_time: field.date_time }));
      
        newEvent.event_options = eventOptions;
      
        await createNewEvent();
      };
      

    useEffect(() => {
        if (!getCookie('username')) {
            router.push("/")
        }
    }, [router])

    return (
        <main>
            <Header />
            <Container>
                <Row>
                    <Form onSubmit={handleSubmit}>
                        <Form.Group className="mb-4">
                            <Row className="mt-3">
                                <Col md>
                                    <Form.Label><FaCalendarAlt className="inline mb-1" />  Event Name</Form.Label>
                                    <Form.Control type="text" name="name" placeholder="Event Name" onChange={handleChangeEvent} required />
                                </Col>
                                <Col md>
                                    <Form.Label><FaListAlt className="inline mb-1" />  Description</Form.Label>
                                    <Form.Control type="text" name="description" placeholder="Description" onChange={handleChangeEvent} />
                                </Col>
                            </Row>
                            {fields.map((field, i) => (
                                <Row key={i}>
                                    <Col className="mt-3">
                                        <Form.Label><FaClock className="inline mb-1" />  Option Date Time</Form.Label>
                                        <Form.Control type="datetime-local" name="date_time" placeholder="Date" onChange={e => handleChangeInput(i, e)} required/>
                                    </Col>
                                    <Col className="mt-5">
                                        <Button variant="primary" className="mr-3" onClick={() => handleAdd(i)}>
                                            <FaPlus className="inline mb-1" />  Option
                                        </Button>
                                        <Button variant="danger" className="mr-3" onClick={() => handleSubtract(i)} disabled={field.id === 1}>
                                            <FaMinus className="inline mb-1" />  Option
                                        </Button>
                                    </Col>
                                </Row>
                            ))}
                        </Form.Group>

                        <Button variant="primary" type="submit" >
                            <FaFileUpload className="inline mb-1" />  Submit
                        </Button>
                    </Form>
                </Row>
            </Container >
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
