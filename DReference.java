package com.dexer.dscript;
import java.util.Arrays;
import static com.dexer.dscript.DVariable.*;
public interface DReference{
    DComplier getCompiler();
    char[]
            BRACKET_SQUARE={'[','}'},
            BRACKET_NORMAL={'(',')'},
            BRACLET_STRING={'\"','\"'},
            BRACLET_CURLY={'{','}'};

    default void reassignToVar(Variable leftV, Variable rightV){
        vars.get(indexOf(leftV)).value=rightV.value;
    }
    default void reassignToVal(Variable leftV,String type,String value){
        if(!leftV.type.equals(type)) {
            new DError(getCompiler(), "type");
            return;
        }
        vars.get(indexOf(leftV)).value=value;
    }
    default String getTypeByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v.type;
            }
        }
        return r;
    }
    default String getValueByName(String name,int area_id,int layout_id){
        String r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v.type;
            }
        }
        return r;
    }
    default Variable getVariableByName(String name,int area_id,int layout_id){
        Variable r=null;
        for(Variable v : vars){
            if(v.area_id==area_id&&v.layout_id<=layout_id&&v.name.equals(name)){
                r=v;
            }
        }
        return r;
    }
    default boolean hasCovered(int pos,char[] bracket){
        String code=getCompiler().code.getCode();
        //System.out.println(code);
        int left=0,right=0;
        for (int i = pos; i < code.length(); i++) {
            if(!Arrays.equals(bracket, BRACLET_STRING)) {
                if (code.charAt(i) == bracket[1])
                    right++;
                if (code.charAt(i) == bracket[0])
                    right--;
            }else if(code.charAt(i) == bracket[0])
                right++;
        }
        for (int i = pos; i >= 0; i--) {
            if(!Arrays.equals(bracket, BRACLET_STRING)) {
                if (code.charAt(i) == bracket[0])
                    left++;
                if (code.charAt(i) == bracket[1])
                    left--;
            }else if(code.charAt(i) == bracket[0])
                left++;
        }

        if(!Arrays.equals(bracket, BRACLET_STRING)) {
            return right == left && right + left != 0;
        } else return left%2!=0||right%2!=0;
    }
    default int indexOf(Variable var){
        int ptr=-1;
        for(Variable v : vars){
            ptr++;
            if(v.layout_id<=var.layout_id&&v.area_id==var.area_id&& v.name.equals(var.name))
                return ptr;
        }
        return ptr;
    }

}
