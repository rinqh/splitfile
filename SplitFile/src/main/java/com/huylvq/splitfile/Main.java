/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.splitfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huylvq
 */
public class Main {

    public static void main(String[] args) {
        try {
            MyQueue queue = new MyQueue();
            ExecutorService threadPool = Executors.newFixedThreadPool(3);
            List<Future<?>> futures = new ArrayList<>();
            List<File> files = new ArrayList<>();
            int maxSize = 100 * 1024 * 1024;
            String input = "input.txt";
            Future producerStatus = threadPool.submit(new ReadFileRunnable(queue, input, files, maxSize));
            Future consumer1Status = threadPool.submit(new WriteFile("Write 1", queue));
            Future consumer2Status = threadPool.submit(new WriteFile("Write 2", queue));
            futures.add(producerStatus);
            futures.add(consumer1Status);
            futures.add(consumer2Status);

// this will wait for the producer to finish its execution.
//            MyData data = null;
//            while (true) {
//                data = queue.get();
//                if (data != null) {
//                    writeFile(data);
//                    data.lines.clear();
//                    data = null;
//                }
//                if (!queue.continueProducing && queue.isEmpty()) {
//                    break;
//                }
//            }
            for (Future<?> future : futures) {
                System.err.println("future");
                future.get(); // get will block until the future is done
            }
            threadPool.shutdown();
            System.out.println(files.size());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeFile(MyData data) {
        System.out.println(System.currentTimeMillis() + " start running!!");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(data.file));
            for (String s : data.lines) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(WriteFile.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(WriteFile.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println(System.currentTimeMillis() + " done!!");
    }
}
