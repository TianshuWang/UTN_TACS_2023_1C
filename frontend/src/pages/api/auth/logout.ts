import type { NextApiRequest, NextApiResponse } from 'next';
import axios from 'axios';
import pino from "pino";

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const logger = pino()
  logger.info("Log out")
  try {
    const response = await axios.post(`${process.env.path}/v1/auth/logout`);

    res.status(response.status).json(response.data);
  } catch (error: any) {
    if (error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json({ message: 'Internal server error' });
    }
  }
}
