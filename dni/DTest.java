package com.dexer.dscript.dni;

import com.dexer.dscript.DClass;
import com.dexer.dscript.DFunction;
import com.dexer.dscript.DFunction.*;
import com.dexer.dscript.DObject;

import java.util.HashMap;
import java.util.Map;

import static com.dexer.dscript.DClass.getObjectById;

public class DTest{
    public static void load(){
        Map<String, DFunction.NativeCode> map=new HashMap();
        map.put("Demo$getInfo",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                System.out.println(123);
                return null;
            }
        });
        map.put("MyClass$insert",new DFunction.NativeCode(){
            @Override
            public DFunction.ParamIns run(DFunction.ParamIns[] pi, String id) {
                DObject obj=getObjectById(id);
                ParamIns ins=obj.getAttribute("str",DFunction.PUBLIC);
                StringBuilder sb=new StringBuilder(ins.value.substring(1,ins.value.length()-1));
                System.out.println(sb.insert(2,pi[0].value.substring(1,pi[0].value.length()-1)).toString());
                return null;
            }
        });
        DRegister.registerNativeMethods(map);
    }
}
