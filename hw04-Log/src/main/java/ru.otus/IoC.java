package ru.otus;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class IoC {

    static <T> T createMyClass(Class<T> myClassInterface , Class<? extends T> myClassImpl) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        InvocationHandler handler = new DemoInvocationHandler<T>(myClassImpl);
        return (T) Proxy.newProxyInstance(IoC.class.getClassLoader(), new Class<?>[]{myClassInterface }, handler);
    }

    static class DemoInvocationHandler<T> implements InvocationHandler {
        private final T myClass;
        private final Set<String> methodsForLog = new HashSet<>();

        DemoInvocationHandler(Class<? extends T> myClassImpl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
            this.myClass = myClassImpl.getConstructor().newInstance();
            Method[] methods = this.myClass.getClass().getDeclaredMethods();
            for (Method m : methods) {
                if (m.getAnnotation(Log.class) != null) {
                    methodsForLog.add(getMethodNameWithParametersTypes(m));
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
            if (methodsForLog.contains(getMethodNameWithParametersTypes(method))) {
                System.out.println(String.format("executed method: %s, params: %s"
                        , method.getName(), args[0]));
            }
            return method.invoke(myClass, args);
        }
    }
}
