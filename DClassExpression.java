package com.dexer.dscript;
import com.dexer.dscript.dni.DRegister;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.dexer.dscript.DReference.*;
import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DExpression.*;
import static com.dexer.dscript.DRes.*;
import static com.dexer.dscript.DTools.*;
import static com.dexer.dscript.DFunction.*;
public class DClassExpression {
    static DClass solveClass(String line){

        String fore=split(line,"{")[0];
        String cont=getContentInBracket(line,BRACLET_CURLY);
        String keyword=line.substring(0,5);
        if(!keyword.equals("class")) return null;
        String[] temps=fore.split("\\s+");

        String name=temps[1];
        createClass(name);
        String[] pieces=split(cont,";");
        //pub var a=b
        //pub static var a=b
        //var a=b
        //pub static get(xx,xx){...}
        for(String p:pieces){
            p=p.trim();
            String code=getContentInBracket(p,BRACLET_CURLY);
            String[] words=split(p," ");

            int state=DYMASTIC;
            int vis=PRIVATE;
            boolean isN=false;
            for(String word : words){
                if(indexOfVis(word)!=-1)
                    vis=indexOfVis(word);
                else if(word.equals("static"))
                    state=STATIC;
                else if(word.equals("native"))
                    isN=true;
            }
            if(code==null){
                String[] vals=split(words[words.length-1],"=");
                getClassByName(name).addAttribute(
                        new DAttribute(vals[0].trim(),
                                requireReturn(vals[1], 0,0),
                                state,vis));
            }
            else {

                ArrayList<Param> pl = new ArrayList<>();
                String str = words[words.length - 1];
                String[] param_strs = split(getContentInBracket(str, BRACKET_NORMAL), ",");

                String name_ = str.substring(0, indexOf(str, '('));
                Param[] params;
                if (!getContentInBracket(str, BRACKET_NORMAL).equals("")) {
                    for (String s : param_strs) {
                        String[] temp = s.trim().split("\\s+");
                        pl.add(new Param(temp[0], temp[1]));
                    }
                    params = pl.toArray(new Param[0]);
                } else params = new Param[0];
                if (name_.equals(name)) {
                    getClassByName(name).addFunction(new DFunction("constructor", params, state, vis, code.trim(), false));
                } else {
                    if(!isN)
                        getClassByName(name).addFunction(new DFunction(name_, params, state, vis, code.trim(), false));
                    else
                        getClassByName(name).addFunction(new DFunction(name_, params, state, vis,DRegister.gain(name,name_), true));
                }
            }
        }
        return null;
    }
    static int indexOfVis(String str){
        switch (str.trim()){
            case "pub":
                return PUBLIC;
            case "pri":
                return PRIVATE;
            case "pro":
                return PROTECTED;
        }
        return -1;
    }
}
