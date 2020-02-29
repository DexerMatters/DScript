package com.dexer.dscript;

import java.util.ArrayList;
import java.util.List;

import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DVariable.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DExpression.*;
public class DClass {
    private static ArrayList<Class> cls=new ArrayList<>();


    public static class Class {
        private String name;
        private ArrayList<DFunction> static_func=new ArrayList<>(),
                                    dymastic_func=new ArrayList<>();
        private Class(String name){
            this.name=name;
        }
        public void addFunction(DFunction function){
            if(function.getState()==DFunction.DYMASTIC||function.getState()==(DFunction.DYMASTIC|DFunction.NATIVE))
                dymastic_func.add(function);
            else
                static_func.add(function);
        }
        public ParamIns runFunction(String name, DFunction.ParamIns[] paramIns){
            for(DFunction func : static_func)
                if(func.getName().equals(name)) {
                    return func.run(paramIns);
                }
            return null;
        }

    }
    static ParamIns runFunction(String str,int layout_id,int area_id){
        if(str.matches("\\w+\\.\\w+\\(.*\\)")){
            String[] strs=str.split("\\.");
            int mode=0;
            String params="",name="",param="";
            ArrayList<ParamIns> pis=new ArrayList<>();
            for(int i=0;i<strs[1].length();i++){
                if(strs[1].charAt(i)==')'&&!hasCovered(str,i,BRACLET_STRING))
                    break;
                if(mode==1)
                    params+=strs[1].charAt(i);
                if(strs[1].charAt(i)=='(')
                    mode = 1;
                if(mode==0)
                    name+=strs[1].charAt(i);
            }
            for (int i = 0; i < params.length(); i++) {
                if(params.charAt(i)==','&&!hasCovered(str,i,BRACLET_STRING)){
                    pis.add(requireReturn(param,area_id,layout_id));
                    param="";
                }else {
                    //System.out.println(param);
                    param += params.charAt(i);
                }
                if(i==params.length()-1) {
                    pis.add(requireReturn(param, area_id, layout_id));

                }
            }
            //DComplier.debug(pis);
            ParamIns[] array = pis.toArray(new ParamIns[pis.size()]);
            return getClassByName("System").runFunction(name,array);

            //System.out.println(strs[1]+"|"+name+"|"+params);
        }
        return null;
    }
    static String getTypeOf(String str){
        if(str.matches("^\".*\"$"))
            return "String";
        if(str.matches("^-?[0-9]+$"))
            return "Integer";
        if(str.matches("^-?[0-9]+\\.[0-9]+$"))
            return "Float";
        if(str.matches("^(true|false)$"))
            return "Boolean";
        if(isEquation(str))
            return "equation";
        if(str.matches("^[a-zA-Z_]+$"))
            return "variable";
        return "Unknown";
    }
    static ParamIns requireReturn(String str, int area_id, int layout_id){
        switch (getTypeOf(str)){
            case "variable":
                Variable var= getVariableByName(str, area_id, layout_id);
                return new ParamIns(var.type,var.value);
            case "equation":
                return getEquationResult(str,area_id,layout_id);
        }

        return new ParamIns(getTypeOf(str),str);
    }
    static void createClass(String name){
        cls.add(new Class(name));
    }
    static Class getClassByName(String name){
        for (int i = 0; i < cls.size(); i++) {
            if(name.equals(cls.get(i).name)){
                return cls.get(i);
            }
        }
        return null;
    }
    static void importClass(){

    }
}
