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
    static ParamIns runFunction(String str,int area_id,int layout_id){

        if(str.matches("^\\w+\\.\\w+\\(.*\\)$")){
            String[] strs=str.split("\\.");
            strs=addBehind(strs,".");
            int mode=0;
            String params="",name="",param="";
            ArrayList<ParamIns> pis=new ArrayList<>();
            for(int i=0;i<strs[1].length();i++){
                if(i==strs[1].length()-1) break;
                if(mode==1)
                    params+=strs[1].charAt(i);
                if(strs[1].charAt(i)=='('&&mode!=1)
                    mode = 1;
                if(mode==0)
                    name+=strs[1].charAt(i);
            }
            for (int i = 0; i < params.length(); i++) {
                if(params.charAt(i)==','&&!hasCovered(params,i,BRACLET_STRING)&&!hasCovered(params,i,BRACKET_NORMAL)){
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
    static int index=0;
    static String getTypeOf(String str){
        index=0;
        if(str.matches("^\".*\"$"))
            return "String";
        if(str.matches("^-?[0-9]+$"))
            return "Integer";
        if(str.matches("^-?[0-9]+\\.[0-9]+$"))
            return "Float";
        if(str.matches("^(true|false)$"))
            return "Boolean";
        if(str.matches("^\\w+\\.\\w+\\(.*\\)$"))
            return "function";
        if(str.matches("(.+&&.+|.+\\|\\|.+)"))
            return "bool_expression";
        if((index=isEquality(str))!=-1)
            return "equality";
        if((index=isInequality(str))!=-1)
            return "inequality";
        if(str.matches("^[a-zA-Z_]+$"))
            return "variable";
        return "expression";
    }
    static ParamIns requireReturn(String str, int area_id, int layout_id){
        switch (getTypeOf(str)){
            case "variable":
                Variable var= getVariableByName(str, area_id, layout_id);
                return new ParamIns(var.type,var.value);
            case "function":
                return runFunction(str,area_id,layout_id);
            case "bool_expression":
                return getBoolExpressionResult(str,area_id,layout_id);
            case "equality":
                return getEqualityResult(str,area_id,layout_id);
            case "inequality":
                return getInequalityResult(str,area_id,layout_id);
            case "expression":
                return getExpressionResult(str, area_id, layout_id);
        }

        return new ParamIns(getTypeOf(str),str);
    }
    public static String[] addBehind(String[] str,String insert){
        String s="";
        for(int i=1;i<str.length;i++)
            s+=str[i]+insert;
        //System.out.println("\\"+s);
        return new String[]{str[0].trim(),s.substring(0,s.length()-1).trim()};
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
