/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedule;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author micha
 */
public class LogFile {
    private static Logger logger = Logger.getLogger("logger");
    private static FileHandler fh;
    
    public LogFile() throws IOException{
        fh = new FileHandler("LogFile.txt", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }
    public Logger getLogger(){
        return this.logger;
    }
}
