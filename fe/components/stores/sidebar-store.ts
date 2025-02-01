import { createStore } from "zustand/vanilla"
import { User } from "@/components/sidebar-client"

export enum SearchType {
  addChannelMember = "addChannelMember",
  addFriend = "addFriend",
  addChannel = "addChannel",
}

export enum RemoveButtonType {
  removeChannelMember = "removeChannelMember",
  removeFriend = "removeFriend",
}

export interface SidebarState {
  user: User
  userId: string
  channels: any[]
  channelId: string
  recipientId: string
  isSearchOpen: boolean
  searchType: SearchType
  members: any[]
  friends: any[]
  removeButtonType: RemoveButtonType
  dialogOpen: boolean
}

export interface SidebarActions {
  setUser: (user: User) => void
  setUserId: (userId: string) => void
  setChannels: (channels: any[]) => void
  setChannelId: (channelId: string) => void
  setRecipientId: (recipientId: string) => void
  setSearchOpen: (isSearchOpen: boolean) => void
  setSearchType: (searchType: SearchType) => void
  setMembers: (members: any[]) => void
  setFriends: (friends: any[]) => void
  setRemoveButtonType: (removeButtonType: RemoveButtonType) => void
  setDialogOpen: (dialogOpen: boolean) => void
}

export type SidebarStore = SidebarActions & SidebarState

export const defaultSidebarState: SidebarState = {
  user: null as unknown as User,
  userId: "",
  channels: [],
  channelId: "",
  recipientId: "",
  isSearchOpen: false,
  searchType: SearchType.addChannelMember,
  members: [],
  friends: [],
  removeButtonType: RemoveButtonType.removeChannelMember,
  dialogOpen: false,
}

export const createSidebarStore = (initialState: SidebarState = defaultSidebarState) => {
  return createStore<SidebarStore>()((set) => ({
    ...initialState,
    setUser: (user) => set({ user: user }),
    setUserId: (userId) => set({ userId: userId }),
    setChannels: (channels) => set({ channels: channels }),
    setChannelId: (channelId) => set({ channelId: channelId }),
    setRecipientId: (recipientId) => set({ recipientId: recipientId }),
    setSearchOpen: (isSearchOpen) => set({ isSearchOpen: isSearchOpen }),
    setSearchType: (searchType) => set({ searchType: searchType }),
    setMembers: (members) => set({ members: members }),
    setFriends: (friends) => set({ friends: friends }),
    setRemoveButtonType: (removeButtonType) => set({ removeButtonType: removeButtonType }),
    setDialogOpen: (dialogOpen) => set({ dialogOpen: dialogOpen }),
  }))
}
