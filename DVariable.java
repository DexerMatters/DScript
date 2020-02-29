package com.dexer.dscript;

import java.util.ArrayList;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
public interface DVariable{
    class Variable{
        String type;
        String value;
        String name;
        int area_id;
        int layout_id;
    }
    private String[] addBehind(String[] str){
        String s="";
        for(int i=1;i<str.length;i++)
            s+=str[i]+"=";
        //System.out.println("\\"+s);
        return new String[]{str[0].trim(),s.substring(0,s.length()-1).trim()};
    }
    ArrayList<Variable> vars=new ArrayList<>();
    default void importVariable(String var_str, int area_id,int layout_id){
        Variable var=new Variable();
        var.area_id=area_id;
        var.layout_id=layout_id;
        if(var_str.matches("^var\\s+[\\W\\w_\\d]+\\s*=\\s*.+$")) {
            String[] strs = addBehind(var_str.split("="));

            if (strs.length == 2) {
                var.value = requireReturn(strs[1], area_id, layout_id).value;
                String[] s = strs[0].split("\\s+");
                if (s.length == 2 && s[0].equals("var")) {
                    var.type = DClass.getTypeOf(requireReturn(strs[1], area_id, layout_id).value);
                    var.name = s[1];
                    vars.add(var);
                }
            }
        }
    }
    default void assignVariable(String str, int area_id,int layout_id) {
        if (str.matches("^[\\W\\w_\\d]+\\s*=\\s*.+$")) {
            String[] strs = addBehind(str.split("="));

            if (strs.length != 2 || !strs[0].matches("[\\w\\W]"))
                return;
            if (getVariableByName(strs[1], area_id, layout_id) != null)
                reassignToVar(getVariableByName(strs[0], area_id, layout_id), getVariableByName(strs[1], area_id, layout_id));
            else
                reassignToVal(getVariableByName(strs[0], area_id, layout_id), DClass.getTypeOf(strs[1]), strs[1]);
        }
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
