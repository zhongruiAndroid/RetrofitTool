//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.log;



public class PrettyFormatStrategy implements FormatStrategy {
    private static final int CHUNK_SIZE = 4000;
    private static final int MIN_STACK_OFFSET = 5;
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String BOTTOM_BORDER = "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────";
    private static final String MIDDLE_BORDER = "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private final int methodCount;
    private final int methodOffset;
    private final boolean showThreadInfo;
    private final LogStrategy logStrategy;
    private final String tag;

    private PrettyFormatStrategy(PrettyFormatStrategy.Builder builder) {
        this.methodCount = builder.methodCount;
        this.methodOffset = builder.methodOffset;
        this.showThreadInfo = builder.showThreadInfo;
        this.logStrategy = builder.logStrategy;
        this.tag = builder.tag;
    }

    public static PrettyFormatStrategy.Builder newBuilder() {
        return new PrettyFormatStrategy.Builder();
    }

    public void log(int priority, String onceOnlyTag, String message) {
        String tag = this.formatTag(onceOnlyTag);
        this.logTopBorder(priority, tag);
        this.logHeaderContent(priority, tag, this.methodCount);
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if(length <= 4000) {
            if(this.methodCount > 0) {
                this.logDivider(priority, tag);
            }

            this.logContent(priority, tag, message);
            this.logBottomBorder(priority, tag);
        } else {
            if(this.methodCount > 0) {
                this.logDivider(priority, tag);
            }

            for(int i = 0; i < length; i += 4000) {
                int count = Math.min(length - i, 4000);
                this.logContent(priority, tag, new String(bytes, i, count));
            }

            this.logBottomBorder(priority, tag);
        }
    }

    private void logTopBorder(int logType, String tag) {
        this.logChunk(logType, tag, "┌────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
    }

    private void logHeaderContent(int logType, String tag, int methodCount) {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        if(this.showThreadInfo) {
            this.logChunk(logType, tag, "│ Thread: " + Thread.currentThread().getName());
            this.logDivider(logType, tag);
        }

        String level = "";
        int stackOffset = this.getStackOffset(trace) + this.methodOffset;
        if(methodCount + stackOffset > trace.length) {
            methodCount = trace.length - stackOffset - 1;
        }

        for(int i = methodCount; i > 0; --i) {
            int stackIndex = i + stackOffset;
            if(stackIndex < trace.length) {
                StringBuilder builder = new StringBuilder();
                builder.append('│').append(' ').append(level).append(this.getSimpleClassName(trace[stackIndex].getClassName())).append(".").append(trace[stackIndex].getMethodName()).append(" ").append(" (").append(trace[stackIndex].getFileName()).append(":").append(trace[stackIndex].getLineNumber()).append(")");
                level = level + "   ";
                this.logChunk(logType, tag, builder.toString());
            }
        }

    }

    private void logBottomBorder(int logType, String tag) {
        this.logChunk(logType, tag, "└────────────────────────────────────────────────────────────────────────────────────────────────────────────────");
    }

    private void logDivider(int logType, String tag) {
        this.logChunk(logType, tag, "├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄");
    }

    private void logContent(int logType, String tag, String chunk) {
        String[] lines = chunk.split(System.getProperty("line.separator"));
        String[] var5 = lines;
        int var6 = lines.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String line = var5[var7];
            this.logChunk(logType, tag, "│ " + line);
        }

    }

    private void logChunk(int priority, String tag, String chunk) {
        this.logStrategy.log(priority, tag, chunk);
    }

    private String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(lastIndex + 1);
    }

    private int getStackOffset(StackTraceElement[] trace) {
        for(int i = 5; i < trace.length; ++i) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if(!name.equals(LoggerPrinter.class.getName()) && !name.equals(L.class.getName())) {
                --i;
                return i;
            }
        }

        return -1;
    }

    private String formatTag(String tag) {
        return !Utils.isEmpty(tag) && !Utils.equals(this.tag, tag)?this.tag + "-" + tag:this.tag;
    }

    public static class Builder {
        int methodCount;
        int methodOffset;
        boolean showThreadInfo;
        LogStrategy logStrategy;
        String tag;

        private Builder() {
            this.methodCount = 2;
            this.methodOffset = 0;
            this.showThreadInfo = true;
            this.tag = "PRETTY_LOGGER";
        }

        public PrettyFormatStrategy.Builder methodCount(int val) {
            this.methodCount = val;
            return this;
        }

        public PrettyFormatStrategy.Builder methodOffset(int val) {
            this.methodOffset = val;
            return this;
        }

        public PrettyFormatStrategy.Builder showThreadInfo(boolean val) {
            this.showThreadInfo = val;
            return this;
        }

        public PrettyFormatStrategy.Builder logStrategy(LogStrategy val) {
            this.logStrategy = val;
            return this;
        }

        public PrettyFormatStrategy.Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public PrettyFormatStrategy build() {
            if(this.logStrategy == null) {
                this.logStrategy = new LogcatLogStrategy();
            }

            return new PrettyFormatStrategy(this);
        }
    }
}