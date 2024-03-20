package class_lib;

import java.util.*;
import java.lang.*;
import class_lib.*;


public class usererror extends Exception{
    public String toString(){
        return("This violates our username policy");
    }
}
