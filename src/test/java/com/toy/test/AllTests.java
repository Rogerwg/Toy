/**************************************************************************************** 
        途牛科技有限公司
 ****************************************************************************************/
package com.toy.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <Description> <br>
 * 
 * @author wanggang3<br>
 * @version 1.0<br>
 * @taskId <br>
 * @CreateDate 2016年4月22日 <br>
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        SquareTest.class, CalculatorTest.class
})
public class AllTests {
    @Test
    public void test() {
    }

}
