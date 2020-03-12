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
        if(leftV.type.charAt(leftV.type.length()-1)!='$')
            vars.get(indexOf(leftV)).value=rightV.value;
    }
    public static void reassignToVal(Variable leftV,String type,String value,int area_id,int layout_id){
        if(leftV.type.charAt(leftV.type.length()-1)!='$')
            vars.get(indexOf(leftV)).value=requireReturn(value,area_id,layout_id).value;
    }
    public static boolean isVaild(Variable v,int area_id,int layout_id,String name){
        return v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name);
    }
    public static String getTypeByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(isVaild(v,area_id,layout_id,name)){
                r=v.type;
            }
        }
        return r;
    }
    public static String getValueByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(isVaild(v,area_id,layout_id,name)){
                r=v.type;
            }
        }
        return r;
    }
    public static Variable getVariableByName(String name,int area_id,int layout_id){
        Variable r=null;
        for(Variable v : vars){
            if(isVaild(v,area_id,layout_id,name)){
                r=v;
            }
        }
        return r;
    }
    public static boolean hasCovered(String str,int pos,char[] bracket){
        
        //System.out.println(code);
            int left = 0, right = 0;
            for (int i = pos; i < str.length(); i++) {
                if (!Arrays.equals(bracket, BRACLET_STRING)) {
                    if (str.charAt(i) == bracket[1])
                        right++;
                    if (str.charAt(i) == bracket[0])
                        right--;
                } else if (str.charAt(i) == bracket[0])
                    right++;
            }
            for (int i = pos; i >= 0; i--) {
                if (!Arrays.equals(bracket, BRACLET_STRING)) {
                    if (str.charAt(i) == bracket[0])
                        left++;
                    if (str.charAt(i) == bracket[1])
                        left--;
                } else if (str.charAt(i) == bracket[0])
                    left++;
            }
            if (!Arrays.equals(bracket, BRACLET_STRING)) {
                return right == left && right + left != 0;
            } else return left % 2 != 0 || right % 2 != 0;
    }
    public static int getIndexCovered(String str,int pos,char[] bracket){

        //System.out.println(code);
        int left = 0, right = 0;
        for (int i = pos + 1; i < str.length(); i++) {
            if (str.charAt(i) == bracket[1])
                right++;
            if (str.charAt(i) == bracket[0])
                right--;
        }
        for (int i = pos - 1; i >= 0; i--) {
            if (str.charAt(i) == bracket[0])
                left++;
            if (str.charAt(i) == bracket[1])
                left--;
        }
        return Math.abs(left*right);
    }
    public static int indexOf(Variable var){
        int ptr=-1;
        for(Variable v : vars){
            ptr++;
            if(isVaild(v,var.area_id,var.layout_id,var.name))
                return ptr;
        }
        return ptr;
    }
    public static boolean isTrue(DFunction.ParamIns bool){
        return bool.value.equals("true");
    }

}
