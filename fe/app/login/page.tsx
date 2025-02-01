import { Button } from "@/components/ui/button"
import { providerMap, signIn } from "@/auth"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"

interface SearchParams {
  callbackUrl?: string
}

interface SignInPageProps {
  searchParams: SearchParams
}

export default function SignInPage({ searchParams }: Readonly<SignInPageProps>) {
  return (
    <div className="flex h-screen items-center justify-center">
      <Card>
        <CardHeader className="text-center">
          <CardTitle className="text-xl">Welcome back</CardTitle>
          <CardDescription>Login with your Github account</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="grid gap-6">
            <div className="flex flex-col gap-4">
              {Object.values(providerMap).map((provider) => (
                <form
                  key={provider.id}
                  action={async () => {
                    "use server"
                    await signIn(provider.id, {
                      callbackUrl: searchParams.callbackUrl ?? "",
                      redirectTo: "/",
                      redirect: true,
                    })
                  }}
                >
                  <Button variant="outline" type="submit" className="w-full">
                    Sign in with {provider.name}
                  </Button>
                </form>
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
