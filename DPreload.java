package com.dexer.dscript;
import javax.print.attribute.standard.MediaSize;

import static com.dexer.dscript.DClass.*;
import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DComplier.*;
import static com.dexer.dscript.DRes.*;
class DPreload {
    public static void load(){
        createClass("System");
        createClass("Thread");
        getClassByName("System").addFunction(new DFunction("output",
                new Param[]{new Param("Object","str")}, STATIC|NATIVE,PUBLIC,
                new NativeCode(){
                    @Override
                    public ParamIns run(ParamIns[] pi,String id) {
                        System.out.println(pi[0].value);
                        return null;
                    }
                }));
        getClassByName("System").addAttribute(new DAttribute("VERSION",new ParamIns("String$",VERSION),STATIC|NATIVE,PUBLIC));
        getClassByName("Thread").addAttribute(new DAttribute("name",new ParamIns("String","null"),DYMASTIC|NATIVE,PUBLIC));
        getClassByName("Thread").addFunction(new DFunction("constructor",
                new Param[]{new Param("String","name"),new Param("String","code")},DYMASTIC|NATIVE,PUBLIC,
                new NativeCode(){
                    @Override
                    public ParamIns run(ParamIns[] pi,String id) {
                        threadMap.put(pi[0].value,new Thread(new Runnable() {
                            @Override
                            public void run() {
                                new DComplier(new DCode(pi[1].value)).compile(0,0);
                            }
                        }));
                        getObjectById(id).reassignAttribute("name","\""+pi[0].value+"\"",0,0);
                        return null;
                    }
                }));
        getClassByName("Thread").addFunction(new DFunction("run",new Param[0],DYMASTIC|NATIVE,PUBLIC,
                new NativeCode(){
                    @Override
                    public ParamIns run(ParamIns[] pi, String id) {
                        String name=getObjectById(id).getAttribute("name",PUBLIC).value;
                        threadMap.get(name).start();
                        return null;
                    }
                }));
        //getClassByName("Thread").addFunction(new DFunction("run",);
    }
}
