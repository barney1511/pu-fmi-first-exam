"use server"

import {Role} from "@/components/sidebar-client";

export async function getChannels(username: string) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels?page=0&size=20&sortBy=createdAt&sortDirection=desc&username=${username}`
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const data = await response.json()
    return data.content
  } catch (error) {
    console.error("Error fetching channels:", error)
    throw error
  }
}

export async function searchUsers(searchTerm: string) {
  if (!searchTerm.trim()) {
    return { content: [] }
  }

  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users?page=0&size=20&sortBy=createdAt&sortDirection=desc&name=${searchTerm}`
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const data = await response.json()
    return data
  } catch (error) {
    console.error("Error searching users:", error)
    throw error
  }
}

export async function addChannelMember(channelId: string, username: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels/${channelId}/members`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      body: JSON.stringify({ username: username }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error("Error adding channel member:", error)
    throw error
  }
}

export async function getChannelMembers(channelId: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels/${channelId}`)

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const data = await response.json()
    return data.members
  } catch (error) {
    console.error("Error fetching channel members:", error)
    throw error
  }
}

export async function createChannel(name: string, userId: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      body: JSON.stringify({ name: name, ownerId: userId }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error("Error creating channel:", error)
    throw error
  }
}

export async function updateChannelName(channelId: string, name: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      body: JSON.stringify({ id: channelId, name: name }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return null
  } catch (error) {
    console.error("Error updating channel name:", error)
    throw error
  }
}

export async function changeRole(channelId: string, username: string, role: Role) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels/${channelId}/members/${username}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
          accept: "*/*",
        },
        body: JSON.stringify({ role: role }),
      }
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return null
  } catch (error) {
    console.error("Error changing role:", error)
    throw error
  }
}

export async function removeChannelMember(channelId: string, username: string) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels/${channelId}/members/${username}`,
      {
        method: "DELETE",
      }
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return null
  } catch (error) {
    console.error("Error removing channel member:", error)
    throw error
  }
}

export async function getFriends(username: string) {
  try {
    const response = await fetch(
      `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/friendships?page=0&size=20&sortBy=createdAt&sortDirection=desc&username=${username}`
    )

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    const data = await response.json()
    return data.content
  } catch (error) {
    console.error("Error fetching friends:", error)
    throw error
  }
}

export async function addFriend(friendId: string, userId: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/friendships`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      body: JSON.stringify({ userId: userId, friendId: friendId }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error("Error adding friend:", error)
    throw error
  }
}

export async function removeFriend(userId: string, friendId: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/friendships`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        accept: "*/*",
      },
      body: JSON.stringify({ userId: userId, friendId: friendId, status: "DECLINED" }),
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return null
  } catch (error) {
    console.error("Error removing friend:", error)
    throw error
  }
}

export async function getMessages(
  chatType: "CHANNEL" | "DIRECT",
  channelId?: string,
  recipientId?: string,
  senderId?: string
) {
  try {
    if (chatType === "CHANNEL") {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/messages/channel?page=0&size=20&sortBy=createdAt&sortDirection=desc&channelId=${channelId}`
      )

      if (!response.ok) {
        throw new Error(`HTTP channel error! status: ${response.status}`)
      }

      const data = await response.json()
      return data.content
    } else if (chatType === "DIRECT") {
      const response = await fetch(
        `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/messages/direct?page=0&size=20&sortBy=createdAt&sortDirection=desc&senderId=${senderId}&recipientId=${recipientId}`
      )

      if (!response.ok) {
        throw new Error(`HTTP direct-message error! status: ${response.status}`)
      }

      const data = await response.json()
      return data.content
    }
  } catch (error) {
    console.error("Error fetching messages:", error)
    throw error
  }
}

export async function removeChannel(channelId: string) {
  try {
    const response = await fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/channels/${channelId}`, {
      method: "DELETE",
    })

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`)
    }

    return null
  } catch (error) {
    console.error("Error removing channel:", error)
    throw error
  }
}
