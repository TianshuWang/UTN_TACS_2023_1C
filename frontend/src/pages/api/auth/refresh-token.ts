import type { NextApiRequest, NextApiResponse } from 'next';
import { getCookie, setCookie } from 'cookies-next';
import axios from 'axios';

export default async function handler(req: NextApiRequest, res: NextApiResponse) {
  const jwt = getCookie('jwt', { req, res })
  axios.post(`${process.env.path}/v1/auth/refresh-token`, {}, {
    headers: {
      'Authorization': `Bearer ${jwt}`,
    }
  }).then((response) => {
    setCookie('jwt', response.data.access_token, { req, res, maxAge: 60 * 60 * 24 });
    res.status(response.status).json(response.data)
  }).catch((error) => {
    res.status(error.response.status).json(error.response.data)
  })
}  