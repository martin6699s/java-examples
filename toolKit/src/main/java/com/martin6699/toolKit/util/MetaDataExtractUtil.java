package com.martin6699.toolKit.util;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifSubIFDDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Created by martin on 2019/5/12.
 */
public class MetaDataExtractUtil {

    /**
     * output :
     * this video all path: /Users/martin/Documents/Work2019/toolKit/target/classes/static/example.mp4
     [MP4] Major Brand - MP4  Base Media v1 [IS0 14496-12:2003]
     [MP4] Minor Version - 512
     [MP4] Compatible Brands - [MP4  Base Media v1 [IS0 14496-12:2003], MP4 Base Media v2 [ISO 14496-12:2005], MP4 Base w/ AVC ext [ISO 14496-12:2005], MP4 v1 [ISO 14496-1:ch13]]
     [MP4] Creation Time - Fri Jan 01 00:00:00 CST 1904
     [MP4] Modification Time - Fri Jan 01 00:00:00 CST 1904
     [MP4] Duration - 00:06:53
     [MP4] Media Time Scale - 1000
     [MP4] Transformation Matrix - 65536 0 0 0 65536 0 0 0 1073741824
     [MP4] Preferred Rate - 1
     [MP4] Preferred Volume - 1
     [MP4] Next Track ID - 2
     this data ===== null
     [MP4 Video] Vendor - Fri Jan 01 00:00:00 CST 1904
     [MP4 Video] Temporal Quality - Fri Jan 01 00:00:00 CST 1904
     [MP4 Video] Width - 852 pixels
     [MP4 Video] Opcolor - 0 0 0
     [MP4 Video] Graphics Mode - Copy
     [MP4 Video] Height - 480 pixels
     [MP4 Video] Compression Type -
     [MP4 Video] Depth - Unknown (24)
     [MP4 Video] Horizontal Resolution - 72
     [MP4 Video] Vertical Resolution - 72
     [MP4 Video] Frame Rate - 23
     this data ===== null
     [File Type] Detected File Type Name - MP4
     [File Type] Detected File Type Long Name - MPEG-4 Part 14
     [File Type] Detected MIME Type - video/mp4
     [File Type] Expected File Name Extension - mp4
     this data ===== null
     [File] File Name - example.mp4
     [File] File Size - 42029134 bytes
     [File] File Modified Date - 星期日 五月 12 11:54:09 +08:00 2019
     this data ===== null
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String fileName = "static/videoplayback.mp4";
        ClassLoader classLoader = new MetaDataUtil().getClass().getClassLoader();

        File videoFile = new File(classLoader.getResource(fileName).getFile());

        System.out.println("this video all path: " + videoFile.getCanonicalPath());

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(videoFile);

            for(Directory directory : metadata.getDirectories()) {
                for(Tag tag : directory.getTags()) {
                    System.out.println(tag);
                }

                Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                System.out.println("this data ===== " + date);

            }




        } catch (ImageProcessingException e) {
            e.printStackTrace();
        }
    }
}
