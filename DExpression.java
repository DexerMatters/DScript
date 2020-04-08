package com.dexer.dscript;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DTools.getContentInBracket;
import static com.dexer.dscript.DTools.getContentInBracket_;
import static com.dexer.dscript.DTools.split;
import static com.dexer.dscript.DVariable.*;
public class DExpression {

    public static int isEquality(String str){
        int r=-1;
        for(int i=1;i<str.length();i++){
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i-1,BRACLET_STRING)&&
                    !hasCovered(str,i-1,BRACKET_NORMAL)&&
                    str.charAt(i)=='='&&
                    str.charAt(i-1)=='=')
                r=i-1;
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    str.charAt(i)=='?')
                r=-1;
        }
        return r;
    }
    public static int isInequality(String str){
        int r=-1;
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
                r=i-1;
            if(     !hasCovered(str,i,BRACKET_NORMAL)&&
                    !hasCovered(str,i,BRACLET_STRING)&&
                    (str.charAt(i)=='<'||
                    str.charAt(i)=='>'))
                r=i;
            if(     !hasCovered(str,i,BRACLET_STRING)&&
                    !hasCovered(str,i,BRACKET_NORMAL)&&
                    str.charAt(i)=='?')
                r=-1;
        }
        return r;
    }
    public static ParamIns getEqualityResult(String str,int area_id,int layout_id){
        int index=isEquality(str);
        String[] strs={
                str.substring(0,index).trim(),
                str.substring(index+2).trim()
        };
        if(requireReturn(strs[0], layout_id, area_id).equals(requireReturn(strs[1], layout_id, area_id)))
            return new ParamIns("bool","true");
        else
            return new ParamIns("bool","false");
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
            return new ParamIns("bool",Boolean.toString(A<=B));
        else if(sym.equals(">="))
            return new ParamIns("bool",Boolean.toString(A>=B));
        else if(sym.equals("!="))
            return new ParamIns("bool",Boolean.toString(A!=B));
        else if(sym.charAt(0)=='<')
            return new ParamIns("bool",Boolean.toString(A<B));
        else if(sym.charAt(0)=='>')
            return new ParamIns("bool",Boolean.toString(A>B));
        return null;
    }
    public static ParamIns getExpressionResult(String str,int area_id,int layout_id){
        String value;
       if(str.matches("^!.+$")){
           String temp=cleanBracket(str.substring(1));

           ParamIns pi=requireReturn(temp,area_id,layout_id);
           if(pi.type.equals("bool")) {
               return new ParamIns("bool",Boolean.toString(!Boolean.parseBoolean(pi.value)));
           }else
               return null;
       }
       else if((value=assignVariableAs(str,area_id,layout_id)) != null){
           return new ParamIns("num",value);
       }
//       if(str.matches("^\\(\\w+\\)\\s*.+$")){
//           String type_casted=str.substring(1,str.indexOf(")"));
//           String temp=cleanBracket(str.substring(str.indexOf(")")+1).trim());
//           String val=requireReturn(temp,area_id,layout_id).value;
//           String type=requireReturn(temp,area_id,layout_id).type;
//           if(type.equals("String"))
//               return new ParamIns(type_casted,val.substring(1,val.length()-1));
//           else
//               return new ParamIns(type_casted,"\""+val+"\"");
//       }
       else if(str.matches("^.+\\?.+(:.+)?$")){
            int mode=0;
            String condition="",
                    iftrue="",
                    iffalse="";
            for(int i=0;i<str.length();i++){
                if(mode==0)
                    condition+=str.charAt(i);
                if(mode==1)
                    iftrue+=str.charAt(i);
                if(mode==2)
                    iffalse+=str.charAt(i);
                if(str.charAt(i)=='?'&&!hasCovered(str,i,BRACLET_STRING))
                    mode=1;
                if(str.charAt(i)==':'&&!hasCovered(str,i,BRACLET_STRING))
                    mode=2;
            }
            condition=condition.substring(0,condition.length()-1);
            if(mode==2) iftrue=iftrue.substring(0,iftrue.length()-1);
            boolean re=Boolean.parseBoolean(requireReturn(condition,area_id,layout_id).value);
            if(re) return requireReturn(iftrue,area_id,layout_id);
            if(!re){
                if(mode==2) return requireReturn(iffalse,area_id,layout_id);
                if(mode==1)
                    if(isEquality(condition)!=-1) {
                        return requireReturn(condition.substring(0, isEquality(condition)), area_id, layout_id);
                    }
                    if(isInequality(condition)!=-1)
                        return requireReturn(condition.substring(0,isInequality(condition)),area_id,layout_id);
            }
       }
       else if(str.matches("^.+\\[.+]$")){
            String[] v=anaylzeArrayGetter(str,area_id,layout_id);
            return getObjectById(v[0]).runFunction("get",new ParamIns[]{new ParamIns("num",v[1])},v[0],PUBLIC);
       }
       else return DMathExpression.solveMathExpression(str.trim(),area_id,layout_id);
       return null;
    }
    public static String[] anaylzeArrayGetter(String str,int area_id,int layout_id){
        String[] valStrs=getContentInBracket_(str,new char[]{'[',']'});
        String valStr=valStrs[valStrs.length-1];
        String left=requireReturn(str.substring(0,str.length()-valStr.length()-2),area_id,layout_id).value;
        return new String[]{left,valStr};
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
    public static ParamIns getArrayExpressionResult(String str,int area_id,int layout_id){
        String[] s=split(str.substring(1,str.length()-1),",");
        ParamIns[] ps=new ParamIns[s.length];
        for(int i=0;i<s.length;i++)
            ps[i]=requireReturn(s[i],area_id,layout_id);
        String id=getClassByName("Array").newInstance(new ParamIns[]{new ParamIns("num",s.length+"")},layout_id);
        for(int i=0;i<s.length;i++)
            getObjectById(id).runFunction("set"
                    ,new ParamIns[]{new ParamIns("num",i+"")
                            ,ps[i]},id,layout_id);
        return new ParamIns("Array",id);
    }
    public static String removeNoteExpressions(String str){
        /*String[] strs=str.split("");
        String temp="";
        int mode=0;
        for (int i = 0; i < strs.length; i++) {
            if(strs[i]+strs[i-1]==)
        }*/
        return null;
    }
    public static String cleanBracket(String str){
        str=str.trim();
        if(str.length()>2) {
            if ((str.charAt(0) + "" + str.charAt(str.length() - 1)).equals("()"))
                return str.substring(1, str.length() - 1);
            else
                return str;
        }else
            return str;
    }
    private static ParamIns bool_and(ParamIns A,ParamIns B){
        return new ParamIns("bool",Boolean.toString(Boolean.parseBoolean(A.value)&&Boolean.parseBoolean(B.value)));
    }
    private static ParamIns bool_or(ParamIns A,ParamIns B){
        return new ParamIns("bool",Boolean.toString(Boolean.parseBoolean(A.value)||Boolean.parseBoolean(B.value)));
    }
    private static ParamIns bool_xor(ParamIns A,ParamIns B){
        return new ParamIns("bool",Boolean.toString(Boolean.logicalXor(Boolean.parseBoolean(A.value),Boolean.parseBoolean(B.value))));
    }
}
