/**
 * Copyright (c) 2012 Todoroo Inc
 *
 * See the file "LICENSE" for the full license governing this code.
 */
package com.todoroo.andlib.service;

import com.todoroo.andlib.service.ExceptionService.AndroidLogReporter;
import com.todoroo.andlib.service.ExceptionService.ErrorReporter;

import java.lang.reflect.Field;
import java.util.LinkedList;



/**
 * Simple Dependency Injection Service for Android.
 * <p>
 * Add dependency injectors to the injector chain, then invoke this method
 * against classes you wish to perform dependency injection for.
 * <p>
 * All errors encountered are handled as warnings, so if dependency injection
 * seems to be failing, check the logs for more information.
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class DependencyInjectionService {

    /**
     * Dependency injectors. Use getters and setters to modify this list
     */
    private final LinkedList<AbstractDependencyInjector> injectors = new LinkedList<>();

    /**
     * Perform dependency injection in the caller object
     *
     * @param caller
     *            object to perform DI on
     */
    public void inject(Object caller) {
        // Traverse through class and all parent classes, looking for
        // fields declared with the @Autowired annotation and using
        // dependency injection to set them as appropriate

        Class<?> cls = caller.getClass();
        while(cls != null) {
            String packageName = cls.getPackage().getName();
            if(!isQualifiedPackage(packageName)) {
                break;
            }

            for(Field field : cls.getDeclaredFields()) {
                if(field.getAnnotation(Autowired.class) != null) {
                    field.setAccessible(true);
                    try {
                        handleField(caller, field);
                    } catch (IllegalStateException | IllegalAccessException | IllegalArgumentException e) {
                        throw new RuntimeException(String.format("Unable to set field '%s' of type '%s'",
                                field.getName(), field.getType()), e);
                    }
                }
            }

            cls = cls.getSuperclass();
        }
    }

    private boolean isQualifiedPackage(String packageName) {
        if(packageName.startsWith("com.todoroo")) {
            return true;
        }
        if(packageName.startsWith("org.weloveastrid")) {
            return true;
        }
        if(packageName.startsWith("org.tasks")) {
            return true;
        }
        return false;
    }

    /**
     * This method returns the appropriate dependency object based on the type
     * that this autowired field accepts
     *
     * @param caller
     *            calling object
     * @param field
     *            field to inject
     */
    private synchronized void handleField(Object caller, Field field)
            throws IllegalStateException, IllegalArgumentException,
            IllegalAccessException {

        if(field.getType().isPrimitive()) {
            throw new IllegalStateException(String.format(
                    "Tried to dependency-inject primative field '%s' of type '%s'",
                    field.getName(), field.getType()));
        }

        // field has already been processed, ignore
        if (field.get(caller) != null) {
            return;
        }

        for (AbstractDependencyInjector injector : injectors) {
            Object injection = injector.getInjection(field);
            if (injection != null) {
                field.set(caller, injection);
                return;
            }
        }

        throw new IllegalStateException(
                String.format("No dependency injector found for autowired " +
                		"field '%s' in class '%s'. Injectors: %s",
                        field.getName(), caller.getClass().getName(),
                        injectors));
    }

    // --- default dependency injector

    private class DefaultDependencyInjector extends AbstractDependencyInjector {
        @Override
        protected void addInjectables() {
            injectables.put("debug", false);
            injectables.put("exceptionService", ExceptionService.class);
            injectables.put("errorReporters", new ErrorReporter[] {
                    new AndroidLogReporter(),
            });
        }
    }

    // --- static methods

    private static DependencyInjectionService instance = null;

    DependencyInjectionService() {
        // prevent instantiation
        injectors.add(new DefaultDependencyInjector());
    }

    /**
     * Gets the singleton instance of the dependency injection service.
     */
    public synchronized static DependencyInjectionService getInstance() {
        if(instance == null) {
            instance = new DependencyInjectionService();
        }
        return instance;
    }

    /**
     * Removes the supplied injector
     */
    public synchronized void removeInjector(AbstractDependencyInjector injector) {
        injectors.remove(injector);
    }

    /**
     * Adds a Dependency Injector to the front of the list
     */
    public synchronized void addInjector(AbstractDependencyInjector injector) {
        removeInjector(injector);

        this.injectors.addFirst(injector);
    }

}