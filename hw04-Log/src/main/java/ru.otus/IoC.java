package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class IoC {

    static TestInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{TestInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestInterface myClass;
        private final Set<String> listMethods = new HashSet<>();

        DemoInvocationHandler(TestInterface myClass) {
            this.myClass = myClass;
            Method[] methods = this.myClass.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.getAnnotation(Log.class) != null) {
                    listMethods.add(getMethodNameWithParametersTypes(m));
                }
            }
        }

        private String getMethodNameWithParametersTypes(Method m) {
            String result = m.getName();
            Parameter[] params = m.getParameters();
            if (params.length != 0) {
                result += " " + Arrays.stream(params)
                        .map(x -> x.getType().toString())
                        .collect(Collectors.joining(","));
            }
            return result;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (listMethods.contains(getMethodNameWithParametersTypes(method))) {
                System.out.println(String.format("executed method: %s, params: %s"
                        , method.getName(), args[0]));
            }
            return method.invoke(myClass, args);
        }
    }
}
