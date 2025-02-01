import * as React from "react"

import { Sidebar, SidebarContent, SidebarHeader, SidebarRail } from "@/components/ui/sidebar"
import { AddButton, ChannelMembers, Channels, Friends } from "@/components/sidebar-client"

interface AppSidebarProps extends React.ComponentProps<typeof Sidebar> {
  user: {
    name: string
    email: string
    image: string
  }
}

export function AppSidebar({ user, ...props }: Readonly<AppSidebarProps>) {
  return (
    <Sidebar {...props}>
      <SidebarHeader className={"mt-2 ml-1.5"}>Menu</SidebarHeader>
      <SidebarContent>
        <Channels user={user} />
        <AddButton type={"Channels"} user={user} />
        <Friends />
        <AddButton type={"Friends"} user={user} />
        <ChannelMembers />
      </SidebarContent>
      <SidebarRail />
    </Sidebar>
  )
}
