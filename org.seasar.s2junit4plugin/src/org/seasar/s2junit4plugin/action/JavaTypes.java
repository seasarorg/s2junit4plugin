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
 */
package org.seasar.s2junit4plugin.action;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

public class JavaTypes {
	public static final String TEST_INTERFACE_NAME= "junit.framework.Test"; //$NON-NLS-1$

    public static boolean isTest(IType type) throws JavaModelException {
		ITypeHierarchy typeHier= type.newSupertypeHierarchy(null);
		IType[] superInterfaces= typeHier.getAllInterfaces();
		for (int i= 0; i < superInterfaces.length; i++) {
			if (superInterfaces[i].getFullyQualifiedName('.').equals(TEST_INTERFACE_NAME))
				return true;
		}
		return false;
	}
}
