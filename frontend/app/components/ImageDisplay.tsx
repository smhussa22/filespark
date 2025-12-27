import React from 'react';

export default function ImageDisplay( {src, fileid}: {src: string, fileid: string}) {

    return (

        <>

            <img src={src} alt={fileid} className="max-w-3xl rounded-xl shadow-lg"/>

        </>

    );

}