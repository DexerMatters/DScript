package com.dexer.dscript;

import java.io.*;
import java.util.ArrayList;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DVariable.*;
import static com.dexer.dscript.DRuntime.*;
public class DComplier{
    public static final String VERSION="0.0.1 dev";
    public static final String INFO =
            "//////////////////\n" +
                    "   DScript "+VERSION+"\n" +
                    "   Copyright c Dexer Matters\n" +
                    "   Feedback:  https://github.com/DexerMatters/DScript/issues\n" +
                    "   Github:    https://github.com/DexerMatters/DScript\n" +
                    "   Contact:  2353708378@qq.com or DexerMatters@gmail.com"+
            "\n//////////////////";
    private boolean enabled=true;
    private File file;
    private static int ins_num=0;
    public static DCode code;
    public static DComplier complier=null;
    public DComplier(DCode code){
        ins_num++;
        DRuntime.comp_now=this;
        if(ins_num==1) {
            DRuntime.comp = this;
            complier = this;
        }
        this.code=code;
    }
    public DComplier(File file){
        ins_num++;
        DRuntime.comp_now=this;
        if(ins_num==1) {
            DRuntime.comp = this;
            complier = this;
        }
        this.file=file;
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
    public void preload(){
    }
    public void close(){
        enabled=false;
    }

    public void compile(int area_id,int layout_id){
        compile(area_id, layout_id,"$Math");
    }
    public void compile(int area_id,int layout_id,String clazz){
        String code_str=code.getCode();
        String line="";
        DPreload.load();
        preload();
        //getClassByName("System").runFunction("output",new ParamIns[]{new ParamIns("String","hello")});
        for (int i = 0; i < code_str.length(); i++) {

            if(code_str.charAt(i)==';'&&enabled){
                if(!(hasCovered(code_str,i,BRACLET_STRING)||hasCovered(code_str,i,BRACLET_CURLY)||hasCovered(code_str,i,BRACKET_NORMAL))) {
                    line = line.trim();
                    DClassExpression.solveClass(line);
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
    }

    public String getFileAbsolutePath(){
        return file.getAbsolutePath();
    }
    public void onError(String message){
        System.out.println(message);
    }
}
