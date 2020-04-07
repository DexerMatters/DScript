package com.dexer.dscript.dni;

import com.dexer.dscript.DClass;
import com.dexer.dscript.DFunction;
import com.dexer.dscript.DObject;

import java.util.HashMap;
import java.util.Map;

import static com.dexer.dscript.DClass.getObjectById;
import static com.dexer.dscript.DFunction.PRIVATE;

public class DArray {
    public static void load(){
        Map<String, DFunction.NativeCode> map=new HashMap();
        map.put("Array$init",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                double length=Double.parseDouble(getObjectById(id).getAttribute("length",PRIVATE).value);
                StringBuilder sb=new StringBuilder();
                for(int i=0;i<length;i++){
                    sb.append("Null:null,");
                }
                getObjectById(id).reassignAttributeForce("arr",sb.toString().substring(0,sb.length()-1),"Native",0,PRIVATE);

                return null;
            }
        });
        map.put("Array$set",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                int index=(int)Double.parseDouble(pi[0].value);

                DFunction.ParamIns value= DClass.requireReturn(pi[1].value,1,PRIVATE);
                String arr=getObjectById(id).getAttribute("arr",PRIVATE).value;
                String[] values=arr.split(",");
                values[index]=value.type+":"+value.value;
                StringBuilder sb=new StringBuilder();
                for(String e:values)
                    sb.append(e+",");
                getObjectById(id).reassignAttributeForce("arr",sb.toString().substring(0,sb.length()-1),"Native",0,PRIVATE);

                return null;
            }
        });
        map.put("Array$get",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                int index=(int)Double.parseDouble(pi[0].value);
                String arr=getObjectById(id).getAttribute("arr",PRIVATE).value;
                String[] values=arr.split(",");
                String val=values[index].split(":")[1],
                        type=values[index].split(":")[0];
                return new DFunction.ParamIns(type,val);
            }
        });
        DRegister.registerNativeMethods(map);
    }
}
