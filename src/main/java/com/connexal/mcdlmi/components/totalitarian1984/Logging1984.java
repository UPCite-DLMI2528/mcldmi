package com.connexal.mcdlmi.components.totalitarian1984;

import com.connexal.mcdlmi.MCDLMI;
import com.connexal.mcdlmi.components.totalitarian1984.utils.LogEntry;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

public class Logging1984 {
    private static final long MAX_LOG_AGE = 10 * 60 * 1000; // 10 minutes

    private long lastLogTime = 0;
    private final Lock queueLock = new ReentrantLock();
    private final Queue<LogEntry> logEntryQueue = new LinkedList<>();

    private final Path logFolder;
    private final Lock writeLock = new ReentrantLock();

    public Logging1984(Path logFolder) {
        this.logFolder = logFolder;
    }

    private void flush(boolean force) {
        if (!force) {
            queueLock.lock();
            long last = lastLogTime;
            int size = logEntryQueue.size();
            queueLock.unlock();

            if (size < 500 && System.currentTimeMillis() - last < MAX_LOG_AGE) {
                return;
            }
        }

        queueLock.lock();
        lastLogTime = System.currentTimeMillis();

        Map<Path, List<String>> logDataMap = new HashMap<>();
        try {
            for (LogEntry entry : logEntryQueue) {
                Path path = logFolder.resolve(entry.date()).resolve(entry.uuid().toString()).resolve(entry.category() + ".log");
                logDataMap.computeIfAbsent(path, k -> new ArrayList<>()).add("[" + entry.time() + "] " + entry.message() + "\n");
            }
            logEntryQueue.clear();
        } catch (Exception e) {
            MCDLMI.getLog().log(Level.SEVERE, "Failed to process log entries", e);
        } finally {
            queueLock.unlock();
        }

        writeLock.lock();
        try {
            for (Map.Entry<Path, List<String>> entry : logDataMap.entrySet()) {
                File file = entry.getKey().toFile();
                List<String> lines = entry.getValue();

                // Check parent folder exists, if not create it
                File parentFolder = file.getParentFile();
                if (!parentFolder.exists() && !parentFolder.mkdirs()) {
                    throw new RuntimeException("Unable to create log folder: " + parentFolder.getAbsolutePath());
                }

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
                    for (String line : lines) {
                        writer.write(line);
                    }
                    writer.close();
                } catch (IOException e) {
                    MCDLMI.getLog().log(Level.SEVERE, "Failed to write log file: " + file.getAbsolutePath(), e);
                }
            }
        } catch (Exception e) {
            MCDLMI.getLog().log(Level.SEVERE, "Failed to write log entries to files", e);
        } finally {
            writeLock.unlock();
        }
    }

    public void flush() {
        MCDLMI.getLog().info("Flushing 1984 logs to disk");
        flush(true);
    }

    public void add(LogEntry entry) {
        MCDLMI.scheduleTask(() -> {
            logEntryQueue.add(entry);
            flush(false);
        });
    }
}
