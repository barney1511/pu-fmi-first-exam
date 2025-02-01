import NextAuth from "next-auth"
import GitHub from "next-auth/providers/github"
import type { Provider } from "next-auth/providers"
import { createOrUpdateGitHubUser } from "@/lib/sync-users"
import authConfig from "@/auth.config"

const providers: Provider[] = [GitHub]

export const providerMap = providers
  .map((provider) => {
    if (typeof provider === "function") {
      const providerData = provider()
      return { id: providerData.id, name: providerData.name }
    } else {
      return { id: provider.id, name: provider.name }
    }
  })
  .filter((provider) => provider.id !== "credentials")

export const { handlers, signIn, auth } = NextAuth({
  ...authConfig,
  session: { strategy: "jwt" },
  callbacks: {
    async signIn({ profile }) {
      if (profile) {
        try {
          await createOrUpdateGitHubUser(profile)
          return true
        } catch (error) {
          console.error("Error syncing user:", error)
          return false
        }
      } else {
        console.error("Profile is undefined")
        return false
      }
    },
  },
})
