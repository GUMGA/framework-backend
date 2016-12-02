/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gumga.framework.domain.shared;

/**
 *
 * @author munif
 */
public class MaximumSharesExceededException extends RuntimeException {

    public MaximumSharesExceededException(String msg) {
        super(msg);
    }
}
