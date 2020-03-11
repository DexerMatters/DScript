package com.dexer.dscript;
import java.util.Arrays;

import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DTools.*;
import static com.dexer.dscript.DVariable.*;
public class DRuntime {
    public static DComplier comp;
    public static void solveKeyword(String code,String line,int index,int area_id,int layout_id){
        if(line.matches("^(if|unless)\\s*\\(.+\\)\\s*\\{.*}")) {
            ParamIns condition = requireReturn(line.substring(indexOf(line, '(') + 1, indexOf(line, ')')),area_id,layout_id);
            String flag=line.substring(0,indexOf(line, '('));
            String[] code_strs=getContentInBracket_(code,BRACLET_CURLY);
            DCode runs = new DCode(code_strs[0]);
            if (isTrue(condition) && flag.equals("if"))
                new DComplier(runs).compileWithoutPretreatment(0, 0);
            if (!isTrue(condition) && flag.equals("unless"))
                new DComplier(runs).compileWithoutPretreatment(0, 0);
            if(code_strs.length==2){
                DCode elses=new DCode(code_strs[1]);
                if (!isTrue(condition) && flag.equals("if"))
                    new DComplier(elses).compileWithoutPretreatment(0, 0);
                if (isTrue(condition) && flag.equals("unless"))
                    new DComplier(elses).compileWithoutPretreatment(0, 0);

            }
        }//for(Integer i=0 > 15)
        if(line.matches("^for\\s*\\(.+\\)\\s*\\{.*}")){
            String condition =line.substring(indexOf(line, '(') + 1, indexOf(line, ')'));
            String code_strs=getContentInBracket(code,BRACLET_CURLY);
            String[] conds=split(condition,"~");
            if(conds.length==2) {
                Variable v = importVariable(conds[0], area_id, layout_id);
                System.out.println(Arrays.toString(conds));
                int from = Integer.parseInt(v.value);
                int to = Integer.parseInt(requireReturn(conds[1],area_id,layout_id).value);

                for (int i = from; i <= to; i++) {
                    reassignToVal(v, v.type, Integer.toString(i), area_id, layout_id);
                    DCode runs = new DCode(code_strs);
                    new DComplier(runs).compileWithoutPretreatment(0, 0);
                }
                removeVariable(v);
            }else{
                conds=split(condition,";");
                Variable v = importVariable(conds[0], area_id, layout_id);
                ParamIns cond=requireReturn(conds[1],area_id,layout_id);

                while(DReference.isTrue(cond)){

                    DCode runs = new DCode(code_strs);
                    new DComplier(runs).compileWithoutPretreatment(0, 0);
                    DCode runs_ = new DCode(conds[2]+";");
                    new DComplier(runs_).compileWithoutPretreatment(0, 0);
                    cond=requireReturn(conds[1],area_id,layout_id);
                }
            }
        }
    }
}
