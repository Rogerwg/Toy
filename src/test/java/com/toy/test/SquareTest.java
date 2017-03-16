/**************************************************************************************** 
        途牛科技有限公司
 ****************************************************************************************/
package com.toy.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.toy.tools.Calculator;

/**
 * <Description> <br>
 * 
 * @author wanggang3<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年4月22日 <br>
 */
@RunWith(Parameterized.class)
public class SquareTest {

    private static Calculator calculator = new Calculator();

    private int param;

    private int result;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
                {2, 4},
                {0, 0}, 
                {-3, 9},
        });
    }

    public SquareTest(int param, int result) {
        this.param = param;
        this.result = result;
    }

    /**
     * Test method for {@link com.toy.tools.Calculator#square(int)}.
     */
    @Test
    public void testSquare() {
        calculator.square(param);
        assertEquals(result, calculator.getResult());
    }

}
