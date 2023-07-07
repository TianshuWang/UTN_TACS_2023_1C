import React, { useState, FormEvent, useEffect } from 'react';
import { AuthenticationRequest } from '../types/app'
import { useRouter } from "next/router";
import Link from 'next/link';
import { FaUserAlt, FaLockOpen, FaSignInAlt } from "react-icons/fa";
import pino from "pino";
import { getCookie } from 'cookies-next';

export default function Login(req: any, res: any) {
  const logger = pino()
  const router = useRouter()
  const [errorMessage, setErrorMessage] = useState();
  const [authenticationData, setAuthenticationData] = useState<AuthenticationRequest>({
    username: "",
    password: ""
  })

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    await authenticate()
  }

  const handleChange = (event: any) => {
    setAuthenticationData({ ...authenticationData, [event.target.name]: event.target.value });
  };

  useEffect(() => {
    if (getCookie('username')) {
      router.push("/events")
      return
    }
  }, [router])

  async function authenticate() {
    try {
      const json = JSON.stringify(authenticationData)
      const response = await fetch("/api/auth/authentication", {
        method: "POST",
        body: json,
      });
      const userData = await response.json();
      if (response.status === 200) {
        router.push("/events");
      } else {
        setErrorMessage(userData.message);
      }
    } catch (error) {
      logger.error(error);
    }
  }


  return (
    <div className='flex flex-col justify-center min-h-screen'>
      <main className="flex flex-col items-center justify-center">
        <div className="w-full max-w-xs">
          <form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4" onSubmit={handleSubmit}>
            <div className="mb-4">
              <div className='mb-6'>
                <h1 className="font-bold text-gray-700 text-xl tracking-tight text-center">TACS</h1>
              </div>
              <div className='mb-6'>
                <i className="bi bi-0-square-fill"></i>
                <span className="font-semibold text-gray-700 text-lg tracking-tight">Login</span>
              </div>
            </div>
            <div className="mb-6">
              <label className="block text-gray-700 text-sm font-bold mb-1" htmlFor="username"><FaUserAlt className="inline" />  Username
              </label>

              <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline mb-3"
                type="text" name="username" placeholder="Username" value={authenticationData.username} onChange={handleChange} required />

              <label className="block text-gray-700 text-sm font-bold mb-1" htmlFor="password">
                <FaLockOpen className="inline" />  Password
              </label>
              <input className="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline mb-3"
                type="password" name="password" placeholder="**********" value={authenticationData.password} onChange={handleChange} required />
              {errorMessage && (<p className="error text-red-500 text-xs italic"> {errorMessage} </p>)}
            </div>
            <div className="flex items-center justify-between">
              <button className="bg-blue-700 hover:bg-blue-500 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline" type="submit">
                <FaSignInAlt className="inline mb-1" />  Sign In
              </button>
              <Link className="inline-block align-baseline font-bold text-md text-blue-500 hover:text-blue-800" href="/sign-up">Sign Up?
              </Link>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
};