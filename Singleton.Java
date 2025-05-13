// Online Java Compiler
// Use this editor to write, compile and run your Java code online
//singleton

class Singleton{
   static Singleton ref;
    private Singleton(){
        
    }
    static Singleton getObj(){
         if(ref==null){
             return new Singleton();
         }else{
             return ref;
         }
     }
}

class Main {
    public static void main(String[] args) {
        Singleton s1=Singleton.getObj();
        System.out.println(s1);
    }
}