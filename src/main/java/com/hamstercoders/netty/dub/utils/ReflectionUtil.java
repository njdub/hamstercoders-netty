package com.hamstercoders.netty.dub.utils;

import com.hamstercoders.netty.dub.server.core.HttpHandler;
import com.hamstercoders.netty.dub.server.core.di.Component;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 20-Jul-15.
 *
 * @author Nazar Dub
 */
public class ReflectionUtil {

    private static final char DOT = '.';
    private static final char SLASH = '/';
    private static final String CLASS_SUFFIX = ".class";
    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    private static final ClassFinderFilter DEFAULT_CLASS_FILTER = clazz -> true;

    private static void find(List<Class<?>> classes, ClassFinderFilter filter, File directory, String scannedPackage) {
        for (File file : filesIn(directory)) {
            String filePath = scannedPackage + DOT + file.getName();
            if (file.isDirectory()) {
                find(classes, filter, file, filePath);
            } else {
                if (filePath.endsWith(CLASS_SUFFIX)) {
                    String fullClassPath = filePath.substring(0, filePath.length() - CLASS_SUFFIX.length());
                    try {
                        Class<?> c = Class.forName(fullClassPath);
                        if (filter.filter(c))
                            classes.add(c);
                    } catch (ClassNotFoundException ignore) {
                        ignore.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param scannedPackage root package for scanning, don't user project root package
     * @param filter         class filter, if <code>filter</code> is <code>null</code> method will use default filter
     *                       {@link ReflectionUtil.ClassFinderFilter}
     * @return all the classes  in <code>scannedPackage</code>  for which the filter is returned <code>true</code>
     */
    public static List<Class<?>> find(String scannedPackage, ClassFinderFilter filter) {
        if (filter == null) {
            filter = DEFAULT_CLASS_FILTER;

        }
        List<Class<?>> classes = new ArrayList<>();
        String scannedPath = scannedPackage.replace(DOT, SLASH);
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
        }
        File rootDir = new File(scannedUrl.getFile());
        find(classes, filter, rootDir, scannedPackage);
        return classes;
    }

    public static List<Class<?>> find(String scannedPackage) {
        return find(scannedPackage, DEFAULT_CLASS_FILTER);
    }


    /**
     * @return if annotation present on the class or in the class annotations  return <code>true</code>, else return
     * <code>false</code>
     */
    public static boolean hasAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz.isAnnotationPresent(annotation)) {
            return true;
        } else {
            List<Annotation> classAnnotations = Arrays.asList(clazz.getAnnotations());
            for (Annotation a : classAnnotations) {
                if (a.annotationType().isAnnotationPresent(annotation)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {
        System.out.println(hasAnnotation(ReflectionUtil.class, Component.class));
    }

    private static File[] filesIn(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            files = new File[0];
        }
        return files;
    }

    public static interface ClassFinderFilter {
        public boolean filter(Class<?> clazz);
    }
}
