//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.log;

import android.util.Log;


public class LogcatLogStrategy implements LogStrategy {
    public LogcatLogStrategy() {
    }

    public void log(int priority, String tag, String message) {
        Log.println(priority, tag, message);
    }
}
