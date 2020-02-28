package com.dexer.dscript;

import java.util.ArrayList;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
public class DComplier implements DVariable,DReference{
    private DNode main_node=new DNode();
    private int times=0;
    DCode code;
    public DComplier(DCode code){
        this.code=code;
    }
    private <T> void debug(ArrayList<T> array){
        for(T a:array) {
            if (a instanceof Variable) {
                Variable v= (Variable) a;
                System.out.println(
                        "name:"+v.name+ ";\n"+
                        "type:"+v.type+";\n"+
                        "value:"+v.value);
            }
        }
    }
    public void compileWithoutPretreatment(){
        String code_str=code.getCode();
        String line="";
        createClass("System");
        getClassByName("System").addFunction(new DFunction("output","Void",
                new Param[]{new Param("String","str")}, STATIC|NATIVE,
                new DFunction.NativeCode(){
                    @Override
                    public String run(ParamIns[] pi) {
                        System.out.println(pi[0].value);
                        return "Void";
                    }
                }));
        getClassByName("System").runFunction("output",new ParamIns[]{new ParamIns("String","hello")});

        for (int i = 0; i < code_str.length(); i++) {
            if(code_str.charAt(i)==';'&&!hasCovered(i,BRACLET_STRING)){
                line=line.trim();
                importVariable(line,0,0);
                assignVariable(line,0,0);
                line="";
            }else
                line+=code_str.charAt(i);

        }
        //reassignToVal(getVariableByName("a",0,0),"String","4567");
        debug(vars);
    }
    public void onError(String message){
        System.out.println(message);
    }

    @Override
    public DComplier getCompiler() {
        return this;
    }
}
