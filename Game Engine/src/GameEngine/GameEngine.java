/*
First we need to complete the Unit Tests 
for the execute engine 

Implement that into Alpha


#########class place holder for testing in code engine in web application.







User Process:

sumbit 

new instance of JVM

pass user source code 

pass in test code for fibonacci

return Pass or Fail

close JVM
*/









package GameEngine;
import java.util.List;
import jdk.jshell.JShell;
import jdk.jshell.Snippet.Status;
import jdk.jshell.SnippetEvent;
import jdk.jshell.SourceCodeAnalysis;

/**
 *
 * @author everettmiller
 */
public class GameEngine {
    
    /*
    CHALLENGE #1 Fibonacci Challenge:
    
    Return the values of the first five 
    fibonnaci numbers in an array. The
    values in the array must be integers,
    and must start at 1.
    
    HINT: 5 is the last element
    in the array.
    */
    
    //------------------------------
    public int[] FibonacciChallenge(){
        
        
 
      
        return null;
        
    }
    //------------------------------
    
   
    /*
    
    $ Evaluate a return Statement for the program
    
    $ If js1 results are passed through unit test
   
    $ Pass or fail
    
    
    
    */
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    private static String testCode1 = """
        int x = 100;                              
        int sum = 0;
        for(int i = 0; i < 5; i++) {
            sum = sum + i;
        }
        x = x + sum;
                                     """;
    
    private static String testCode2 = """
        int x = 200;                              
        int sum = 0;
        for(int i = 0; i < 10; i++) {
            sum = sum + i;
        }
        x = x + sum;
                                      """;
    
    private static String testCode6 = """
      public class Test {
          public int test() {
              return 5;
          }                  
      }
      Test test = new Test();
      int x = test.test();
                                    """;
    
    private static String testCode3 = "x"; // eval() of this express can be used to retrieve the value of integer variable "x" as as string
    private static String testCode4 = "sum"; // eval() of this express can be used to retrieve the value of integer variable "sum" as as string
    private static String testCode5 = "test.test()"; // eval() of this express can be used to retrieve the value returned by the method "test.test()" as as string
    
  
    public static void main(String[] args) throws InterruptedException {
      
    
        
        JShell.Builder jsb = JShell.builder();
        
        /*
            jsb.build() builds a JShell state engine. This is the entry-point
            to all JShell functionality. This creates a remote process
            for execution. It is thus important to close the returned instance.
        
            Each jsb.build() call creates a new java.exe process (separate JVM instance)
        */
        
        JShell js1 = jsb.build(); // JVM instance 1
        JShell js2 = jsb.build(); // JVM instance 2
        JShell js3 = jsb.build(); // JVM instance 3

        System.out.println(execute(js1, testCode2, testCode1));
        System.out.println(execute(js2, testCode2, testCode4));
        System.out.println(execute(js3, testCode6, testCode5));
        
        
        
        /*
            Wait 30 seconds.  Use ctrl-shift-esc to bring up Windows Task Manager.
            You will see three java.exe instances.  One instance is this program,
            the other two instances are created by the JShell code.
            For a hacking challenge, each person's code needs to execute in a separate JVM.
        */
        /*
        //Thread.sleep(30000); 
        js1.close(); // Close JVM instance 1 (if you don't do this, you will have a resource leak.  close() terminates the JVM instance)
        js2.close(); // Close JVM instance 2 (if you don't do this, you will have a resource leak.  close() terminates the JVM instance)
        js3.close(); // Close JVM instance 3 (if you don't do this, you will have a resource leak.  close() terminates the JVM instance)
        */
    }
    
    /*
        The JShell eval(String) method can only evaluate a single snippet of code.
        A snippet can be one expression, statement, variable declaration, 
        method declaration, class declaration, or import.
        
        The SourceCodeAnalysis.analyzeCompletion(String) method is used to break up
        arbitrary input into individual complete snippets. 
    
        JavaDoc for JShell eval() method 
        https://docs.oracle.com/en/java/javase/17/docs/api/jdk.jshell/jdk/jshell/JShell.html#eval(java.lang.String)
        
        The sourceCode is the Java code to be executed.
        The resultCode is a Java expression that evaluates to a single value
        and that value can be extracted using the value() method of the SnippetEvent
        returned by eval().
    */
   
    public static String execute(JShell jsh, String sourceCode, String resultCode) {
        String source = "";
        String remaining = sourceCode;
        SourceCodeAnalysis.CompletionInfo scaci = null;
        List<SnippetEvent> se = null;
        
        while(remaining.length() > 0) {
            scaci = jsh.sourceCodeAnalysis().analyzeCompletion(remaining);
            source = scaci.source(); // extract a single snippet for eval() from remaining
            remaining = scaci.remaining(); // set remaining to whatever is left
            se = jsh.eval(source); // evaluate single snippet
            
            if(!scaci.completeness().isComplete()){
                return null; // incomplete code
            }
            
            
            // invalid snippet
            if(se.get(0).status() != Status.VALID){
                return null;
            } 
        }
        
        
        se = jsh.eval(resultCode);
        if(se.get(0).status() != Status.VALID){
            return null; // invalid code
        } 
        
        
        return se.get(0).value(); // executed all code
        
    }
    
}


