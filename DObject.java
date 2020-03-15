package com.dexer.dscript;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DClass.*;
public class DObject {
    private static int count=0x100000;
    private String type;
    private ParamIns[] params;
    private DFunction[] funcs;
    private DAttribute[] attrs;
    private String id;
    DObject(String type){
        this.type=type;
        List<DFunction> list=new ArrayList<>();
        for(DFunction f:getClassByName(type).getAllFunctions()){
            list.add(new DFunction(f.getName(),f.getParams(),f.getState(),f.getVisibility(),f.getCode()));
        }
        funcs=list.toArray(new DFunction[0]);

        List<DAttribute> list_=new ArrayList<>();
        for(DAttribute a:getClassByName(type).getAllAttribute()){
            list_.add(new DAttribute(a.getName(),a.getVal(),a.getState(),a.getVisibility()));
        }
        attrs=list_.toArray(new DAttribute[0]);
        id="[$"+count+"$]";
        count++;
    }

    public String getType() {
        return type;
    }
    public String getId(){
        return id;
    }
    public void reassignAttribute(String name,String value,int area_id,int layout_id){
        for (int i = 0; i < attrs.length; i++) {
            String type=attrs[i].getVal().type;
            if(type.charAt(type.length()-1)!='$')
                if(attrs[i].getName().equals(name)){
                    attrs[i].setVal(requireReturn(value,area_id,layout_id));
                }
        }
    }
    public ParamIns getAttribute(String name,int vis){
        for(DAttribute attr : attrs){
            if(attr.getName().equals(name))
                return attr.getVal();
        }
        return null;
    }
    public ParamIns runFunction(String name,ParamIns[] params,String id,int vis){
        for(DFunction func : funcs){
            if(func.getName().equals(name))
                return func.run(params,id,vis);
        }
        return null;
    }
    public void runConstructor(ParamIns[] params,String id,int vis){
        for(DFunction func : funcs){
            if(func.getName().equals("constructor")) {
                func.run(params,id, vis);
            }
        }
    }
}
