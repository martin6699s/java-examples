package com.myagent.martin6699s;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

/**
 * @author martin
 * @date 2019/9/9
 **/
public class PremainTransformer implements ClassFileTransformer {

    final static String prefix = "\nlong startTime = System.currentTimeMillis();\n";

    final static String postfix = "\nlong endTime = System.currentTimeMillis();\n";
    final static List<String> methodList = new ArrayList<String>();


    public PremainTransformer() {
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
                                substring(method.lastIndexOf(".") + 1, method.length());

                        String outputStr = "\nSystem.out.println(\"方法执行时间:  "
                                + methodName
                                + "\" + (endTime - startTime) + \"ms.\");";

                        CtMethod ctMethod = ctClass
                                .getDeclaredMethod(methodName);

                        String newMethodName = methodName + "$impl";

                        ctMethod.setName(newMethodName);

                        //创建新的方法，复制原来的方法 ，名字为原来的名字
                        CtMethod newMethod = CtNewMethod.copy(ctMethod, methodName, ctClass,null);

                        // 构建新的方法体
                        StringBuilder bodyStr = new StringBuilder(250);
                        bodyStr.append("{");
                        bodyStr.append(prefix);
                        bodyStr.append(newMethodName+"($$);\n");
                        bodyStr.append(postfix);
                        bodyStr.append(outputStr);
                        bodyStr.append("}");

                        newMethod.setBody(bodyStr.toString());
                        ctClass.addMethod(newMethod);
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
