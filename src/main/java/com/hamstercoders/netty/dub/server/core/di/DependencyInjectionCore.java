package com.hamstercoders.netty.dub.server.core.di;

import com.hamstercoders.netty.dub.server.core.di.bean.*;
import com.hamstercoders.netty.dub.server.core.di.exceptions.ComponentNotFoundException;
import com.hamstercoders.netty.dub.server.core.di.exceptions.FailureBeanCreationException;
import com.hamstercoders.netty.dub.server.core.di.exceptions.FailureBeanInjectionException;
import com.hamstercoders.netty.dub.server.core.di.exceptions.FailureComponentCreationException;
import com.hamstercoders.netty.dub.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static com.hamstercoders.netty.dub.utils.ReflectionUtil.hasAnnotation;

/**
 * Created on 23-Jul-15.
 *
 * @author Nazar Dub
 */
public class DependencyInjectionCore implements ComponentSource {

    private List<Class<?>> configurations;

    private List<Class<?>> componentsClasses;

    private Map<String, BeanDefinition> beans;

    private Map<String, BeanDefinition> preparedComponents;

    private String rootPackage;

    public DependencyInjectionCore(String rootPackage) {
        Objects.requireNonNull(rootPackage);
        this.rootPackage = rootPackage;
        beans = new HashMap<>();
        preparedComponents = new HashMap<>();
    }

    public void initialization() {
        findConfigurationClasses(this.rootPackage);
        findBeans();
        findComponents(this.rootPackage);
        injectBeansToComponents();
    }

    private void findConfigurationClasses(String rootPackage) {
        configurations = ReflectionUtil.find(rootPackage, clazz -> clazz.isAnnotationPresent(Configuration.class));
    }

    private void findBeans() {
        try {
            Object currentConfiguration = null;
            for (Class<?> c : configurations) {
                currentConfiguration = c.newInstance();
                for (Method m : c.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Bean.class)) {
                        m.setAccessible(true);
                        Class<?> returnClass = m.getReturnType();
                        if (m.getAnnotation(Bean.class).value()) {
                            beans.put(returnClass.getName(),
                                    new SingletonBeanDefinition(returnClass, m.invoke(currentConfiguration)));
                        } else {
                            beans.put(returnClass.getName(),
                                    new PrototypeBeanDefinition(returnClass, currentConfiguration, m));
                        }
                    }
                }
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new FailureBeanCreationException(e);
        }
    }

    private void findComponents(String rootPackage) {
        componentsClasses = ReflectionUtil.find(rootPackage, clazz -> hasAnnotation(clazz, Component.class) && !clazz.isAnnotation());
    }

    private void injectBeansToComponents() {
        for (Class<?> c : componentsClasses) {
            try {
                Object object = c.newInstance();
                for (Field f : c.getDeclaredFields()) {
                    if (f.isAnnotationPresent(Inject.class)) {
                        f.setAccessible(true);
                        Class<?> fieldClass = f.getType();
                        BeanDefinition beanDefinition = findBeenByClass(fieldClass);
                        f.set(object, beanDefinition.getInstance());
                    }
                }
                preparedComponents.put(c.getName(), new SingletonBeanDefinition(c, object));
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private BeanDefinition findBeenByClass(Class<?> clazz) {
        BeanDefinition definitionToReturn = beans.get(clazz.getName());
        if (definitionToReturn != null) {
            return definitionToReturn;          //TODO Resolve more than one bean to inject in this case
        } else {
            for (BeanDefinition definition : beans.values()) {
                if (Arrays.asList(definition.getClazz().getInterfaces()).contains(clazz)) {
                    if (definitionToReturn == null) {
                        definitionToReturn = definition;
                    } else {
                        throw new FailureBeanInjectionException("Can't resolve bean injection, more than one beanDefinition found, for class: " + clazz.getName());
                    }
                }
            }
            if (definitionToReturn != null) {
                return definitionToReturn;
            } else {
                throw new FailureBeanInjectionException("Can't find bean to inject for class: " + clazz.getName());
            }
        }
    }

    @Override
    public Object getComponent(Class<?> clazz) {
        Object objectToReturn = preparedComponents.get(clazz.getName()).getInstance();
        if (objectToReturn != null) {
            return objectToReturn;
        } else {
            for (BeanDefinition definition : preparedComponents.values()) {
                if (Arrays.asList(definition.getClazz().getInterfaces()).contains(clazz)) {
                    if (objectToReturn == null) {
                        objectToReturn = definition.getInstance();
                    } else {
                        throw new FailureComponentCreationException("Can't resolve component to return, more than one component found, for class: " + clazz.getName());
                    }
                }
            }
            if (objectToReturn != null) {
                return objectToReturn;
            } else {
                throw new ComponentNotFoundException("Can't find component  for class: " + clazz.getName());
            }
        }
    }

}
