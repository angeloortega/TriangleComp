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
import Triangle.tools.Triangle.AbstractSyntaxTrees.FormalParameter;
public class ActualParameterData {
    private String packageName;
    private String callerPackage;
    private FormalParameter FPS;
    
    public ActualParameterData(FormalParameter fps, String packageName ,String callerPackage){
        this.packageName = packageName;
        this.FPS = fps;
        this.callerPackage = callerPackage;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    public String getCallerPackage() {
        return callerPackage;
    }

    public void setCallerPackage(String packageName) {
        this.callerPackage = packageName;
    }

    public FormalParameter getFP() {
        return FPS;
    }

    public void setFP(FormalParameter fps) {
        this.FPS = fps;
    }
    
}
