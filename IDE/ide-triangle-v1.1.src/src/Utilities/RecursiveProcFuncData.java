/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

/**
 *
 * @author Angelo PC
 */
public class RecursiveProcFuncData {
    private String packageName;
    private Integer iteration;
    
    public RecursiveProcFuncData(Integer iteration, String packageName){
        this.packageName = packageName;
        this.iteration = iteration;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getIteration() {
        return iteration;
    }

    public void setIteration(Integer iteration) {
        this.iteration = iteration;
    }
    
}
