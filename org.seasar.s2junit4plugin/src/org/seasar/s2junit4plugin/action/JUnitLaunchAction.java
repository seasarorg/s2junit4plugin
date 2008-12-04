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

import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.seasar.s2junit4plugin.Logger;
import org.seasar.s2junit4plugin.Messages;


public class JUnitLaunchAction extends QuickJUnitAction {
    private String mode;
    private ILaunchShortcut launchShortcut;
    
    public JUnitLaunchAction(ILaunchShortcut launchShortcut, String mode) {
        this.launchShortcut = launchShortcut;
        this.mode = mode;
    }

    private IJavaElement getTargetElement(IAction action) throws JavaModelException {
        IJavaElement element = getSelectedElement();
        if (element == null || element.getElementType() < IJavaElement.COMPILATION_UNIT)
            return element;

        IJavaElement testableElement = JavaElements.getTestMethodOrClass(element);
        if (testableElement != null)
            return testableElement;

        IType type = JavaElements.getPrimaryTypeOf(element);
        if (type == null)
            return element;

        openInformation(action, Messages.getString("JUnitLaunchAction.notJUnitElement")); //$NON-NLS-1$
        return null;
    }

    private IJavaElement getSelectedElement() throws JavaModelException {
        IJavaElement element = getElementOfJavaEditor();
        return element == null ? javaElement : element;
    }

    public void run(IAction action) {
        try {
            IJavaElement element = getTargetElement(action);
            if (element == null)
                return;
            ISelection sel = new StructuredSelection(new Object[] { element });
            launchShortcut.launch(sel, mode);
        } catch (JavaModelException e) {
            Logger.error(e, this);
        }
    }
}
