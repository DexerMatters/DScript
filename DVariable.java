package com.dexer.dscript;

import java.util.ArrayList;
import java.util.Arrays;

import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DExpression.*;
public class DVariable{
    static class Variable{
        String type;
        String value;
        String name;
        int area_id;
        int layout_id;
    }

    static ArrayList<Variable> vars=new ArrayList<>();
    static Variable importVariable(String var_str, int area_id,int layout_id){
        Variable var=new Variable();
        var.area_id=area_id;
        var.layout_id=layout_id;
        if(var_str.matches("^(var|con)\\s+[\\W\\w_\\d]+\\s*=\\s*.+$")) {
            String[] strs = addBehind(var_str.split("="),"=");

            if (strs.length == 2) {
                DFunction.ParamIns pi=requireReturn(strs[1], area_id, layout_id);
                var.value = pi.value;
                String[] s = strs[0].split("\\s+");
                if (s.length == 2 && s[0].equals("var")) {
                    var.type = DClass.getTypeOf(pi.value);
                    var.name = s[1];
                    vars.add(var);
                }
                if(s.length==2&&s[0].equals("con")){
                    var.type=DClass.getTypeOf(requireReturn(strs[1], area_id, layout_id).value)+"$";
                    var.name=s[1];
                    vars.add(var);
                }
            }
        }
        return var;
    }
    static void assignVariable(String str, int area_id,int layout_id) {
        if (str.matches("^[\\W\\w_\\d.]+\\s*=\\s*.+$")) {
            String[] strs = addBehind(str.split("="), "=");
            if(strs[0].contains("var")) return;
            if (strs[0].indexOf('.') == -1) {
                if (getVariableByName(strs[1], area_id, layout_id) != null)
                    reassignToVar(getVariableByName(strs[0], area_id, layout_id), getVariableByName(strs[1], area_id, layout_id));
                else
                    reassignToVal(getVariableByName(strs[0], area_id, layout_id), DClass.getTypeOf(strs[1]), strs[1], area_id, layout_id);
            }else{
                String[] temp=strs[0].split("\\.");
                Variable v=getVariableByName(temp[0],0,0);
                if(v==null){
                    getClassByName(temp[0]).reassignAttribute(temp[1],requireReturn(strs[1],area_id,layout_id).value);
                }else
                    getObjectById(requireReturn(temp[0],area_id,layout_id).value).reassignAttribute(temp[1],requireReturn(strs[1],area_id,layout_id).value,area_id,layout_id);
            }
        }
    }
    static String assignVariableAs(String str,int area_id,int layout_id){
        if(str.matches("^.+\\s*[+\\-][+\\-]$")){
            String temp=cleanBracket(str.substring(0,str.length()-2).trim());
            String sym=str.substring(str.length()-2).trim();
            Variable v=getVariableByName(temp,area_id,layout_id);
            if(v.type.equals("Integer")) {
                int del= sym.equals("++")?1:-1;
                reassignToVal(v, "Integer", String.valueOf(Integer.parseInt(v.value) + del), area_id, layout_id);
                return getVariableByName(temp,area_id,layout_id).value;
            }
            else new DError(comp,"type");
        }
        if(str.matches("^[+\\-][+\\-]\\s*.+$")){
            String temp=str.substring(2).trim();
            String sym=str.substring(0,2).trim();
            Variable v=getVariableByName(temp,area_id,layout_id);
            if(v.type.equals("Integer")) {
                int del= sym.equals("++")?1:-1;
                String v_=v.value;
                reassignToVal(v, "Integer", String.valueOf(Integer.parseInt(v.value) + del), area_id, layout_id);
                return v_;
            }
            else new DError(comp,"type");
        }
        return null;
    }
    static void removeVariable(Variable v){
        vars.remove(v);
    }
    static Variable createVariable(String type,String name,String value,int area_id,int layout_id){
        Variable v=new Variable();
        v.name=name;
        v.type=type;
        v.value=value;
        v.area_id=area_id;
        v.layout_id=layout_id;
        return v;
    }
    static int indexOf(Variable var){
        int ptr=-1;
        for(Variable v : vars){
            ptr++;
            if(v.layout_id<=var.layout_id&&v.area_id==var.area_id&& v.name.equals(var.name))
                return ptr;
        }
        return ptr;
    }

}
