package com.myagent.martin6699s;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author martin
 * @date 2019/9/9
 **/
public class AgentmainTransformer implements ClassFileTransformer {

    final static List<String> methodList = new ArrayList<String>();


    public AgentmainTransformer() {
        methodList.add("com.myagent.martin6699s.TimeHelloWorld.sayHello");
        methodList.add("com.myagent.martin6699s.TimeHelloWorld.sayHelloMartin");

    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        if(className.startsWith("com/myagent/martin6699s")) {
            // 判断加载的类包名是不是我们需要目标类的包名
            className = className.replaceAll("/", ".");

            CtClass ctClass = null;

            try {

                ctClass = ClassPool.getDefault().get(className);

                for(String method: methodList) {

                    if(method.startsWith(className)) {

                        String methodName = method.
                                substring(method.lastIndexOf(".") + 1);

                        CtMethod ctMethod = ctClass
                                .getDeclaredMethod(methodName);
                        ctMethod.addLocalVariable("startTime", CtClass.longType);
                        ctMethod.insertBefore("startTime = System.currentTimeMillis();");
                        ctMethod.insertAfter("{final long endTime = System.currentTimeMillis();\n System.out.println(\"方法("+ className +"#" + methodName +")执行时间: \" + (endTime-startTime) + \" 毫秒(ms) \");}");

                    }
                }

                System.out.println("准备替换-----" + className);
                return ctClass.toBytecode();

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("异常信息：" + e.getMessage());
            }
        }


        return null;
    }
}
