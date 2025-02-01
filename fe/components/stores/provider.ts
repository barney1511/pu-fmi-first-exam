import { create } from "zustand"
import { createSidebarStore, RemoveButtonType, SearchType, SidebarStore } from "./sidebar-store"
import { User } from "@/components/sidebar-client"

const ssrSafe =
  (config: (set: any, get: any, api: any) => any, isSSR = typeof window === "undefined") =>
  (set: any, get: any, api: any) => {
    if (!isSSR) {
      return config(set, get, api)
    }
    const ssrSet = () => {
      throw new Error("Cannot set state of Zustand store in SSR")
    }
    api.setState = ssrSet
    return config(ssrSet, get, api)
  }

const sidebarStore = createSidebarStore()

export const useSidebarStore = create<SidebarStore>(
  ssrSafe((set: any, get: any, api: any) => ({
    ...sidebarStore.getState(),
    setUser: (user: User) => set({ user: user }),
    setUserId: (userId: string) => set({ userId: userId }),
    setChannels: (channels: any[]) => set({ channels: channels }),
    setChannelId: (channelId: string) => set({ channelId: channelId }),
    setRecipientId: (recipientId: string) => set({ recipientId: recipientId }),
    setSearchOpen: (isSearchOpen: boolean) => set({ isSearchOpen: isSearchOpen }),
    setSearchType: (searchType: SearchType) => set({ searchType: searchType }),
    setMembers: (members: any[]) => set({ members: members }),
    setFriends: (friends: any[]) => set({ friends: friends }),
    setRemoveButtonType: (removeButtonType: RemoveButtonType) => set({ removeButtonType: removeButtonType }),
  }))
)
