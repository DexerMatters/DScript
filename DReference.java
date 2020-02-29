package com.dexer.dscript;
import java.util.Arrays;

import static com.dexer.dscript.DClass.requireReturn;
import static com.dexer.dscript.DVariable.*;
public class DReference{
    public static char[]
            BRACKET_SQUARE={'[','}'},
            BRACKET_NORMAL={'(',')'},
            BRACLET_STRING={'\"','\"'},
            BRACLET_CURLY={'{','}'};
    public static DComplier comp;
    public static void reassignToVar(Variable leftV, Variable rightV){
        vars.get(indexOf(leftV)).value=rightV.value;
    }
    public static void reassignToVal(Variable leftV,String type,String value){
        if(!leftV.type.equals(type)) {
            new DError(comp, "type");
            return;
        }
        vars.get(indexOf(leftV)).value=value;
    }
    public static String getTypeByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v.type;
            }
        }
        return r;
    }
    public static String getValueByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v.type;
            }
        }
        return r;
    }
    public static Variable getVariableByName(String name,int area_id,int layout_id){
        Variable r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v;
            }
        }
        return r;
    }
    public static boolean hasCovered(String str,int pos,char[] bracket){
        
        //System.out.println(code);
        int left=0,right=0;
        for (int i = pos; i < str.length(); i++) {
            if(!Arrays.equals(bracket, BRACLET_STRING)) {
                if (str.charAt(i) == bracket[1])
                    right++;
                if (str.charAt(i) == bracket[0])
                    right--;
            }else if(str.charAt(i) == bracket[0])
                right++;
        }
        for (int i = pos; i >= 0; i--) {
            if(!Arrays.equals(bracket, BRACLET_STRING)) {
                if (str.charAt(i) == bracket[0])
                    left++;
                if (str.charAt(i) == bracket[1])
                    left--;
            }else if(str.charAt(i) == bracket[0])
                left++;
        }

        if(!Arrays.equals(bracket, BRACLET_STRING)) {
            return right == left && right + left != 0;
        } else return left%2!=0||right%2!=0;
    }
    public static int indexOf(Variable var){
        int ptr=-1;
        for(Variable v : vars){
            ptr++;
            if(v.layout_id<=var.layout_id&&v.area_id==var.area_id&& v.name.equals(var.name))
                return ptr;
        }
        return ptr;
    }

}
