package com.dexer.dscript;

import java.io.*;
import java.util.ArrayList;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DVariable.*;
import static com.dexer.dscript.DRuntime.*;
public class DComplier{
    private DNode main_node=new DNode();
    private int times=0;
    public static DCode code;
    public static int AREA_ID=0,LAYOUT_ID=0;
    public DComplier(DCode code){
        DRuntime.comp=this;
        this.code=code;
    }
    public DComplier(File file){
        StringBuffer sb=new StringBuffer();
        try {
            BufferedReader fis=new BufferedReader(new FileReader(file));
            String line;
            while((line=fis.readLine())!=null)
                sb.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        code=new DCode(sb.toString());
    }
    public static <T> void debug(ArrayList<T> array){
        for(T a:array) {
            if (a instanceof Variable) {
                Variable v= (Variable) a;
                System.out.println(
                        "var : name:"+v.name+ ";\n"+
                        "var : type:"+v.type+";\n"+
                        "var : value:"+v.value);
            }
            if(a instanceof ParamIns){
                ParamIns pi=(ParamIns) a;
                System.out.println(
                        "param : type:"+pi.type+";\n"+
                        "param : value:"+pi.value);
            }
        }
    }
    public void preLoad(){}
    public void compileWithoutPretreatment(int area_id,int layout_id){
        String code_str=code.getCode();
        String line="";
        preLoad();
        //getClassByName("System").runFunction("output",new ParamIns[]{new ParamIns("String","hello")});
        for (int i = 0; i < code_str.length(); i++) {

            if(code_str.charAt(i)==';'){
                if(!(hasCovered(code_str,i,BRACLET_STRING)||hasCovered(code_str,i,BRACLET_CURLY)||hasCovered(code_str,i,BRACKET_NORMAL))) {
                    line = line.trim();
                    importVariable(line, area_id, layout_id);
                    assignVariable(line, area_id, layout_id);
                    assignVariableAs(line, area_id, layout_id);
                    solveKeyword(code_str, line, i, area_id, layout_id);
                    runFunction(line, area_id, layout_id);
                    line = "";
                }else line+=code_str.charAt(i);
            }else
                line+=code_str.charAt(i);

        }
        //reassignToVal(getVariableByName("a",0,0),"String","4567");
        //debug(vars);
    }
    public void onError(String message){
        System.out.println(message);
    }
}
