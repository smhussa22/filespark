import React from 'react';

export default function AudioPlayer({src}: {src: string}) {

    return (

        <>

            <audio controls src={src} className="w-full max-w-lg" />

        </>

    );

}