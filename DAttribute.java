package com.dexer.dscript;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
public class DAttribute {
    public static final int STATIC=12;
    public static final int DYMASTIC=24;
    public static final int NATIVE=6;
    public static final int PUBLIC=1;
    public static final int PROTECTED=2;
    public static final int PRIVATE=3;
    private String name;
    private int vis;
    private int state;
    private ParamIns val;
    //NATIVE STATIC 14
    //NATIVE DYNASTIC 30
    DAttribute(String name,ParamIns val, int state,int vis){
        this.state=state;
        this.name=name;
        this.vis=vis;
        this.val=val;
    }

    public ParamIns getVal() {
        return val;
    }

    public String getName() {
        return name;
    }
    public int getState(){
        return state;
    }
    public void setVal(ParamIns val) {
        this.val = val;
    }

    public void setName(String name) {
        this.name = name;
    }
}
