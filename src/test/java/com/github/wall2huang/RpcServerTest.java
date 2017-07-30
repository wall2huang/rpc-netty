package com.github.wall2huang;/**
 * Created by Administrator on 2017/7/26.
 */

import com.github.wall2huang.configuration.SpringServerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * author : Administrator
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringServerConfig.class)
public class RpcServerTest
{
    @Test
    public void start() throws NoSuchMethodException
    {


    }


}
