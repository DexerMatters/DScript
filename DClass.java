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
import static com.dexer.dscript.DRes.*;
public class DClass{

    public static class Class {
        private String name;
        private ArrayList<DFunction> static_func=new ArrayList<>(),
                                    dymastic_func=new ArrayList<>();
        private ArrayList<DAttribute> static_attr=new ArrayList<>(),
                                    dymastic_attr=new ArrayList<>();
        private ArrayList<DFunction> constructor=new ArrayList<>();
        private Class(String name){
            this.name=name;
        }
        public String newInstance(ParamIns[] pi,int vis){
            DObject o=new DObject(name);
            objs.add(o);
            o.runConstructor(pi,o.getId(),vis);
            return o.getId();
        }


        public void addFunction(DFunction function){
            if(!function.getName().equals("constructor"))
                if(function.getState()==DFunction.DYMASTIC) {
                    dymastic_func.add(function);
                }else
                    static_func.add(function);
            else constructor.add(function);
        }
        public void addAttribute(DAttribute attribute){
            if(attribute.getState()==DAttribute.DYMASTIC||attribute.getState()==(DAttribute.DYMASTIC|DAttribute.NATIVE))
                dymastic_attr.add(attribute);
            else
                static_attr.add(attribute);
        }
        public ParamIns runStaticFunction(String name, DFunction.ParamIns[] params,String id,int vis){
            for(DFunction func :static_func){
                if(func.getName().equals(name)&&func.getParams().length==params.length&&(func.getVisibility()==vis||vis==PRIVATE)){
                    if(func.getParams().length!=0)
                        for (int i = 0; i < params.length; i++) {
                            if(params[i].type.equals(func.getParams()[i].type)||func.getParams()[i].type.equals("Object")) {
                                return func.run(params, id, vis);
                            }
                        }
                    else return func.run(params, id, vis);
                }
            }
            return null;
        }
        public DFunction[] getAllFunctions(){
            return dymastic_func.toArray(new DFunction[0]);
        }
        public DAttribute[] getAllAttribute(){
            return dymastic_attr.toArray(new DAttribute[0]);
        }
        public ArrayList<DFunction> getConstructor(){
            return constructor;
        }
        public ParamIns reassignAttribute(String name,String value){
            for (int i = 0; i < static_attr.size(); i++) {
                String type=static_attr.get(i).getVal().type;
                if(type.charAt(type.length()-1)!='$')
                    if(static_attr.get(i).getName().equals(name))
                        static_attr.get(i).setVal(requireReturn(value,0,0));
            }
            return null;
        }
        public ParamIns getAttribute(String name, int layout_id){
            for(DAttribute attr : static_attr) {
                if (attr.getName().equals(name)) {
                    if(layout_id==attr.getVisibility()||layout_id==PRIVATE)
                    return attr.getVal();
                }
            }
            return null;
        }

    }

    static ParamIns instanceClass(String str, int area_id, int layout_id){
        str=str.trim();
        String type=str.substring(indexOf(str,'w')+2,indexOf(str,'(')).trim();
        String temp=getContentInBracket(str,BRACKET_NORMAL);
        ArrayList<ParamIns> pis=new ArrayList<>();
        if(!temp.equals("")) {
            for (String s : split(temp, ","))
                pis.add(requireReturn(s.trim(), area_id, layout_id));
            return new ParamIns(type, getClassByName(type).newInstance(pis.toArray(new ParamIns[0]), layout_id));
        }
        else {
            return new ParamIns(type, getClassByName(type).newInstance(new ParamIns[0], layout_id));
        }
    }
    static ParamIns runFunction(String str,int area_id,int layout_id){

        if(str.matches("^.+\\.\\w+\\(.*\\)$")){
            String[] strs=split(str,".");
            strs=addFront(strs,".");
            if(!strs[0].matches("^[\\W\\w_]+$")) return null;
            String params;
            ArrayList<ParamIns> pis=new ArrayList<>();
            params=getContentInBracket_(str,BRACKET_NORMAL)[getContentInBracket_(str,BRACKET_NORMAL).length-1];
            ParamIns[] array;
            if(params.equals("")) array=new ParamIns[0];
            else {
                for (String s : split(params, ","))
                    pis.add(requireReturn(s.trim(), area_id, layout_id));
                array = pis.toArray(new ParamIns[0]);
            }
            DClass.Class v=getClassByName(strs[0]);
            if(v!=null)
                return getClassByName(strs[0]).runStaticFunction(strs[1].substring(0,indexOf(strs[1],'(')),array,"null",layout_id);
            else{
                DObject obj=getObjectById(requireReturn(strs[0],area_id,layout_id).value);
                return obj.runFunction(strs[1].substring(0,indexOf(strs[1],'(')),array,obj.getId(),layout_id);
            }
            //System.out.println(strs[1]+"|"+name+"|"+params);
        }
        return null;
    }
    public static ParamIns getAttribute(String str,int area_id,int layout_id){
        String[] temp=split(str,".");
        temp=addFront(temp,".");
        Class v=getClassByName(temp[0]);
        if(v!=null)
            return getClassByName(temp[0]).getAttribute(temp[1],layout_id);
        else {

            return getObjectById(requireReturn(temp[0], area_id, layout_id).value).getAttribute(temp[1],layout_id);
        }
    }
    static int index=0;
    public static String getTypeOf(String str){
        index=0;
        if(str.matches("^\".*\"$"))
            return "String";
        if(str.matches("^-?[0-9]+\\.*[0-9]+$"))
            return "Number";
        if(str.matches("^-?[0-9]+\\.[0-9]+$"))
            return "Float";
        if(str.matches("^(true|false)$"))
            return "Boolean";
        if(str.matches("^.+\\.[a-zA-Z_]+$"))
            return "attribute";

        if(str.matches("^\\{.+}$"))
            return "Array";
        if(str.matches("^\\[\\$.+\\$]$"))
            return "Object";
        if(str.matches("^new\\s+[a-zA-Z_]+\\s*\\(.*\\)$"))
            return "objectInstance";
        if(str.matches("^\\w+\\.\\w+\\(.*\\)$"))
            return "function";
        if(isBoolExpressionResult(str)!=null)
            return "bool_expression";
        if((index=isEquality(str))!=-1)
            return "equality";
        if((index=isInequality(str))!=-1)
            return "inequality";

        if(str.matches("^[a-zA-Z_]+$"))
            return "variable";
        return "expression";
    }
    public static String getObjTypeOf(String str){
        return getObjectById(str).getType();
    }
    public static ParamIns requireReturn(String str, int area_id, int layout_id){
        str=cleanBracket(str);

        switch (getTypeOf(str)){
            case "String":
                return new ParamIns(getTypeOf(str),str);
            case "Array":
                return getArrayExpressionResult(str,area_id,layout_id);
            case "Object":
                return new ParamIns(getObjectById(str).getType(),str);
            case "objectInstance":
                return instanceClass(str,area_id,layout_id);
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
                return getAttribute(str,area_id,layout_id);
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
    public static String[] addFront(String[] str,String insert){
        String s="";
        for(int i=0;i<str.length-1;i++)
            s+=str[i]+insert;
        //System.out.println("\\"+s);
        return new String[]{s.substring(0,s.length()-1).trim(),str[str.length-1].trim()};
    }
    static void createClass(String name){
        cls.add(new Class(name));
    }
    public static Class getClassByName(String name){
        for (int i = 0; i < cls.size(); i++) {
            if(name.equals(cls.get(i).name)){
                return cls.get(i);
            }
        }
        return null;
    }
    public static DObject getObjectById(String id){
        for (int i = 0; i < objs.size(); i++) {
            if(id.equals(objs.get(i).getId())){
                return objs.get(i);
            }
        }
        return null;
    }
    static void importClass(){

    }
}
