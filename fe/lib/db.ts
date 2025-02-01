import { Pool, PoolClient, QueryConfig } from "pg"

const poolConfig = {
  user: process.env.POSTGRES_USER,
  password: process.env.POSTGRES_PASSWORD,
  host: process.env.POSTGRES_HOST,
  port: parseInt(process.env.POSTGRES_PORT ?? "5432"),
  database: process.env.POSTGRES_DATABASE,
  ssl: false,
  max: 20,
  idleTimeoutMillis: 30000,
  connectionTimeoutMillis: 2000,
}

const pool: Pool = new Pool(poolConfig)

export const getClient = async (): Promise<PoolClient> => {
  return await pool.connect()
}

export const query = async (text: string | QueryConfig<string[]>, params?: string[]) => {
  return pool.query(text, params)
}

export default pool
