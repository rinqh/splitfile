/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.splitfile;

import java.io.File;
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
            Future consumer1Status = threadPool.submit(new WriteFileRunnable("Write 1", queue));
            Future consumer2Status = threadPool.submit(new WriteFileRunnable("Write 2", queue));
            futures.add(producerStatus);
            futures.add(consumer1Status);
            futures.add(consumer2Status);

            for (Future<?> future : futures) {
                //System.err.println("future");
                future.get(); // get will block until the future is done
            }
            threadPool.shutdown();
            System.out.println(files.size());
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
