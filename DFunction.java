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

        @Override
        public boolean equals(Object obj) {
            return this.value.equals(((ParamIns) obj).value);
        }
    }
    static class NativeCode{
        public ParamIns run(ParamIns[] pi){
            return null;
        };
    }
    private Param[] params;
    private int state;
    private NativeCode code_code=null;
    private String name;
    private int vis;
    public static final int STATIC=12;
    public static final int DYMASTIC=24;
    public static final int NATIVE=6;
    public static final int PUBLIC=1;
    public static final int PROTECTED=2;
    public static final int PRIVATE=3;
    //NATIVE STATIC 14
    //NATIVE DYNASTIC 30
    DFunction(String name,Param[] params, int state,int vis, Object code){
        this.params=params;
        this.state=state;
        this.name=name;
        this.vis=vis;
        if(state==6||state==14||state==30)
            this.code_code=(NativeCode) code;
        //else this.code_str=(String) code;

    }
    public ParamIns run(ParamIns[] ins,int vis){
        if(ins.length==params.length)
            for (int i = 0; i < ins.length; i++) {
                if(!(ins[i].type.equals(params[i].type)||params[i].type.equals("Object"))) {
                    return null;
                }
            }
        if(code_code!=null&&this.vis==vis)
            return code_code.run(ins);
        return null;
    }

    public String getName() {
        return name;
    }

    public Param[] getParams() {
        return params;
    }

    public int getState() {
        return state;
    }
}
