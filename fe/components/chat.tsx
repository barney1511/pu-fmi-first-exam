"use client"

import { useEffect, useState } from "react"
import { Client } from "@stomp/stompjs"
import { useSidebarStore } from "@/components/stores/provider"
import { useShallow } from "zustand/react/shallow"
import { getMessages } from "@/app/actions"

interface UserDTO {
  userId: string
  username: string
}

interface Message {
  messageId: string
  channelId?: string
  sender: UserDTO
  recipient?: UserDTO
  type: "CHANNEL" | "DIRECT"
  content: string
}

interface ChatComponentProps {
  chatType: "CHANNEL" | "DIRECT"
  channelId?: string
  recipientId?: string
}

export default function ChatComponent({ channelId, recipientId, chatType }: Readonly<ChatComponentProps>) {
  const [messages, setMessages] = useState<Message[]>([])
  const [inputMessage, setInputMessage] = useState("")
  const [connected, setConnected] = useState(false)
  const [stompClient, setStompClient] = useState<Client | null>(null)
  const { userId: CURRENT_USER_ID, user: CURRENT_USER } = useSidebarStore(
    useShallow((state) => ({
      userId: state.userId,
      user: state.user,
    }))
  )
  const channels = useSidebarStore(useShallow((state) => state.channels))
  const channel = channels.find((c) => c.channelId === channelId)

  const friends = useSidebarStore(useShallow((state) => state.friends))
  const friend = friends.find((f) => f.friendId === recipientId)

  useEffect(() => {
    const fetchMessages = async () => {
      try {
        if (chatType === "CHANNEL" && channelId) {
          const fetchedMessages = await getMessages(chatType, channelId)
          setMessages(fetchedMessages)
        } else if (chatType === "DIRECT" && recipientId && CURRENT_USER_ID) {
          const fetchedMessages = await getMessages(chatType, undefined, recipientId, CURRENT_USER_ID)
          setMessages(fetchedMessages)
        }
      } catch (error) {
        console.error("Error fetching messages:", error)
      }
    }

    void fetchMessages()

    const client = new Client({
      brokerURL: "ws://localhost:8080/ws",
      debug: (str) => {
        console.log("STOMP: " + str)
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    })

    client.onConnect = (frame) => {
      console.log("Connected")
      setConnected(true)

      if (chatType === "CHANNEL" && channelId) {
        client.subscribe(`/topic/channel/${channelId}`, (message) => {
          const receivedMessage = JSON.parse(message.body)
          setMessages((prevMessages) => [...prevMessages, receivedMessage])
        })
      }

      if (chatType === "DIRECT" && CURRENT_USER_ID) {
        client.subscribe(`/user/${CURRENT_USER_ID}/topic/direct`, (message) => {
          const receivedMessage = JSON.parse(message.body)
          setMessages((prevMessages) => [...prevMessages, receivedMessage])
        })
      }
    }

    client.onDisconnect = () => {
      setConnected(false)
    }

    client.onStompError = (frame) => {
      console.error("STOMP error", frame)
      setConnected(false)
    }

    client.activate()
    setStompClient(client)

    return () => {
      if (client) {
        void client.deactivate()
      }
    }
  }, [channelId, recipientId, chatType, CURRENT_USER_ID])

  const sendMessage = (e: React.FormEvent) => {
    e.preventDefault()
    if (!inputMessage.trim() || !stompClient || !connected) return

    const messageRequest = {
      channelId: chatType === "CHANNEL" ? channelId : undefined,
      senderId: CURRENT_USER_ID,
      recipientId: chatType === "DIRECT" ? recipientId : undefined,
      content: inputMessage,
      type: chatType,
    }

    const destination = chatType === "CHANNEL" ? `/app/channel/${channelId}` : `/app/direct/${recipientId}`

    stompClient.publish({
      destination,
      body: JSON.stringify(messageRequest),
    })

    const optimisticMessage: Message = {
      messageId: `temp-${Date.now()}`,
      channelId: channelId,
      sender: {
        userId: CURRENT_USER_ID,
        username: CURRENT_USER.name,
      },
      recipient: chatType === "DIRECT" ? { userId: recipientId!, username: friend.friendName } : undefined,
      type: chatType,
      content: inputMessage,
    }

    setMessages((prev) => [...prev, optimisticMessage])
    setInputMessage("")
  }

  const getChatTitle = () => {
    if (chatType === "CHANNEL") {
      return channel ? `Chat ${channel.name}` : "Loading..."
    } else if (chatType === "DIRECT") {
      return friend ? `${friend.friendName} - Direct Message` : "Loading..."
    }
  }

  return (
    <div className="flex h-full flex-col text-black">
      <div className="flex justify-between border-b p-2">
        <div className="font-bold">{getChatTitle()}</div>
        <div className={`px-2 text-sm ${connected ? "text-green-600" : "text-red-600"}`}>
          {connected ? "Connected" : "Disconnected"}
        </div>
      </div>

      <div className="flex-1 overflow-y-auto p-4">
        {messages
          .filter((msg) => !msg.messageId.startsWith("temp-"))
          .map((msg) => (
            <div
              key={msg.messageId}
              className={`mb-2 rounded-lg p-2 ${
                msg.sender.userId === CURRENT_USER_ID ? "ml-auto bg-blue-200" : "bg-gray-200"
              } max-w-[50%]`}
            >
              <div className="text-sm font-bold">{msg.sender.username}</div>
              <div>{msg.content}</div>
            </div>
          ))}
      </div>

      <form onSubmit={sendMessage} className="border-t p-4">
        <div className="flex gap-2">
          <input
            type="text"
            value={inputMessage}
            onChange={(e) => setInputMessage(e.target.value)}
            placeholder={`Type a message to ${chatType === "CHANNEL" ? "channel" : "user"}...`}
            className="flex-1 rounded border p-2"
            disabled={!connected}
          />
          <button
            type="submit"
            disabled={!connected}
            className="rounded bg-blue-500 px-4 py-2 text-white disabled:bg-gray-300"
          >
            Send
          </button>
        </div>
      </form>
    </div>
  )
}
