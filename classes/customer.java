package class_lib;

import java.util.*;
import class_lib.user;



interface custom{
    void deposit(int d);
    int transfer(int d, customer c);
    int get_amount();
    String get_pass();
    int buy(int i, int share);
    int sell(int i, int share);
}



public class customer extends user implements custom{
    private int n = 0;
    public int aadhar;
    public String gender;
    public String Nationality;
    int c = 0;
    public int shares[] = new int[10];
    public customer(String n, String s, int a, String s1, String s2)throws passworderror, usererror{
            
            super(n, s);
            aadhar = a;
            gender = s1;
            Nationality = s2;
            for (int i = 0; i<10 ; i++){
                shares[i] = 0;
            }

        
    }
    public int get_amount(){
        return n;
    }
    public String get_pass(){
        return super.get_pass();
    }
    public void deposit(int d){
        n +=d;
    }
    public int transfer(int d, customer c){
        if(d<=n && d>0){
            n-=d;
            c.n += d;
            return 1;
        }
        else{
            return 0;
        }
    }
    int p;
    public int buy(int i, int share){
        p = bank.stocks[i].price;
        if(n < p*share){
            return 0;
        }
        else{
            this.n -= (p*share);
            shares[i] = shares[i] + share;
            System.out.println(n);
            return 1;
        }
    }
    int profit;
    public int sell(int i, int share){
        p = bank.stocks[i].price;
        
        if(shares[i] < share){
            return 0;
        }
        else{
            profit = share*p;
            shares[i] = shares[i] - share;
            this.n += profit;
            System.out.println(n);
            return 1;
        }
    }
    public int getsmoney(){
        int res = 0;
        for(int i = 0; i<10; i++){
            res = res + shares[i]*bank.stocks[i].price;
        }
        return res;
    }


}