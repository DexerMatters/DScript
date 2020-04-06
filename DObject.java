package com.dexer.dscript;
import java.util.ArrayList;
import java.util.List;

import static com.dexer.dscript.DFunction.*;
import static com.dexer.dscript.DClass.*;
public class DObject {
    private static int count=0x100000;
    private String type;
    private ParamIns[] params;
    private DFunction[] funcs;
    private ArrayList<DFunction> constructor=new ArrayList<>();
    private DAttribute[] attrs;
    private String id;
    DObject(String type){
        this.type=type;
        ArrayList<DFunction> c=getClassByName(type).getConstructor();
        if(c!=null) {
            for(DFunction i : c){
                constructor.add(new DFunction(i.getName(), i.getParams(), i.getState(), i.getVisibility(), i.getCode(), i.isNative()));
            }

        }
            List<DFunction> list=new ArrayList<>();
            for(DFunction f:getClassByName(type).getAllFunctions()){
                list.add(new DFunction(f.getName(),f.getParams(),f.getState(),f.getVisibility(),f.getCode(),f.isNative()));
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
    public void reassignAttributeForce(String name,String value,String type,int area_id,int layout_id){
        for (int i = 0; i < attrs.length; i++) {
            if(attrs[i].getName().equals(name))
                attrs[i]=new DAttribute(name,new ParamIns(type,value),attrs[i].getState(),attrs[i].getVisibility());
        }
    }
    public ParamIns getAttribute(String name,int layout_id){
        for(DAttribute attr : attrs){
            if(attr.getName().equals(name)&&(attr.getVisibility()==layout_id||layout_id==PRIVATE))
                return attr.getVal();
        }
        return null;
    }
    public ParamIns runFunction(String name,ParamIns[] params,String id,int vis){
        for(DFunction func : funcs){
            if(func.getName().equals(name)&&func.getParams().length==params.length&&(func.getVisibility()==vis||vis==PRIVATE)){
                if(params.length!=0)
                    for (int i = 0; i < params.length; i++) {
                        if(params[i].type.equals(func.getParams()[i].type)||func.getParams()[i].type.equals("Object")) {
                            return func.run(params, id, vis);
                        }
                    }
                else return func.run(params, id, vis);
            }
        }
        return null;
    }
    public void runConstructor(ParamIns[] params,String id,int vis){
        if(constructor!=null) {
            for(DFunction c : constructor){
                if(c.getParams().length==params.length&&(vis==PRIVATE||c.getVisibility()==vis))
                    for(int i=0;i<params.length;i++){
                        if(params[i].type.equals(c.getParams()[i].type)){
                            c.run(params, id, vis);
                            return;
                        }
                    }
            }

        }
    }
}
