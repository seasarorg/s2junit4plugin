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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.ide.IDE;
import org.seasar.s2junit4plugin.Activator;
import org.seasar.s2junit4plugin.Constants;
import org.seasar.s2junit4plugin.Logger;
import org.seasar.s2junit4plugin.util.JDTUtil;
import org.seasar.s2junit4plugin.util.PreferenceStoreUtil;
import org.seasar.s2junit4plugin.util.ResouceUtil;
import org.seasar.s2junit4plugin.wizard.NewDiconWizard;



/**
 * TestCase dicon を開く {@link IEditorActionDelegate} です。
 */
public class OpenDiconPairAction implements IEditorActionDelegate, IObjectActionDelegate {



	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction, org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {}

	/**
	 * @see org.eclipse.ui.IEditorActionDelegate#setActiveEditor(org.eclipse.jface.action.IAction, org.eclipse.ui.IEditorPart)
	 */
	public void setActiveEditor(IAction action, IEditorPart targetEditor) {}

	/**
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IResource resource = ResouceUtil.getCurrentSelectedResouce();
		if (resource instanceof IFile) {
			IFile file = (IFile) resource;
			try {
				ICompilationUnit compilationUnit = JavaCore.createCompilationUnitFrom(file);

				IJavaProject javaProject = compilationUnit.getJavaProject();
				String compiliance = javaProject.getOption(JavaCore.COMPILER_COMPLIANCE, true);
				try {
					if(Float.parseFloat(compiliance) < 1.5) {
						Logger.debug("not S2Junit4 environemnt. copliance=" + compiliance);
						return;
					}
				}
				catch(NumberFormatException nfe) {
					Logger.warn("Illegal Compiler Level: " + compiliance, nfe);
					return;
				}

				IType type = getClassPairType(compilationUnit);
				if(type == null) {
					return;
				}

				IFile diconPair = getDiconPair(type);
				Logger.debug("diconPair=" + diconPair);

				IWorkbench workbench = Activator.getDefault().getWorkbench();
				if(diconPair != null) {
					IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), diconPair);
				}
				else {
					String testResourcesPath = PreferenceStoreUtil.getPreferenceStoreOfWorkspace().getString(Constants.PREF_TEST_RESOURCES_PATH);
					IFolder folder = javaProject.getProject().getFolder(testResourcesPath);
					IPackageFragmentRoot packageFragmentRoot = javaProject.findPackageFragmentRoot(folder.getFullPath());
					NewDiconWizard wizard = new NewDiconWizard(type.getPackageFragment(), packageFragmentRoot, type.getTypeQualifiedName() + ".dicon");
					wizard.init(workbench, new StructuredSelection());
					new WizardDialog(workbench.getActiveWorkbenchWindow().getShell(), wizard).open();
				}
			}
			catch(Exception e) {
				Logger.error(e, this);
			}
		}



	}

	/**
	 * エディタで編集中の Java ソースモデルを取得します。
	 * @param activeEditor 現在アクティブになっているエディタ
	 * @return エディタで編集中の Java ソースモデル
	 */
	protected ICompilationUnit getCompilationUnit(IEditorPart activeEditor) {
		IFile file = ((IFileEditorInput)activeEditor.getEditorInput()).getFile();
		return JavaCore.createCompilationUnitFrom(file);
	}

	/**
	 * TestCase の dicon ペアを取得します。
	 * @param compilationUnit　Javaソースモデル
	 * @return TestCase の dicon ペア
	 * @throws JavaModelException JDTエラーが発生した場合
	 */
	protected IFile getDiconPair(IType type) throws JavaModelException {
		String diconFileName = type.getFullyQualifiedName().replace('.', '/') + ".dicon";
		IJavaProject javaProject = type.getJavaProject();
		IWorkspaceRoot root = javaProject.getProject().getWorkspace().getRoot();
		for(IPath sourceFolder : JDTUtil.getSourceFolders(javaProject)) {
			IPath diconFilePath = sourceFolder.append(diconFileName);
			IFile file = root.getFile(diconFilePath);
			if(file.exists()) {
				return root.getFile(diconFilePath);
			}
		}
		return null;
	}

	/**
	 * S2JUnit4 TestCase クラスを取得します。
	 * @param compilationUnit Java ソース
	 * @return TestCase クラス。S2JUnit4 TestCase クラスが未定義の場合 null
	 */
	protected IType getClassPairType(ICompilationUnit compilationUnit) throws JavaModelException {
		IFile file = (IFile)compilationUnit.getResource();
		String	fileName = file.getName();
		String extension = file.getFileExtension();
		if((extension != null) && !fileName.equals(extension)) {
			fileName = fileName.substring(0, (fileName.length() - extension.length() - 1));
		}
		Logger.debug("file=" + fileName);

		for(IType type : compilationUnit.getTypes()) {
			if(fileName.equals(type.getTypeQualifiedName())) {
				if(isS2JUnit4TestCase(type)) {
					return type;
				}
			}
		}
		Logger.debug("not S2Junut4 testcase.");
		return null;
	}

	/**
	 * クラスが S2JUnit4 テストケースクラスであるかどうかを判定します。
	 * @param type　判定対象のクラス
	 * @return S2JUnit4 テストケースの場合 true
	 */
	protected boolean isS2JUnit4TestCase(IType type) throws JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(type.getCompilationUnit());
		IBinding[] bindings = parser.createBindings(new IType[]{type}, null);
		IAnnotationBinding[] annotations = bindings[0].getAnnotations();
		for(IAnnotationBinding annotation : annotations) {
			Logger.debug("annotation=" + annotation.getName());
			if("org.junit.runner.RunWith".equals(annotation.getAnnotationType().getQualifiedName())) {
				for(IMemberValuePairBinding value : annotation.getAllMemberValuePairs()) {
					Logger.debug("anno params=" + value.getName() + " / " + value.getValue());
					if("value".equals(value.getName())) {
						ITypeBinding runwithType = (ITypeBinding)value.getValue();
						return "org.seasar.framework.unit.Seasar2".equals(runwithType.getQualifiedName());
					}
				}
			}
		}
		return false;
	}
}