package com.dexer.dscript;

import java.lang.reflect.Method;

public class DFunction {
    static class Param{
        public String type;
        public String name;
        public Param(String type,String name){
            this.name=name;
            this.type=type;
        }
    }
    static class ParamIns{
        public String type;
        public String value;
        public ParamIns(String type,String value){
            this.value=value;
            this.type=type;
        }
    }
    static class NativeCode{
        public String run(ParamIns[] pi){
            return null;
        };
    }
    private Param[] params;
    private String return_type;
    private int state;
    private NativeCode code_code=null;
    private String code_str=null;
    private String name;
    public static final int STATIC=12;
    public static final int DYMASTIC=24;
    public static final int NATIVE=6;
    //NATIVE STATIC 14
    //NATIVE DYNASTIC 30
    DFunction(String name,String return_type, Param[] params, int state, Object code){
        this.params=params;
        this.return_type=return_type;
        this.state=state;
        this.name=name;
        if(state==6||state==14||state==30)
            this.code_code=(NativeCode) code;
        //else this.code_str=(String) code;

    }
    public String run(ParamIns[] ins){
        if(ins.length==params.length)
            for (int i = 0; i < ins.length; i++) {
                if(!ins[i].type.equals(params[i].type)) {
                    return null;
                }
            }
        if(code_code!=null)
            return code_code.run(ins);
        return code_str;
    }

    public String getName() {
        return name;
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
