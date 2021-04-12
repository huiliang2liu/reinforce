package com.xh.activity.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DecodeDex {
    static void save(InputStream is, File file) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            byte[] buff = new byte[1024 * 1024];
            int len;
            while ((len = is.read(buff)) != -1)
                fos.write(buff, 0, len);
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(fos!=null)
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
