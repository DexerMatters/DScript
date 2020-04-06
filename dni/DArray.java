package com.dexer.dscript.dni;

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
        DRegister.registerNativeMethods(map);
    }
}
