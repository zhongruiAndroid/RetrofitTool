//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.log;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DiskLogStrategy implements LogStrategy {
    private final Handler handler;

    public DiskLogStrategy(Handler handler) {
        this.handler = handler;
    }

    public void log(int level, String tag, String message) {
        this.handler.sendMessage(this.handler.obtainMessage(level, message));
    }

    static class WriteHandler extends Handler {
        private final String folder;
        private final int maxFileSize;

        WriteHandler(Looper looper, String folder, int maxFileSize) {
            super(looper);
            this.folder = folder;
            this.maxFileSize = maxFileSize;
        }

        public void handleMessage(Message msg) {
            String content = (String)msg.obj;
            FileWriter fileWriter = null;
            File logFile = this.getLogFile(this.folder, "logs");

            try {
                fileWriter = new FileWriter(logFile, true);
                this.writeLog(fileWriter, content);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException var8) {
                if(fileWriter != null) {
                    try {
                        fileWriter.flush();
                        fileWriter.close();
                    } catch (IOException var7) {
                        ;
                    }
                }
            }

        }

        private void writeLog(FileWriter fileWriter, String content) throws IOException {
            fileWriter.append(content);
        }

        private File getLogFile(String folderName, String fileName) {
            File folder = new File(folderName);
            if(!folder.exists()) {
                folder.mkdirs();
            }

            int newFileCount = 0;
            File existingFile = null;

            File newFile;
            for(newFile = new File(folder, String.format("%s_%s.csv", new Object[]{fileName, Integer.valueOf(newFileCount)})); newFile.exists(); newFile = new File(folder, String.format("%s_%s.csv", new Object[]{fileName, Integer.valueOf(newFileCount)}))) {
                existingFile = newFile;
                ++newFileCount;
            }

            return existingFile != null?(existingFile.length() >= (long)this.maxFileSize?newFile:existingFile):newFile;
        }
    }
}
