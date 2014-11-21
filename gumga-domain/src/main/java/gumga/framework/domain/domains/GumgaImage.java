/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.domains;

/**
 *
 * @author munif
 */
public class GumgaImage extends GumgaFile {

    public GumgaImage() {
    }

    public GumgaImage(String name, long size, String mimeType, byte[] bytes) {
        super(name, size, mimeType, bytes);
    }

    public GumgaImage(GumgaFile other) {
        super(other);
    }

}
