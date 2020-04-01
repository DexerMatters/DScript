package com.dexer.dscript;

import java.lang.ref.Reference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Queue;

import static com.dexer.dscript.DRes.vars;
import static com.dexer.dscript.DVariable.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
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
        public ParamIns run(ParamIns[] pi,String id){
            return null;
        };

    }
    private Param[] params;
    private int state;
    private NativeCode code_code;
    private String code_str;
    private Object code_obj;
    private String name;
    private int vis;
    private boolean isNative;
    public static final int STATIC=12;
    public static final int DYMASTIC=24;
    public static final int PUBLIC=1;
    public static final int PROTECTED=2;
    public static final int PRIVATE=3;
    //NATIVE STATIC 14
    //NATIVE DYNASTIC 30
    DFunction(String name,Param[] params, int state,int vis, Object code,boolean isNative){
        this.params=params;
        this.state=state;
        this.name=name;
        this.vis=vis;
        this.isNative=isNative;
        this.code_obj=code;
        if(isNative)
            this.code_code=(NativeCode) code_obj;
        else{
            code_str = (String) code_obj;
            this.code_code=new NativeCode(){
                @Override
                public ParamIns run(ParamIns[] pi, String id) {
                    DObject obj=getObjectById(id);
                    int index=0;
                    ArrayList<Variable> list=new ArrayList<>();
                    if(obj!=null) {
                        Variable v=createVariable(obj.getType() + "$", "self", obj.getId(), 1, 0);
                        vars.add(v);
                        list.add(v);
                    }
                    if(params.length!=0)
                        for(ParamIns i : pi) {
                            Variable v = createVariable(i.type, params[index].name, i.value, 1, 0);
                            vars.add(v);
                            list.add(v);
                            index++;
                        }
                    new DComplier(new DCode(code_str)).compile(1,0);
                    Variable returns = getVariableByName("%return%",1,0);
                    for(Variable a : list) removeVariable(a);
                    removeVariableByAreaId(1);
                    if(returns!=null)
                        return new ParamIns(returns.type,returns.value);
                    return new ParamIns("Null","null");
                }
            };
        }

    }
    public ParamIns run(ParamIns[] ins,String id,int vis){
            if (ins.length == params.length)
                for (int i = 0; i < ins.length; i++) {
                    if(ins[i]!=null)
                    if (!(ins[i].type.equals(params[i].type) || params[i].type.equals("Object"))) {
                        return null;
                    }
        }
        if (code_code != null && this.vis == vis)
            return code_code.run(ins,id);
        return null;
    }

    public int getVisibility() {
        return vis;
    }

    public Object getCode() {
        return code_obj;
    }

    public String getCodeString() {
        return code_str;
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
    public boolean isNative(){
        return isNative;
    }
}
