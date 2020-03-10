package com.dexer.dscript;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DTools.*;
public class DRuntime {
    public static DComplier comp;
    public static void solveIf(String code,String line,int index,int area_id,int layout_id){
        if(line.matches("^(if|unless)\\s*\\(.+\\)\\s*\\{.*}")) {
            ParamIns condition = requireReturn(line.substring(indexOf(line, '(') + 1, indexOf(line, ')')),area_id,layout_id);
            String flag=line.substring(0,indexOf(line, '('));
            DCode runs = new DCode(getContentInBracket(code,BRACLET_CURLY));
            if(isTrue(condition)&&flag.equals("if"))
                new DComplier(runs).compileWithoutPretreatment(0,0);
            if(!isTrue(condition)&&flag.equals("unless"))
                new DComplier(runs).compileWithoutPretreatment(0,0);
        }
        else if(line.matches("^(if|unless)\\s*\\(.+\\)\\s*\\{.*}\\s*else\\s*\\{.*}")){
        }
    }
}
