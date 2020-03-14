package com.dexer.dscript;
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
        funcs=getClassByName(type).getAllFunctions();
        attrs=getClassByName(type).getAllAttribute();
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
    public ParamIns runFunction(String name,ParamIns[] params,int vis){
        for(DFunction func : funcs){
            if(func.getName().equals(name))
                return func.run(params,vis);
        }
        return null;
    }
    public void runConstructor(ParamIns[] params,int vis){
        for(DFunction func : funcs){
            if(func.getName().equals("constructor")) {
                func.run(params, vis);
            }
        }
    }
}
