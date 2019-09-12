package com.myagent.martin6699s;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

/**
 * @author martin
 * @date 2019/9/9
 **/
public class MyAgentmainAgent {

   public static void agentmain(String agentArgs, Instrumentation inst)
       throws ClassNotFoundException, UnmodifiableClassException {

       String targetClassPath = "com.myagent.martin6699s.TimeHelloWorld";

       for(Class<?> clazz : inst.getAllLoadedClasses()) {

           if(!inst.isModifiableClass(clazz)) {
               continue;
           }

           if(clazz.getName().equals(targetClassPath)) {
               inst.addTransformer(new AgentmainTransformer(), true);
               inst.retransformClasses(clazz);
               System.out.println("Agent Main Done");

           }
       }

   }


}
