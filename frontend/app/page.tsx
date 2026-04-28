import Footer from "./components/Footer";
import { HeroVideoSection } from "./components/HeroVideoSection";
import { StatsSection } from "./components/StatsSection";

export const dynamic = "force-dynamic";

export default async function Home() {

  return (
    <>
      <HeroVideoSection />
      <StatsSection />
      <Footer />
    </>
  );
}
