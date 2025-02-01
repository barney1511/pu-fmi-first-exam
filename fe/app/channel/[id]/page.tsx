import { SetId } from "@/components/sidebar-client"
import ChatComponent from "@/components/chat"

export default async function Channel({ params }: Readonly<{ params: Promise<{ id: string }> }>) {
  const id = (await params).id
  return (
    <>
      <SetId channelId={id} />
      <ChatComponent channelId={id} chatType={"CHANNEL"} />
    </>
  )
}
