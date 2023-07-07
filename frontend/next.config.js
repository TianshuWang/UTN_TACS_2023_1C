/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
}

const isDev = process.env.NODE_ENV === 'development'

module.exports = {
  env: {
    path: isDev ? 'http://localhost:8091':'http://backend:8091',
    timeRange : 2
  }
}
