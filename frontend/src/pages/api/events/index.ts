import axios from 'axios';
import type { NextApiRequest, NextApiResponse } from 'next';
import { getCookie } from 'cookies-next';
import pino from "pino";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const logger = pino()
  logger.info("Get all events")
  const jwt = getCookie('jwt', { req, res });
  logger.info(`jwt: ${jwt}`)
  try {
    const url = `${process.env.path}/v1/events`;
    const config = {
      headers: {
        'Authorization': `Bearer ${jwt}`,
      }
    }

    let response;

    if (req.method === "GET") {
      response = await axios.get(url, config);
    } else {
      response = await axios.post(url, req.body, config);
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
