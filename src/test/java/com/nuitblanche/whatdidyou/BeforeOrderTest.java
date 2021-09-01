package com.nuitblanche.whatdidyou;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BeforeOrderTest {

    @Before
    public void beforeOne(){
        System.out.println("Before one");
    }
    @Before
    public void beforeTwo(){
        System.out.println("Before two");
    }
    @Before
    public void beforeThree(){
        System.out.println("Before three");
    }
    @Before
    public void beforeFour(){
        System.out.println("Before four");
    }

    @After
    public void after(){
        System.out.println("afterOne!!");
    }

    @After
    public void afterTwo(){
        System.out.println("afterTwo!!");
    }
    @After
    public void afterThree(){
        System.out.println("afterThree!!");
    }

    @Test
    public void dummy() {
        System.out.println("hello");
    }

    @Test
    public void dummy2(){
        System.out.println("hello 2");
    }
}
