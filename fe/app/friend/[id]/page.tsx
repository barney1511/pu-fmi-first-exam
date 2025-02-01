import { SetId } from "@/components/sidebar-client"
import ChatComponent from "@/components/chat"

export default async function Friend({ params }: Readonly<{ params: Promise<{ id: string }> }>) {
  const id = (await params).id
  return (
    <>
      <SetId recipientId={id} />
      <ChatComponent recipientId={id} chatType="DIRECT" />
    </>
  )
}
