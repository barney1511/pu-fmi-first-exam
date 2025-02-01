import { v5 as uuidv5 } from "uuid"

const GITHUB_NAMESPACE = "faa86ddc-c675-4629-b13d-1c8bddfbab22"

export function generateGitHubUUID(githubId: string) {
  return uuidv5(githubId, GITHUB_NAMESPACE)
}
