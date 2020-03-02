package com.dexer.dscript;
import java.lang.reflect.Array;
import java.util.ArrayList;
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
    public static Integer[] isBoolExpressionResult(String str){
        ArrayList<Integer> ar=new ArrayList<>();
        for(int i=1;i<str.length();i++){
            String temp=str.charAt(i-1)+""+str.charAt(i);

            if((temp.equals("&&")||temp.equals("||")||temp.equals("##")) &&
                    !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)) {
                ar.add(i);
            }
        }
        if(ar.size()!=0)
            return ar.toArray(new Integer[0]);
        else return null;
    }
    public static ParamIns getBoolExpressionResult(String str,int area_id,int layout_id){
        Integer[] ar;
        if((ar=isBoolExpressionResult(str))!=null) {
            String[] strs = new String[ar.length + 1];
            String[] syms = new String[ar.length];
            if(ar.length>=2) {
                int index = 0;
                for (int i : ar) {
                    index++;
                    String sym = str.charAt(i - 1) + "" + str.charAt(i);
                    syms[index-1] = sym;
                    strs[index] = str.substring(ar[index - 1] + 1, ar[index] - 1);
                    if (index == ar.length - 1) break;
                }
            }
            int last=ar[ar.length-1];
            syms[syms.length-1]=str.charAt(last-1) + "" + str.charAt(last);
            strs[0]=str.substring(0,ar[0]-1);
            strs[strs.length-1]=str.substring(last+1);
            //Second
            ParamIns[] pis=new ParamIns[strs.length];
            for (int i = 0; i < pis.length; i++)
                pis[i]=requireReturn(strs[i],area_id,layout_id);
            int ptr=0;
            while(ptr<syms.length){
                if(syms[ptr].equals("&&"))
                    pis[0]=bool_and(pis[0],pis[ptr+1]);
                if(syms[ptr].equals("||"))
                    pis[0]=bool_or(pis[0],pis[ptr+1]);
                if(syms[ptr].equals("##"))
                    pis[0]=bool_xor(pis[0],pis[ptr+1]);
                ptr++;
            }

            return pis[0];
        }
        return null;
    }
    public static String cleanBracket(String str){
        str=str.trim();
        if((str.charAt(0) + "" + str.charAt(str.length() - 1)).equals("()"))
            return str.substring(1,str.length()-1);
        else
            return str;
    }
    private static ParamIns bool_and(ParamIns A,ParamIns B){
        return new ParamIns("Boolean",Boolean.toString(Boolean.parseBoolean(A.value)&&Boolean.parseBoolean(B.value)));
    }
    private static ParamIns bool_or(ParamIns A,ParamIns B){
        return new ParamIns("Boolean",Boolean.toString(Boolean.parseBoolean(A.value)||Boolean.parseBoolean(B.value)));
    }
    private static ParamIns bool_xor(ParamIns A,ParamIns B){
        return new ParamIns("Boolean",Boolean.toString(Boolean.logicalXor(Boolean.parseBoolean(A.value),Boolean.parseBoolean(B.value))));
    }
}
