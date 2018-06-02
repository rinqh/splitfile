/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.splitfile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huylvq
 */
public class WriteFileRunnable implements Runnable {

    private BlockingQueue queue;
    private List<String> lines;
    private File output;
    private int index;

    public WriteFileRunnable() {
    }

    public WriteFileRunnable(BlockingQueue queue) {
        this.queue = queue;
    }

    public WriteFileRunnable(BlockingQueue queue, File output) {
        this.queue = queue;
        this.output = output;
    }

    public WriteFileRunnable(BlockingQueue queue, File output, int index) {
        this.queue = queue;
        this.output = output;
        this.index = index;
    }

    public WriteFileRunnable(BlockingQueue queue, List<String> lines, File output, int index) {
        this.queue = queue;
        this.lines = lines;
        this.output = output;
        this.index = index;
    }
    
    

    @Override
    public void run() {
        writeFile(lines, output);
        lines.clear();
        queue.add(index);
    }

    private void writeFile(List<String> lines, File outputFile) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFile));
            for (String s : lines) {
                writer.write(s);
                writer.newLine();
            }
        } catch (IOException ex) {
            Logger.getLogger(SplitFileUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            if (writer != null) {
                try {
                    writer.flush();
                    writer.close();
                } catch (IOException ex) {
                    Logger.getLogger(SplitFileUtil.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

}
