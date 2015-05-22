/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.application;

import gumga.framework.core.GumgaValues;
import gumga.framework.domain.domains.GumgaFile;
import gumga.framework.domain.domains.GumgaImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GumgaTempFileService {
    
    @Autowired
    private GumgaValues gumgaValues;

    public String create(GumgaFile gumgaFile) {
        String tempFileName = "uploadData" + (System.currentTimeMillis() * 1000 + new Random().nextInt(1000));

        try { //TODO Arrumar o tratamento da Exception
            File folder = new File(gumgaValues.getUploadTempDir());
            folder.mkdirs();
            FileOutputStream fos = new FileOutputStream(gumgaValues.getUploadTempDir() + "/" + tempFileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(gumgaFile);
            oos.close();
            fos.close();
            return tempFileName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }

        public String delete(String fileName) {
        try { //TODO Arrumar o tratamento da Exception
            File file= new File(gumgaValues.getUploadTempDir() + "/" + fileName);
            file.delete();
            return "OK";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "error";
    }

    
    public GumgaFile find(String tempFileName) {
        try { //TODO Arrumar o tratamento da Exception
            FileInputStream fis = new FileInputStream(gumgaValues.getUploadTempDir() + "/" + tempFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            GumgaFile gf = (GumgaFile) ois.readObject();
            ois.close();
            fis.close();
            return gf;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
