package com.github.wall2huang.core;/**
 * Created by Administrator on 2017/7/19.
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * author : Administrator
 **/
public class RpcServer implements ApplicationContextAware, InitializingBean
{

    

    @Override
    public void afterPropertiesSet() throws Exception
    {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {

    }
}
