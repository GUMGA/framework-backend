/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain;

/**
 *
 * @author munif
 */
public class GumgaObjectAndRevision {

    private GumgaRevisionEntity gumgaRevisionEntity;

    private Object object;

    public GumgaObjectAndRevision(GumgaRevisionEntity gumgaRevisionEntity, Object object) {
        this.gumgaRevisionEntity = gumgaRevisionEntity;
        this.object = object;
    }

    public GumgaRevisionEntity getGumgaRevisionEntity() {
        return gumgaRevisionEntity;
    }

    public void setGumgaRevisionEntity(GumgaRevisionEntity gumgaRevisionEntity) {
        this.gumgaRevisionEntity = gumgaRevisionEntity;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

}
