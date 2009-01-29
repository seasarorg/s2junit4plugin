/*
 * Copyright 2008 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * This is a program source code derived from the Quick JUnit Plugin for Eclipse.
 * An original copyright:Copyright © 2003-2008 Masaru Ishii,The Quick JUnit Plugin Project.
 */
package org.seasar.s2junit4plugin.action;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;


public class JavaElements {
    public static boolean isTestMethod(IJavaElement element) throws JavaModelException {
        if (!(element instanceof IMethod))
            return false;
        IMethod method = (IMethod) element;
        if (method.getNumberOfParameters() != 0)
            return false;
        if (!method.getReturnType().equals("V"))
            return false;
        int flags = method.getFlags();
        if (!Flags.isPublic(flags) || Flags.isStatic(flags))
            return false;
        if(method.getElementName().startsWith("test")) return true;
        
        if (hasS2JUnit4Annotation((IType)element.getParent())) return true;
        
        return hasTestAnnotationOnMethod(method);
    }

    public static IType getPrimaryTypeOf(IJavaElement element) {
        if (element == null)
            return null;
        ICompilationUnit cu = null;
        if (element instanceof ICompilationUnit) {
            cu = (ICompilationUnit) element;
        } else if (element instanceof IMember) {
            cu = ((IMember) element).getCompilationUnit();
        }
        return cu != null ? cu.findPrimaryType() : null; 
    }
    
    public static boolean isTestClass(IType type) throws JavaModelException {
        ITypeHierarchy superTypeHierarchy = type.newSupertypeHierarchy(null);
        IType superTypes[] = superTypeHierarchy.getAllInterfaces();
        for (int i = 0; i < superTypes.length; ++i) {
            IType superType = superTypes[i];
            if (superType.getFullyQualifiedName().equals(JavaTypes.TEST_INTERFACE_NAME))
                return true;
        }
        return false;
    }

    public static IJavaElement getTestMethodOrClass(IJavaElement element)
            throws JavaModelException {
        if (element.getElementType() == IJavaElement.COMPILATION_UNIT) {
            element = ((ICompilationUnit) element).findPrimaryType();
        }
        while (element != null) {
            if (isTestMethod(element))
                return element;
            
            if (isTestRunnerPassibleClass(element)) {
                IType type = (IType) element;
                if (isTestClass(type)) return element;
                if (hasSuiteMethod(type)) return element;
                if (hasSuiteAnnotation(type)) return element;
                if (hasTestAnnotation(type)) return element;
                if (hasS2JUnit4Annotation(type)) return element;
            }
            element = element.getParent();
        }
        return null;
    }

    private static boolean hasSuiteAnnotation(IType type) throws JavaModelException {
        return type.getSource() == null ? false:type.getSource().indexOf("@SuiteClasses") > -1;
	}
    
    private static boolean hasS2JUnit4Annotation(IType type) throws JavaModelException {
        return type.getSource() == null ? false:type.getSource().indexOf("@RunWith(Seasar2.class)") > -1;
	}

	private static boolean isTestRunnerPassibleClass(IJavaElement element) throws JavaModelException {
        if (!(element instanceof IType))
            return false;
        IType type = (IType) element;
        if (!type.isClass())
            return false;
        int flags = type.getFlags();
        if (Flags.isAbstract(flags) || !Flags.isPublic(flags))
            return false;

        return true;
    }
    
    private static boolean hasSuiteMethod(IType type) throws JavaModelException {
        IMethod[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (isStaticSuiteMethod(methods[i])) return true;
        }
        return false;
    }
    
    private static boolean hasTestAnnotation(IType type) throws JavaModelException{
        IMethod[] methods = type.getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (hasTestAnnotationOnMethod(methods[i])) return true;
        }
        return false;
    }
    
    private static boolean hasTestAnnotationOnMethod(IMethod method) throws JavaModelException{
        return method.getSource() == null ? false:method.getSource().indexOf("@Test") > -1;
    }

    private static boolean isStaticSuiteMethod(IMethod method) throws JavaModelException {
        return ((method.getElementName().equals("suite")) &&
                method.getSignature().equals("()QTest;") &&
                Flags.isPublic(method.getFlags()) &&
                Flags.isStatic(method.getFlags())
        );
    }
}
