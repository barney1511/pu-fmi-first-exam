import type { NextConfig } from "next"
import WithBundleAnalyzer from "@next/bundle-analyzer"

let nextConfig: NextConfig = {
  /* config options here */
}

const withAnalyzer = (sourceConfig: NextConfig): NextConfig => WithBundleAnalyzer()(sourceConfig)

if (process.env.ANALYZE === "true") {
  nextConfig = withAnalyzer(nextConfig)
}

export default nextConfig
