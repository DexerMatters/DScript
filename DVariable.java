package com.dexer.dscript;

import java.util.ArrayList;

public interface DVariable extends DReference{
    class Variable{
        String type;
        String value;
        String name;
        int area_id;
        int layout_id;
    }
    ArrayList<Variable> vars=new ArrayList<>();
    default void importVariable(String var_str, int area_id,int layout_id){
        Variable var=new Variable();
        var.area_id=area_id;
        var.layout_id=layout_id;
        String[] strs=var_str.split("\\s*=\\s*");
        if(strs.length==2) {
            var.value = strs[1];
            String[] s=strs[0].split("\\s+");
            if(s.length==2&&s[0].equals("var")) {
                var.type = DClass.getTypeOf(strs[1]);
                var.name = s[1];
                vars.add(var);
            }
        }
    }
    default void assignVariable(String str, int area_id,int layout_id){
        String[] strs=str.split("\\s*=\\s*");
        if(strs.length!=2||!strs[0].matches("[\\w\\W]"))
            return;
        if(getVariableByName(strs[1],area_id,layout_id)!=null)
            reassignToVar(getVariableByName(strs[0],area_id,layout_id),getVariableByName(strs[1],area_id,layout_id));
        else
            reassignToVal(getVariableByName(strs[0],area_id,layout_id),DClass.getTypeOf(strs[1]),strs[1]);
    }
    default Variable createVariable(String type,String name,String value,int area_id,int layout_id){
        Variable v=new Variable();
        v.name=name;
        v.type=type;
        v.value=value;
        v.area_id=area_id;
        v.layout_id=layout_id;
        return v;
    }
    default int indexOf(Variable var){
        int ptr=-1;
        for(Variable v : vars){
            ptr++;
            if(v.layout_id<=var.layout_id&&v.area_id==var.area_id&& v.name.equals(var.name))
                return ptr;
        }
        return ptr;
    }


}
