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

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;

public abstract class QuickJUnitAction implements IEditorActionDelegate, IObjectActionDelegate {
    private Shell shell;
    protected IJavaElement javaElement;
    protected ITextEditor javaEditor;
    
    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        if (!(targetEditor instanceof ITextEditor)) {
            javaEditor = null;
            return;
        }
        javaEditor = (ITextEditor) targetEditor;
        shell = javaEditor.getSite().getShell();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
        shell = targetPart.getSite().getShell();
    }

    public void selectionChanged(IAction action, ISelection selection) {
        if (! (selection instanceof IStructuredSelection)) {
            javaElement = null;
            return;
        }
        Object element = ((IStructuredSelection) selection).getFirstElement();
        if (element instanceof IJavaElement)
            javaElement = (IJavaElement) element;
        else
            javaElement = null;
    }

    protected IJavaProject[] getJavaProjects() throws JavaModelException {
        return JavaCore.create(ResourcesPlugin.getWorkspace().getRoot()).getJavaProjects();
    }

    protected void openInformation(IAction action, String message) {
        MessageDialog.openInformation(shell, action.getText(), message);
    }

    protected Shell getShell() {
        return shell;
    }

    protected IJavaElement getElementOfJavaEditor() throws JavaModelException {
        ICompilationUnit unit = getCompilationUnitOfJavaEditor();
        if (unit == null)
            return null;
        ISelectionProvider provider = javaEditor.getSelectionProvider();
        ISelection selection = provider.getSelection();
        if (!(selection instanceof ITextSelection))
            return null;
        int offset = ((ITextSelection) selection).getOffset();
        IJavaElement element = unit.getElementAt(offset);
        return element;
    }
    
    protected ICompilationUnit getCompilationUnitOfJavaEditor() throws JavaModelException {
        if (javaEditor == null)
            return null;
        IEditorInput input = javaEditor.getEditorInput();
        IJavaElement element = (IJavaElement) input.getAdapter(IJavaElement.class);
        if (element instanceof ICompilationUnit)
            return (ICompilationUnit) element;
        return null;
    }
}
