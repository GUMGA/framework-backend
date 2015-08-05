/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import gumga.framework.core.GumgaValues;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaLoggerService {
    
    private static final Logger log=LoggerFactory.getLogger(GumgaLogService.class);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Autowired
    private GumgaValues gumgaValues;

    public void logToFile(String msg, int level) {
        try {
            String line = createLogLine(msg, level);
            File folder = new File(gumgaValues.getLogDir());
            folder.mkdirs();
            File log = new File(folder, "gumgalog.txt");
            FileWriter fw = new FileWriter(log, true);
            fw.write(line);
            fw.close();
        } catch (IOException ex) {
            log.error("Problema ao loggar no arquivo",ex);
        }
    }

    public String createLogLine(String msg, int level) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[level];
        String line = sdf.format(new Date()) + " " + stackTraceElement.getClassName() + " " + stackTraceElement.getMethodName() + " " + stackTraceElement.getLineNumber() + " " + msg + "\n";
        return line;

    }
}
