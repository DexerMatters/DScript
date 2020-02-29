package com.dexer.dscript;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
public class DExpression {
    public static boolean isEquation(String str){
        for(int i=1;i<str.length();i++){
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)&&
                    str.charAt(i)=='='&&
                    str.charAt(i-1)=='='){
                return true;
            }
        }
        return false;
    }
    public static ParamIns getEquationResult(String str,int area_id,int layout_id){
        str="$"+str;
        String leftV="",rightV="";
        int mode=0;
        for(int i=1;i<str.length();i++){
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)&&
                    str.charAt(i)=='='&&
                    str.charAt(i-1)=='='){
                    i++;
                    mode=1;
            }
            if(mode==0){
                leftV+=str.charAt(i);
            }
            else{
                rightV+=str.charAt(i);
            }

        }
        leftV=leftV.split("=")[0];
        if(requireReturn(leftV, layout_id, area_id).equals(requireReturn(rightV, layout_id, area_id)))
            return new ParamIns("Boolean","true");
        else
            return new ParamIns("Boolean","false");
    }

}
