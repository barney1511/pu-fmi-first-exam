"use client"

import { useSidebarStore } from "@/components/stores/provider"
import { useShallow } from "zustand/react/shallow"
import { Button } from "@/components/ui/button"
import * as React from "react"
import { useEffect, useState } from "react"
import { toast } from "sonner"

import { CommandDialog, CommandEmpty, CommandInput, CommandItem, CommandList } from "@/components/ui/command"
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from "@/components/ui/dialog"
import {
  addChannelMember,
  addFriend,
  changeRole,
  createChannel,
  getChannelMembers,
  getChannels,
  getFriends,
  removeChannel,
  removeChannelMember,
  removeFriend,
  searchUsers,
} from "@/app/actions"
import {
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
} from "@/components/ui/sidebar"
import { SearchType } from "@/components/stores/sidebar-store"
import Link from "next/link"
import { Input } from "@/components/ui/input"
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu"

export interface User {
  name: string
  email: string
  image: string
}

export const AddButton = ({ type }: { type: string }) => {
  const setSearchOpen = useSidebarStore(useShallow((state) => state.setSearchOpen))
  const setSearchType = useSidebarStore(useShallow((state) => state.setSearchType))
  const setDialogOpen = useSidebarStore(useShallow((state) => state.setDialogOpen))
  return (
    <Button
      className={"mx-4 bg-white text-black hover:text-white"}
      onClick={() => {
        if (type === "Channels") {
          setDialogOpen(true)
          return
        } else if (type === "Friends") {
          setSearchType(SearchType.addFriend)
        } else if (type === "Channel Members") {
          setSearchType(SearchType.addChannelMember)
        }
        setSearchOpen(true)
      }}
    >
      Add {type}
    </Button>
  )
}

export function SetId({ channelId, recipientId }: Readonly<{ channelId?: string; recipientId?: string }>) {
  const setChannelId = useSidebarStore(useShallow((state) => state.setChannelId))
  const setRecipientId = useSidebarStore(useShallow((state) => state.setRecipientId))
  useEffect(() => {
    if (channelId) {
      setChannelId(channelId)
    } else if (recipientId) {
      setRecipientId(recipientId)
    }
    return () => {
      if (channelId) setChannelId("")
      if (recipientId) setRecipientId("")
    }
  }, [channelId, recipientId, setChannelId, setRecipientId])
  return <></>
}

export enum Role {
  OWNER = "OWNER",
  ADMINISTRATOR = "ADMINISTRATOR",
  MEMBER = "MEMBER",
}

export function ChannelMembers() {
  const channelId = useSidebarStore(useShallow((state) => state.channelId))
  const { members, setMembers } = useSidebarStore(
    useShallow((state) => ({ members: state.members, setMembers: state.setMembers }))
  )
  const user = useSidebarStore(useShallow((state) => state.user))

  useEffect(() => {
    const fetchData = async () => {
      if (channelId.trim() === "") {
        setMembers([])
        return
      }
      try {
        const result = await getChannelMembers(channelId)
        setMembers(result)
      } catch (error) {
        toast("Failed to fetch channel members. Please try again.")
        setMembers([])
      }
    }

    void fetchData()
  }, [channelId, setMembers])

  if (members.length === 0) {
    return null
  }

  const handleRemoveMember = async (username: string) => {
    try {
      await removeChannelMember(channelId, username)
      const updatedMembers = await getChannelMembers(channelId)
      setMembers(updatedMembers)
    } catch (error) {
      console.error("Error removing channel member:", error)
      toast("Failed to remove member from the channel")
    }
  }

  const handleRoleChange = async (username: string, newRole: Role) => {
    try {
      await changeRole(channelId, username, newRole)
      const updatedMembers = await getChannelMembers(channelId)
      setMembers(updatedMembers)
    } catch (error) {
      console.error("Error changing member role:", error)
      toast("Failed to change member role")
    }
  }

  return (
    <SidebarGroup>
      <SidebarGroupLabel className={"text-md mb-2"}>Channel Members</SidebarGroupLabel>
      <SidebarGroupContent className={"mb-4"}>
        <SidebarMenu>
          {members.map((member) => (
            <SidebarMenuItem key={member.userId} className={"flex flex-col"}>
              <div className="flex items-center justify-between">
                <SidebarMenuButton>{member.username}</SidebarMenuButton>
              </div>
              <div className="ml-2 flex justify-between text-gray-500">
                {member.role !== "OWNER" ? (
                  <DropdownMenu>
                    <DropdownMenuTrigger asChild>
                      <Button className="bg-white text-black hover:text-white">
                        {member.role === Role.ADMINISTRATOR ? "ADMIN" : member.role}
                      </Button>
                    </DropdownMenuTrigger>
                    <DropdownMenuContent>
                      <DropdownMenuItem onClick={() => void handleRoleChange(member.username, Role.MEMBER)}>
                        Member
                      </DropdownMenuItem>
                      <DropdownMenuItem onClick={() => void handleRoleChange(member.username, Role.ADMINISTRATOR)}>
                        Admin
                      </DropdownMenuItem>
                    </DropdownMenuContent>
                  </DropdownMenu>
                ) : (
                  member.role
                )}
                {member.role !== "OWNER" && (
                  <Button
                    className={"mx-4 bg-white text-black hover:text-white"}
                    onClick={() => void handleRemoveMember(member.username)}
                  >
                    Remove
                  </Button>
                )}
              </div>
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
      <AddButton type={"Channel Members"} user={user} />
    </SidebarGroup>
  )
}

export function Friends() {
  const { friends, setFriends } = useSidebarStore(
    useShallow((state) => ({ friends: state.friends, setFriends: state.setFriends }))
  )
  const user = useSidebarStore(useShallow((state) => state.user))

  useEffect(() => {
    const fetchData = async () => {
      if (!user) {
        return
      }
      try {
        const result = await getFriends(user.name)
        const updatedFriends = result.map((friend) => {
          if (friend.friendName === user.name) {
            return { ...friend, friendName: friend.username, username: friend.friendName }
          }
          return friend
        })
        setFriends(updatedFriends)
      } catch (error) {
        toast("Failed to fetch friends. Please try again.")
        setFriends([])
      }
    }

    void fetchData()
  }, [user, setFriends])

  if (friends.length === 0) {
    return null
  }

  return (
    <SidebarGroup>
      <SidebarGroupLabel className={"text-md mb-2"}>Friends</SidebarGroupLabel>
      <SidebarGroupContent>
        <SidebarMenu>
          {friends.map((friend) => (
            <SidebarMenuItem key={friend.friendshipId} className={"flex"}>
              <SidebarMenuButton asChild>
                <Link href={`/friend/${friend.friendId}`}>{friend.friendName}</Link>
              </SidebarMenuButton>
              <RemoveButton type={"Friends"} friendId={friend.friendId} />
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  )
}

export function Channels({ user }: Readonly<{ user: User }>) {
  const { channels, setChannels } = useSidebarStore(
    useShallow((state) => ({ channels: state.channels, setChannels: state.setChannels }))
  )
  const setUser = useSidebarStore(useShallow((state) => state.setUser))
  const setUserId = useSidebarStore(useShallow((state) => state.setUserId))

  useEffect(() => {
    setUser(user)

    const setId = async () => {
      try {
        const { content: response } = await searchUsers(user.name)
        setUserId(response[0].userId)
      } catch (error) {
        console.error("Error fetching user:", error)
        toast("Failed to fetch user. Please try again.")
      }
    }

    void setId()
  }, [user, setUser, setUserId])

  useEffect(() => {
    const fetchData = async () => {
      if (!user) {
        return
      }
      try {
        const result = await getChannels(user.name)
        setChannels(result)
      } catch (error) {
        toast("Failed to fetch channels. Please try again.")
        setChannels([])
      }
    }

    void fetchData()
  }, [user, setChannels])

  if (channels.length === 0) {
    return null
  }

  return (
    <SidebarGroup>
      <SidebarGroupLabel className={"text-md mb-2"}>Channels</SidebarGroupLabel>
      <SidebarGroupContent>
        <SidebarMenu>
          {channels.map((channel) => (
            <SidebarMenuItem key={channel.channelId} className={"flex"}>
              <SidebarMenuButton asChild>
                <Link href={`/channel/${channel.channelId}`}>{channel.name}</Link>
              </SidebarMenuButton>
              <RemoveButton type={"Channels"} channelId={channel.channelId} />
            </SidebarMenuItem>
          ))}
        </SidebarMenu>
      </SidebarGroupContent>
    </SidebarGroup>
  )
}

export function GlobalSearch() {
  const [searchTerm, setSearchTerm] = useState("")
  const [searchResults, setSearchResults] = useState([])

  const channelId = useSidebarStore(useShallow((state) => state.channelId))
  const { isSearchOpen, setSearchOpen } = useSidebarStore(
    useShallow((state) => ({
      isSearchOpen: state.isSearchOpen,
      setSearchOpen: state.setSearchOpen,
    }))
  )
  const setMembers = useSidebarStore(useShallow((state) => state.setMembers))
  const searchType = useSidebarStore(useShallow((state) => state.searchType))
  const user = useSidebarStore(useShallow((state) => state.user))
  const userId = useSidebarStore(useShallow((state) => state.userId))
  const setFriends = useSidebarStore(useShallow((state) => state.setFriends))

  useEffect(() => {
    const fetchData = async () => {
      if (searchTerm.trim() === "") {
        setSearchResults([])
        return
      }

      try {
        if (searchType === SearchType.addChannelMember || searchType === SearchType.addFriend) {
          const result = await searchUsers(searchTerm)
          setSearchResults(result.content)
        }
      } catch (error) {
        if (searchType === SearchType.addChannelMember || searchType === SearchType.addFriend) {
          toast("Failed to search users. Please try again.")
        }
        setSearchResults([])
      }
    }

    const delayDebounceFn = setTimeout(() => {
      void fetchData()
    }, 150)

    return () => clearTimeout(delayDebounceFn)
  }, [searchTerm, searchType])

  const handleItemClick = async (username: string) => {
    try {
      if (searchType === SearchType.addChannelMember) {
        await addChannelMember(channelId, username)
        toast(`Added ${username} to the channel`)
        const updatedMembers = await getChannelMembers(channelId)
        setMembers(updatedMembers)
      } else if (searchType === SearchType.addFriend) {
        const result = await searchUsers(username)
        const userToAdd = result.content.find((user) => user.username === username)
        if (userToAdd) {
          await addFriend(userToAdd.userId, userId)
          toast(`Added ${username} as a friend`)
          const updatedFriends = await getFriends(user.name)
          setFriends(updatedFriends)
        }
      }
    } catch (error) {
      if (searchType === SearchType.addChannelMember) {
        console.error("Error adding channel member:", error)
        toast("Failed to add member to the channel")
      } else if (searchType === SearchType.addFriend) {
        console.error("Error adding friend:", error)
        toast("Failed to add friend")
      }
    } finally {
      setSearchOpen(false)
    }
  }

  return (
    <CommandDialog open={isSearchOpen} onOpenChange={setSearchOpen}>
      <DialogTitle className={"sr-only"}>Add Channel Member</DialogTitle>
      <CommandInput value={searchTerm} onValueChange={setSearchTerm} placeholder="Search for user..." />
      <CommandList>
        {searchResults.length === 0 ? (
          <CommandEmpty>No users found.</CommandEmpty>
        ) : (
          searchResults.map((user) => (
            <CommandItem key={user.userId} onSelect={() => void handleItemClick(user.username)}>
              {user.username}
            </CommandItem>
          ))
        )}
      </CommandList>
    </CommandDialog>
  )
}

export function RemoveButton({
  type,
  friendId,
  channelId,
}: Readonly<{ type: string; friendId?: string; channelId?: string }>) {
  const user = useSidebarStore(useShallow((state) => state.user))
  const setFriends = useSidebarStore(useShallow((state) => state.setFriends))
  const setChannels = useSidebarStore(useShallow((state) => state.setChannels))
  const handleClick = async () => {
    if (type === "Channels") {
      await removeChannel(channelId)
      const result = await getChannels(user.name)
      setChannels(result)
    } else if (type === "Friends") {
      const { content: users } = await searchUsers(user.name)
      await removeFriend(users[0].userId, friendId)
      const result = await getFriends(user.name)
      const updatedFriends = result.map((friend) => {
        if (friend.friendName === user.name) {
          return { ...friend, friendName: friend.username, username: friend.friendName }
        }
        return friend
      })
      setFriends(updatedFriends)
    }
  }
  return (
    <Button className={"mx-4 bg-white text-black hover:text-white"} onClick={() => void handleClick()}>
      Remove
    </Button>
  )
}

export function DialogCreate() {
  const { dialogOpen, setDialogOpen } = useSidebarStore(
    useShallow((state) => ({
      dialogOpen: state.dialogOpen,
      setDialogOpen: state.setDialogOpen,
    }))
  )
  const [channelName, setChannelName] = useState("")
  const userId = useSidebarStore(useShallow((state) => state.userId))
  const user = useSidebarStore(useShallow((state) => state.user))
  const { channels, setChannels } = useSidebarStore(
    useShallow((state) => ({ channels: state.channels, setChannels: state.setChannels }))
  )

  const handleCreateChannel = async () => {
    if (!channelName.trim()) {
      return
    }

    try {
      await createChannel(channelName, userId)
      const updatedChannels = await getChannels(user.name)
      setChannels(updatedChannels)
      toast(`Channel "${channelName}" created successfully`)
      setDialogOpen(false)
      setChannelName("")
    } catch (error) {
      console.error("Error creating channel:", error)
      toast("Failed to create channel. Please try again.")
    }
  }

  return (
    <Dialog open={dialogOpen} onOpenChange={setDialogOpen}>
      <DialogContent className={"text-black"}>
        <DialogHeader>
          <DialogTitle>Create a new channel</DialogTitle>
        </DialogHeader>
        <Input
          value={channelName}
          onChange={(e) => {
            setChannelName(e.target.value)
          }}
          placeholder="Enter channel name"
        />
        <DialogFooter>
          <Button onClick={() => void handleCreateChannel()}>Create</Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}
