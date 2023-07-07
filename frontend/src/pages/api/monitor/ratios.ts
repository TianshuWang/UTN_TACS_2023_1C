import axios from 'axios';
import type { NextApiRequest, NextApiResponse } from 'next';
import { getCookie } from 'cookies-next';
import pino from "pino";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const logger = pino()
  logger.info("Get marketing report")
  const jwt = getCookie('jwt', { req, res });
  logger.info(`jwt: ${jwt}`)
  try {
    const url = `${process.env.path}/v1/monitor/ratios`;

    const response = await axios.get(url, {
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
