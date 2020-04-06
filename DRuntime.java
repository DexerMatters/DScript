package com.dexer.dscript;
import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DTools.*;
import static com.dexer.dscript.DVariable.*;
public class DRuntime {
    public static DComplier comp;
    public static HashMap<String,Thread> thrs=new HashMap<>();
    public static void solveKeyword(String code,String line,int index,int area_id,int layout_id){
        File file_=new File(comp.getFileAbsolutePath());
        if(line.matches("^(if|unless)\\s*\\(.+\\)\\s*\\{.*}")) {
            ParamIns condition = requireReturn(line.substring(indexOf(line, '(') + 1, indexOf(line, ')')),area_id,layout_id);
            String flag=line.substring(0,indexOf(line, '('));
            String[] code_strs=getContentInBracket_(line,BRACLET_CURLY);
            DCode runs = new DCode(code_strs[0]);
            if (flag.equals("if")&&isTrue(condition))
                new DComplier(runs).compile(area_id, layout_id);
            if (flag.equals("unless")&&!isTrue(condition))
                new DComplier(runs).compile(area_id, layout_id);
            if(code_strs.length==2){
                DCode elses=new DCode(code_strs[1]);
                if (flag.equals("if")&&!isTrue(condition))
                    new DComplier(elses).compile(area_id, layout_id);
                if (flag.equals("unless")&&isTrue(condition))
                    new DComplier(elses).compile(area_id, layout_id);

            }
        }//for(Integer i=0 > 15)
        if(line.matches("^for\\s*\\(.+\\)\\s*\\{.*}$")){
            String condition =line.substring(indexOf(line, '(') + 1, indexOf(line, ')'));
            String code_strs=getContentInBracket(line,BRACLET_CURLY);
            String[] conds=split(condition,"~");
            if(conds.length==2) {
                Variable v = importVariable(conds[0], area_id, layout_id);
                int from = Integer.parseInt(v.value);
                int to = Integer.parseInt(requireReturn(conds[1],area_id,layout_id).value);
                if(from<to) {
                    for (int i = from; i <= to; i++) {
                        reassignToVal(v, v.type, Integer.toString(i), area_id, layout_id);
                        DCode runs = new DCode(code_strs);
                        new DComplier(runs).compile(area_id, layout_id);
                    }
                }else{
                    for (int i = from; i >= to; i--) {
                        reassignToVal(v, v.type, Integer.toString(i), area_id, layout_id);
                        DCode runs = new DCode(code_strs);
                        new DComplier(runs).compile(area_id, layout_id);
                    }
                }
                removeVariable(v);
            }else{
                conds=split(condition,";");
                Variable v = importVariable(conds[0], area_id, layout_id);
                ParamIns cond=requireReturn(conds[1],area_id,layout_id);

                while(DReference.isTrue(cond)){

                    DCode runs = new DCode(code_strs);
                    new DComplier(runs).compile(area_id,layout_id);
                    DCode runs_ = new DCode(conds[2]+";");
                    new DComplier(runs_).compile(area_id,layout_id);
                    cond=requireReturn(conds[1],area_id,layout_id);
                }
                removeVariable(v);
            }
        }
        if(line.matches("^async\\s*\\(.+\\)\\s*\\{.*}$")){
            String name =line.substring(indexOf(line, '(') + 1, indexOf(line, ')'));
            String code_strs=getContentInBracket(line,BRACLET_CURLY);
            importVariable("var "+name+"="+"new Thread(\""+name+"\",\""+code_strs+"\")",area_id,layout_id);
        }
        if(line.matches("^rm\\s+[a-zA-Z_]+$")){
            String name =line.split("\\s+")[1];
            removeVariable(getVariableByName(name,area_id,layout_id));
        }
        if(line.matches("^ret\\s+.+$")){
            String val =line.split("\\s+")[1];
            importVariable("var __return="+val,1,layout_id);
            comp.close();
        }
        if(line.matches("^imp\\s+.+$")){
            String val =line.split("\\s+")[1];

            //System.out.println(comp.getFileAbsolutePath());
            File file=new File(file_.getParentFile().getAbsoluteFile()+File.separator+val);
            new DComplier(file).compile(0,0);
        }
        if(line.matches("^imps\\s+.+$")){
            String val =line.split("\\s+")[1];
            File file=new File(file_.getParentFile().getAbsoluteFile()+File.separator+val);
            File[] files=file.listFiles();
            for(File f : files){
                new DComplier(f).compile(0,0);
            }
        }
    }
}
