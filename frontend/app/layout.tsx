import type { Metadata, Viewport } from "next";
import NavigationBar from "./components/NavigationBar";
import "./globals.css";
import Script from "next/script";
import { getCurrentUser } from "./lib/auth";

export const metadata: Metadata = {
  title: "FileSpark",
  description: "Bypass size limits completely for free",
};

export const viewport: Viewport = {
  width: "device-width",
  initialScale: 1,
  viewportFit: "cover",
};

export default async function RootLayout({
  children,
}: Readonly<{ children: React.ReactNode }>) {
  const user = await getCurrentUser();

  return (

    <html lang="en">

      <body>

        <Script
          src="https://accounts.google.com/gsi/client"
          async
          strategy="afterInteractive"
        />

        <NavigationBar />

        {children}

      </body>

    </html>

  );
  
}
