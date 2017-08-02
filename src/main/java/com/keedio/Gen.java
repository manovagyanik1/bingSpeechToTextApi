package com.keedio;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by shubhamagrawal on 02/08/17.
 */
public class Gen {

    // generic function to copy stream
    public static void copyStream(InputStream is, OutputStream os) {
        final int buffer_size = 4096;
        try {

            byte[] bytes = new byte[buffer_size];
            int k=-1;
            double prog=0;
            while ((k = is.read(bytes, 0, bytes.length)) > -1) {
                if(k != -1) {
                    os.write(bytes, 0, k);
                    prog=prog+k;
                    double progress = ((long) prog)/1000;///size;
                    System.out.println("UPLOADING: "+progress+" kB");
                }
            }
            os.flush();
            is.close();
            os.close();
        } catch (Exception ex) {
            System.out.println("File to Network Stream Copy error "+ex);
        }
    }
}
