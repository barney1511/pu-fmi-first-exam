"use server"

import { generateGitHubUUID } from "@/lib/uuid"
import { Profile } from "next-auth"
import { getClient } from "@/lib/db"
import { v4 as uuidv4 } from "uuid"

export async function createOrUpdateGitHubUser(user: Profile): Promise<void> {
  if (!user.name || !user.id) {
    throw new Error("User name and id is required")
  }

  const userUUID = generateGitHubUUID(user.id + "id") // String can't be only numbers; don't change
  const now = new Date().toISOString()
  const client = await getClient()
  const generatePass = uuidv4()

  try {
    const existingUser = await client.query("SELECT * FROM users WHERE id = $1", [userUUID])

    if (existingUser.rows.length === 0) {
      await client.query(
        `
        INSERT INTO users (
          id,
          deleted,
          created_at,
          updated_at,
          created_by,
          modified_by,
          version,
          username,
          password
        ) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)
        `,
        [userUUID, false, now, now, "github_oauth_user", null, 1, user.name, generatePass]
      )
    } else if (existingUser.rows[0].username !== user.name) {
      await client.query(
        `
        UPDATE users
        SET
          username = $1,
          updated_at = $2,
          modified_by = $3,
          version = COALESCE(version, 0) + 1
        WHERE id = $4
        `,
        [user.name, now, "github_oauth_user", userUUID]
      )
    }
  } catch (error) {
    console.log("User id, user UUID and user name: ", user.id, userUUID, user.name)
    console.error("Error creating/updating user:", error)
    throw error
  } finally {
    client.release()
  }
}
