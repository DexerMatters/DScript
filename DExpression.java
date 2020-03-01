package com.dexer.dscript;
import java.util.Arrays;

import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DVariable.*;
public class DExpression {

    public static int isEquality(String str){
        for(int i=1;i<str.length();i++){
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)&&
                    str.charAt(i)=='='&&
                    str.charAt(i-1)=='='){
                return i-1;
            }
        }
        return -1;
    }
    public static int isInequality(String str){
        for(int i=1;i<str.length();i++){
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)&&
                    str.charAt(i)=='='&&
                    (str.charAt(i-1)=='<'||
                    str.charAt(i-1)=='>'||
                    str.charAt(i-1)=='!'
                    ))
                return i-1;
            if(     !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i,BRACLET_STRING)&&
                    (str.charAt(i)=='<'||
                    str.charAt(i)=='>')){
                return i;
            }
        }
        return -1;
    }
    public static ParamIns getEqualityResult(String str,int area_id,int layout_id){
        int index=isEquality(str);
        String[] strs={
                str.substring(0,index).trim(),
                str.substring(index+2).trim()
        };
        if(requireReturn(strs[0], layout_id, area_id).equals(requireReturn(strs[1], layout_id, area_id)))
            return new ParamIns("Boolean","true");
        else
            return new ParamIns("Boolean","false");
    }
    public static ParamIns getInequalityResult(String str,int area_id,int layout_id){
        int index=isInequality(str);
        String sym=str.charAt(index)+""+str.charAt(index+1);
        String[] strs;
        if(sym.charAt(1)=='=')
            strs= new String[]{
                    str.substring(0, index).trim(),
                    str.substring(index + 2).trim()
            };
        else
            strs= new String[]{
                    str.substring(0, index).trim(),
                    str.substring(index + 1).trim()
            };
        int A=Integer.parseInt(requireReturn(strs[0], layout_id, area_id).value);
        int B=Integer.parseInt(requireReturn(strs[1], layout_id, area_id).value);

        if(sym.equals("<="))
            return new ParamIns("Boolean",Boolean.toString(A<=B));
        else if(sym.equals(">="))
            return new ParamIns("Boolean",Boolean.toString(A>=B));
        else if(sym.equals("!="))
            return new ParamIns("Boolean",Boolean.toString(A!=B));
        else if(sym.charAt(0)=='<')
            return new ParamIns("Boolean",Boolean.toString(A<B));
        else if(sym.charAt(0)=='>')
            return new ParamIns("Boolean",Boolean.toString(A>B));
        return null;
    }
    public static ParamIns getExpressionResult(String str,int area_id,int layout_id){
        String value;
       if(str.matches("^!.+$")){
           String temp=cleanBracket(str.substring(1));

           ParamIns pi=requireReturn(temp,area_id,layout_id);
           if(pi.type.equals("Boolean")) {
               return new ParamIns("Boolean",Boolean.toString(!Boolean.parseBoolean(pi.value)));
           }else
               return null;
       }
       if((value=assignVariableAs(str,area_id,layout_id)) != null){
           return new ParamIns("Integer",value);
       }
       if(str.matches("^\\(\\w+\\)\\s*.+$")){
           String type_casted=str.substring(1,str.indexOf(")"));
           String temp=cleanBracket(str.substring(str.indexOf(")")+1).trim());
           String val=requireReturn(temp,area_id,layout_id).value;
           String type=requireReturn(temp,area_id,layout_id).type;
           if(type.equals("String"))
               return new ParamIns(type_casted,val.substring(1,val.length()-1));
           else
               return new ParamIns(type_casted,"\""+val+"\"");
       }

       return null;
    }
    public static ParamIns getBoolExpressionResult(String str,int area_id,int layout_id){
        return null;
    }
    public static String cleanBracket(String str){
        if((str.charAt(0) + "" + str.charAt(str.length() - 1)).equals("()"))
            return str.substring(1,str.length()-1);
        else
            return str;
    }

}
