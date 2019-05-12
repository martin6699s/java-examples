package com.martin6699.toolKit.util;
import net.sf.ehcache.management.ResourceClassLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp4.MP4Parser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

/**
 * Created by martin on 2019/5/12.
 */
public class MetaDataUtil {

    /**
     *
     * output :
     this video all path: /Users/martin/Documents/Work2019/toolKit/target/classes/static/example.mp4
     Contents of the document:  :Lavf58.20.100

     Metadata of the document:
     date: 1904-01-01T00:00:00Z
     xmp:CreatorTool: Lavf58.20.100
     meta:creation-date: 1904-01-01T00:00:00Z
     Creation-Date: 1904-01-01T00:00:00Z
     tiff:ImageLength: 480
     dcterms:created: 1904-01-01T00:00:00Z
     dcterms:modified: 1904-01-01T00:00:00Z
     Last-Modified: 1904-01-01T00:00:00Z
     Last-Save-Date: 1904-01-01T00:00:00Z
     xmpDM:audioSampleRate: 1000
     meta:save-date: 1904-01-01T00:00:00Z
     modified: 1904-01-01T00:00:00Z
     tiff:ImageWidth: 852
     xmpDM:duration: 413.78
     Content-Type: application/mp4

     *
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws TikaException
     */
    public static void main(final String[] args) throws IOException,SAXException, TikaException {


        String fileName = "static/videoplayback.mp4";
        ClassLoader classLoader = new MetaDataUtil().getClass().getClassLoader();

        File videoFile = new File(classLoader.getResource(fileName).getFile());

        System.out.println("this video all path: " + videoFile.getCanonicalPath());

        //detecting the file type
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputstream = new FileInputStream(videoFile);
        ParseContext pcontext = new ParseContext();

        //Html parser
        MP4Parser MP4Parser = new MP4Parser();
        MP4Parser.parse(inputstream, handler, metadata,pcontext);
        System.out.println("Contents of the document:  :" + handler.toString());
        System.out.println("Metadata of the document:");
        String[] metadataNames = metadata.names();

        for(String name : metadataNames) {
            System.out.println(name + ": " + metadata.get(name));
        }
    }
}
