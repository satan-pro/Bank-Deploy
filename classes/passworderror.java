package class_lib;

import java.util.*;
import java.lang.*;
import class_lib.*;


public class passworderror extends Exception{
    public String toString(){
        return("This violates our password policy");
    }
}
