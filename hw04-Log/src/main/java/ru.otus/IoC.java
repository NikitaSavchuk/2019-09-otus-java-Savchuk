package ru.otus;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IoC {

    static TestInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestInterface) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{TestInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestInterface myClass;

        DemoInvocationHandler(TestInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println(String.format("executed method: %s, params: %s"
                    , method.getName(), args[0]));
            return method.invoke(myClass, args);
        }
    }
}
