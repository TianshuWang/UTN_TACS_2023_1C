import type { NextApiRequest, NextApiResponse } from 'next';
import { setCookie } from 'cookies-next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  try {
    const response = await axios.post(`${process.env.path}/v1/auth/register`, req.body, {
      headers: {
        "content-type": "application/json",
      },
    });

    setCookie('jwt', response.data.access_token, { req, res, maxAge: 60 * 60 * 24 });
    setCookie('username', response.data.username, { req, res, maxAge: 60 * 60 * 24 });
    res.status(response.status).json(response.data);
  } catch (error: any) {
    if (error.response) {
      res.status(error.response.status).json(error.response.data);
    } else {
      res.status(500).json({ message: 'Internal server error' });
    }
  }
}
