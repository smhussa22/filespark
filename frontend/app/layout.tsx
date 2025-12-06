import type { Metadata } from "next";
import NavigationBar from "./components/NavigationBar";
import "./globals.css";

export const metadata: Metadata = {

  title: "FileSpark",
  description: "Bypass size limits completely for free",
  
};

export default function RootLayout( { children }: Readonly<{ children: React.ReactNode; }>) {

  return (

    <html lang="en">
    
      <body>
    
        <NavigationBar/>
        {children}
    
      </body>
    
    </html>
  
  );

}
