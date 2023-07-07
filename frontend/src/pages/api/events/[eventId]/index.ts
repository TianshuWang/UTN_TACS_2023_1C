import type { NextApiRequest, NextApiResponse } from 'next';
import { getCookie } from 'cookies-next';
import axios from 'axios';
import pino from "pino";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
    const logger = pino()
    logger.info(`Get event ${req.query.eventId}`)
    const jwt = getCookie('jwt', { req, res });
    logger.info(`jwt: ${jwt}`)
    const config = {
        headers: {
            'Authorization': `Bearer ${jwt}`,
        },
    };

    try {
        let response;
        if (req.method === 'GET') {
            const url = `${process.env.path}/v1/events/${req.query.eventId}`;
            response = await axios.get(url, config);
        } else {
            const url = `${process.env.path}/v1/events/${req.query.eventId}?status=${req.query.status}`;
            response = await axios.patch(url, {}, config);
        }

        res.status(response.status).json(response.data);
    } catch (error: any) {
        if (error.response) {
            res.status(error.response.status).json(error.response.data);
        } else {
            res.status(500).json({ message: 'Internal server error' });
        }
    }
}
