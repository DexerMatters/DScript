package com.dexer.dscript;

import java.util.ArrayList;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
public class DComplier implements DVariable{
    private DNode main_node=new DNode();
    private int times=0;
    DCode code;
    public DComplier(DCode code){
        comp=this;
        this.code=code;
    }
    public static <T> void debug(ArrayList<T> array){
        for(T a:array) {
            if (a instanceof Variable) {
                Variable v= (Variable) a;
                System.out.println(
                        "var : name:"+v.name+ ";\n"+
                        "var : type:"+v.type+";\n"+
                        "var : value:"+v.value);
            }
            if(a instanceof ParamIns){
                ParamIns pi=(ParamIns) a;
                System.out.println(
                        "param : type:"+pi.type+";\n"+
                        "param : value:"+pi.value);
            }
        }
    }
    public void compileWithoutPretreatment(){
        String code_str=code.getCode();
        String line="";
        createClass("System");
        getClassByName("System").addFunction(new DFunction("output",
                new Param[]{new Param("Object","str")}, STATIC|NATIVE,
                new DFunction.NativeCode(){
                    @Override
                    public ParamIns run(ParamIns[] pi) {
                        System.out.println(pi[0].value);
                        return new ParamIns("Void",null);
                    }
                }));
        //getClassByName("System").runFunction("output",new ParamIns[]{new ParamIns("String","hello")});

        for (int i = 0; i < code_str.length(); i++) {
            if(code_str.charAt(i)==';'&&!hasCovered(code_str,i,BRACLET_STRING)){
                line=line.trim();
                importVariable(line,0,0);
                assignVariable(line,0,0);
                runFunction(line,0,0);
                line="";
            }else
                line+=code_str.charAt(i);

        }
        //reassignToVal(getVariableByName("a",0,0),"String","4567");
        //debug(vars);
    }
    public void onError(String message){
        System.out.println(message);
    }
}
