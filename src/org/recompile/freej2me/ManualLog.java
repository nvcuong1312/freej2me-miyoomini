package org.recompile.freej2me;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class ManualLog {

    private static PrintWriter out;
    private static String currentDate;

    public static void init() {
        if (out != null) return;

        try {
            currentDate = today();

            File baseDir = getExecutableDir();
            File logDir = new File(baseDir, "logs");
            logDir.mkdirs();

            File logFile = new File(logDir, "freej2me-" + currentDate + ".log");

            out = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream(logFile, true),
                    "UTF-8"
                )
            );

            write("=== Log started ===");
            write("BaseDir=" + baseDir.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            out = null;
        }
    }

    public static void write(String msg) {
        if (out == null) return;

        try {
            rotateIfNeeded();
            out.println(time() + " " + msg);
            out.flush();
        } catch (Exception ignored) {}
    }

    private static void rotateIfNeeded() throws Exception {
        String today = today();
        if (!today.equals(currentDate)) {
            out.close();
            currentDate = today;

            File baseDir = getExecutableDir();
            File logDir = new File(baseDir, "logs");
            File logFile = new File(logDir, "freej2me-" + currentDate + ".log");

            out = new PrintWriter(
                new OutputStreamWriter(
                    new FileOutputStream(logFile, true),
                    "UTF-8"
                )
            );
        }
    }

    private static File getExecutableDir() {
        try {
            return new File(
                ManualLog.class
                    .getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
            ).getParentFile();
        } catch (Exception e) {
            return new File(".");
        }
    }

    private static String today() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private static String time() {
        return new SimpleDateFormat("HH:mm:ss.SSS").format(new Date());
    }
}
