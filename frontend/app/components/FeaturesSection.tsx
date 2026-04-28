import { FaKeyboard, FaImage, FaLink, FaShieldAlt } from "react-icons/fa";

export function FeaturesSection() {

  return (

    <section className="relative z-20 bg-mainblack border-t border-maingrey">

      <div className="mx-auto max-w-6xl px-6 py-20">

        <div className="text-center mb-14">

          <h2 className="text-3xl md:text-4xl font-bold text-mainwhite">
            Share files at the speed of <span className="text-mainorange">copy &amp; paste</span>
          </h2>

          <p className="mt-3 text-mainwhite/70 max-w-2xl mx-auto">
            FileSpark Desktop sits in your tray and turns any file or screenshot on your clipboard
            into a sharable link in one keystroke.
          </p>

        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">

          <Feature
            icon={<FaKeyboard size={28} />}
            title="Ctrl + Shift + U"
            body="Press it anywhere on your computer. The file or screenshot you've copied becomes a public link, copied straight to your clipboard."
          />

          <Feature
            icon={<FaImage size={28} />}
            title="Screenshots, instantly"
            body="Snip with Win + Shift + S, hit the hotkey, paste the link in Discord. No file save, no upload window, no waiting."
          />

          <Feature
            icon={<FaLink size={28} />}
            title="Links that embed"
            body="Pasted into Discord, Slack, or X, FileSpark links unfurl with previews — image, video, audio, or document."
          />

          <Feature
            icon={<FaShieldAlt size={28} />}
            title="Private by default"
            body="New uploads are private until you flip them to unlisted or public. Delete a file and the link dies with it."
          />

        </div>

      </div>

    </section>

  );

}

function Feature({ icon, title, body }: { icon: React.ReactNode; title: string; body: string }) {

  return (

    <div className="rounded-lg border border-maingrey bg-mainblack/40 p-6 hover:border-mainorange/60 transition">

      <div className="text-mainorange mb-4">{icon}</div>

      <h3 className="text-mainwhite text-lg font-semibold mb-2">{title}</h3>

      <p className="text-mainwhite/70 text-sm leading-relaxed">{body}</p>

    </div>

  );

}
