package com.dexer.dscript;

import java.lang.reflect.Method;

public class DFunction {
    static class Param{
        public String type;
        public String name;
    }
    static class ParamIns{
        public String type;
        public String name;
        public String value;
    }
    static abstract class NativeCode{
        public abstract String run(ParamIns pi);
    }
    private Param[] params;
    private String return_type;
    private int state;
    private NativeCode code_code=null;
    private String code_str=null;
    public static final int STATIC=12;
    public static final int DYMASTIC=24;
    public static final int NATIVE=6;
    //NATIVE STATIC 14
    //NATIVE DYNASTIC 30
    DFunction(String return_type, Param[] params, int state, Object code){
        this.params=params;
        this.return_type=return_type;
        this.state=state;
        if(state==6||state==14||state==30)
            this.code_code=(NativeCode) code;
        else this.code_str=(String) code;

    }
    public String run(ParamIns ins){
        if(code_code!=null)
            return code_code.run(ins);
        return code_str;
    }
    public Param[] getParams() {
        return params;
    }

    public String getReturnType() {
        return return_type;
    }

    public int getState() {
        return state;
    }
}
