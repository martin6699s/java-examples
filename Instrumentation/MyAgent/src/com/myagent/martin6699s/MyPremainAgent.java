package com.myagent.martin6699s;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author martin
 * @date 2019/9/11
 **/
public class MyPremainAgent {

    public static void premain(String agentArgs, Instrumentation inst)
            throws ClassNotFoundException, UnmodifiableClassException {

        System.out.println("进入---premain---start");
        inst.addTransformer(new PremainTransformer());
        System.out.println("进入---premain---end");
    }
}
