import Footer from "./components/Footer";
import { FeaturesSection } from "./components/FeaturesSection";
import { HeroVideoSection } from "./components/HeroVideoSection";
import { getStats } from "./components/StatsSection";

export const dynamic = "force-dynamic";

export default async function Home() {
  const stats = await getStats();

  return (
    <>
      <HeroVideoSection stats={stats} />
      <FeaturesSection />
      <Footer />
    </>
  );
}
