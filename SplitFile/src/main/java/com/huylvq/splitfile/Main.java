/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.huylvq.splitfile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author huylvq
 */
public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        SplitFileUtil util = new SplitFileUtil();
        try {
            util.readAndSplitFile("input.txt", "output.txt", 100 * 1024 * 1024);
        } catch (IOException | InterruptedException | ExecutionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
