package com.github.wall2huang;/**
 * Created by Administrator on 2017/7/25.
 */

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * author : Administrator
 **/
@Configuration
@ComponentScan
public class BootStrap
{
    public static void main(String[] args)
    {
        new AnnotationConfigApplicationContext(BootStrap.class);
    }
}
