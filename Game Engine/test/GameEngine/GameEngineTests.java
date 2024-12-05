/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package GameEngine;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author everettmiller
 */
public class GameEngineTests {
    
    public GameEngineTests() {
    }

    /**
     * Test of FibonacciChallenge method, of class GameEngine.
     */
    @Test
    public void TestFibonacciChallenge()  {
        System.out.println("FibonacciChallenge");
        GameEngine Fibonacci = new GameEngine();
        int[] expResult = {1,1,2,3,5};
        int[] result = Fibonacci.FibonacciChallenge();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    }
   
}