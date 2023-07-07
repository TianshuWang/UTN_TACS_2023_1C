import type { NextApiRequest, NextApiResponse } from 'next';
import { getCookie } from 'cookies-next';
import axios from 'axios';
import pino from "pino";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const logger = pino()
  logger.info(`Register user to event ${req.query.eventId}`)
  const jwt = getCookie('jwt', { req, res });
  logger.info(`jwt: ${jwt}`)
  try {
    const url = `${process.env.path}/v1/events/${req.query.eventId}/user`;

    const response = await axios.patch(url, {}, {
      headers: {
        'Authorization': `Bearer ${jwt}`,
      },
    });

    res.status(response.status).json(response.data);
  } catch (error: any) {
    if (error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json({ message: 'Internal server error' });
    }
  }
}
