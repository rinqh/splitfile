/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.splitfile;

import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author huylvq
 */
public class SplitFileUtil {

    String inputFile;
    String outputFile;

    SplitFileUtil() {
    }

    SplitFileUtil(String in, String out) {
        this.inputFile = in;
        this.outputFile = out;
    }

    List<File> readAndSplitFile(String input, String output, int maxSizeTempFile) throws FileNotFoundException, IOException, InterruptedException, ExecutionException {
        List<File> tempFiles = new ArrayList<>();
        int currentSize = 0;
        BufferedReader br = null;
        br = new BufferedReader(new FileReader(input));

        ////
        ExecutorService executor = Executors.newFixedThreadPool(5);
        List<Future<?>> futures = new ArrayList<>();
        //BlockingQueue<List<String>> queueList = new LinkedBlockingDeque<>(5);
        int index = 0;
        BlockingQueue<Integer> queueIndex = new LinkedBlockingDeque<>(5);
        //BlockingQueue<Future> queueFuture = new LinkedBlockingDeque<>(5);
        List<List<String>> lines = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            lines.add(new ArrayList<>());
            queueIndex.add(i);
        }
        String line = null;
        //List<String> lines = new ArrayList<>();
        index = queueIndex.take();
        while ((line = br.readLine()) != null) {
            lines.get(index).add(line);
            currentSize += line.length() + 1;
            if (currentSize >= maxSizeTempFile) {
                System.out.println("quuee " + queueIndex.size());
                currentSize = 0;
                Collections.sort(lines.get(index));
                File file = new File("tmp" + System.currentTimeMillis());
                tempFiles.add(file);
                //queueList.add(lines);
                WriteFileRunnable run = new WriteFileRunnable(queueIndex, lines.get(index), file, index);
                Future f = executor.submit(run);
                futures.add(f);
                //lines = queueList.take();
                index = queueIndex.take();
                //lines = new ArrayList<>();
            }
        }

        if (!lines.isEmpty()) {
            //System.out.println(lines);
            Collections.sort(lines.get(index));
            File file = new File("tmp" + System.currentTimeMillis());
            tempFiles.add(file);
            WriteFileRunnable run = new WriteFileRunnable(queueIndex, lines.get(index), file, index);
            Future f = executor.submit(run);
            futures.add(f);
            //writeFile(lines, file);
            lines.clear();
        }

        for (Future<?> future : futures) {
            System.err.println("future");
            future.get(); // get will block until the future is done
        }
        executor.shutdown();
        return tempFiles;
    }
}
