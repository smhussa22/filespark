export function StatsSection() {

  return (

    <section className="relative z-20 bg-mainblack">

      <div className="mx-auto max-w-7xl px-6 py-16">

        <div className="grid grid-cols-1 md:grid-cols-3 gap-10 text-center">
          
          <div>

            <div className="text-3xl font-bold text-white">

              56

            </div>

            <div className="mt-2 text-sm text-mainorange">

              files uploaded

            </div>

          </div>

          <div>

            <div className="text-3xl font-bold text-white">
              6
            </div>

            <div className="mt-2 text-sm text-mainorange">
              users connected
            </div>

          </div>

          <div>
            <div className="text-3xl font-bold text-white">
              12+
            </div>
            <div className="mt-2 text-sm text-mainorange">
              supported file types
            </div>
          </div>

        </div>
      </div>
    </section>
  );
}
