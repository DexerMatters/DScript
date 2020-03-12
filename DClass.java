package com.dexer.dscript;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DVariable.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DExpression.*;
import static com.dexer.dscript.DTools.*;
public class DClass {
    private static ArrayList<Class> cls=new ArrayList<>();


    public static class Class {
        private String name;
        private ArrayList<DFunction> static_func=new ArrayList<>(),
                                    dymastic_func=new ArrayList<>();
        private ArrayList<DAttribute> static_attr=new ArrayList<>(),
                                    dymastic_attr=new ArrayList<>();
        private Class(String name){
            this.name=name;
        }
        public void addFunction(DFunction function){
            if(function.getState()==DFunction.DYMASTIC||function.getState()==(DFunction.DYMASTIC|DFunction.NATIVE))
                dymastic_func.add(function);
            else
                static_func.add(function);
        }
        public void addAttribute(DAttribute attribute){
            if(attribute.getState()==DAttribute.DYMASTIC||attribute.getState()==(DAttribute.DYMASTIC|DAttribute.NATIVE))
                dymastic_attr.add(attribute);
            else
                static_attr.add(attribute);
        }
        public ParamIns runFunction(String name, DFunction.ParamIns[] paramIns,int vis){
            for(DFunction func : static_func) {
                if (func.getName().equals(name)) {
                    return func.run(paramIns,vis);
                }
            }
            return null;
        }
        public ParamIns getAttribute(String name){
            for(DAttribute attr : static_attr) {
                if (attr.getName().equals(name)) {
                    return attr.getVal();
                }
            }
            return null;
        }

    }
    static ParamIns runFunction(String str,int area_id,int layout_id){

        if(str.matches("^\\w+\\.\\w+\\(.*\\)$")){
            String[] strs=str.split("\\.");
            strs=addBehind(strs,".");
            int mode=0;
            String params="";
            ArrayList<ParamIns> pis=new ArrayList<>();
            params=getContentInBracket(str,BRACKET_NORMAL);
            for(String s : split(params,","))
                pis.add(requireReturn(s.trim(),area_id,layout_id));
            ParamIns[] array=pis.toArray(new ParamIns[0]);
            return getClassByName("System").runFunction(strs[1].substring(0,indexOf(strs[1],'(')),array,PUBLIC);

            //System.out.println(strs[1]+"|"+name+"|"+params);
        }
        return null;
    }
    static ParamIns getAttribute(String str){
        String[] temp=split(str,".");
        return getClassByName(temp[0]).getAttribute(temp[1]);

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
        if(isBoolExpressionResult(str)!=null)
            return "bool_expression";
        if((index=isEquality(str))!=-1)
            return "equality";
        if((index=isInequality(str))!=-1)
            return "inequality";
        if(str.matches("^[a-zA-Z_]+\\.[a-zA-Z_]+$"))
            return "attribute";
        if(str.matches("^[a-zA-Z_]+$"))
            return "variable";
        return "expression";
    }
    static ParamIns requireReturn(String str, int area_id, int layout_id){
        str=cleanBracket(str);
        switch (getTypeOf(str)){
            case "String":
                return new ParamIns(getTypeOf(str),str.substring(1,str.length()-1));
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
            case "attribute":
                return getAttribute(str);
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
