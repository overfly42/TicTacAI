package MyTicTacAI2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.App;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyTest
{
    public MyTest()
    {

    }
    @Test
    public void test1()
    {
        final App a = new App();
        final App b = new App();
        assertTrue(true);
assertEquals(a,b);
    }
    @Test
    public void test2()
    {
        String x = "abc";
        String y = "abcd";
        assertTrue(x.equals(y)," x und y und diese beiden"); 
    }
}