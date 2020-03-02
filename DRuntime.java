package com.dexer.dscript;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
public class DRuntime {
    public static DComplier comp;
    public static int solveIf(String code,String line,int index,int area_id,int layout_id){
        if(line.matches("^(if|unless)\\s*\\(.+\\)\\s*\\{.*}")){
            String condition=line.substring(line.indexOf("(")+1,line.indexOf(")"));
            String runs="",elses="";
            int mode=0;
            int i;
            for(i=index-2;i>=0;i--){
                if(code.charAt(i)=='{'&&!hasCovered(code,i,BRACLET_CURLY)&&hasCovered(code,i,BRACLET_STRING)){
                    if(code.substring(i - 4, i).equals("else")){
                        mode=1;
                    }else
                        break;
                }
                if(mode==1) {
                    runs = code.charAt(i) + runs;
                }else
                    elses=code.charAt(i) + elses;

            }
            if(mode==1) runs=runs.substring(0,runs.length()-6);
            ParamIns pi= requireReturn(condition,area_id,layout_id);
            if(pi.value.equals("true")&& line.substring(0, 2).equals("if")){
                new DComplier(new DCode(runs)).compileWithoutPretreatment(0,0);
            }
            if(pi.value.equals("false")&& line.substring(0, 6).equals("unless")){
                new DComplier(new DCode(runs)).compileWithoutPretreatment(0,0);
            }
            if(mode==1){
                if(pi.value.equals("false")&& line.substring(0, 2).equals("if")){
                    new DComplier(new DCode(elses)).compileWithoutPretreatment(0,0);
                }
                if(pi.value.equals("true")&& line.substring(0, 6).equals("unless")){
                    new DComplier(new DCode(elses)).compileWithoutPretreatment(0,0);
                }
            }

            return i;
        }else
            return -1;
    }
}
