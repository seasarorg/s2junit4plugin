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

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.ide.IDE;
import org.seasar.s2junit4plugin.Activator;
import org.seasar.s2junit4plugin.Logger;
import org.seasar.s2junit4plugin.util.JDTUtil;
import org.seasar.s2junit4plugin.util.ResouceUtil;


/**
 * TestCase class を開く {@link IEditorActionDelegate} です。
 */
public class OpenClassPairAction implements IWorkbenchWindowActionDelegate, IEditorActionDelegate, IObjectActionDelegate {


	/**
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

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
				IProject project = file.getProject();
				if(!project.isNatureEnabled(JavaCore.NATURE_ID)) {
					Logger.debug("not java project.");
					return;
				}

				IJavaProject javaProject = JavaCore.create(project);
				List<IPath> sourceFolders = JDTUtil.getSourceFolders(javaProject);

				IPath classPair = getClassPairName(file, sourceFolders);
				Logger.debug("classPair=" + classPair);
				IFile classPairFile = getClassPairFile(classPair, project, sourceFolders);
				if(classPairFile != null) {
					IDE.openEditor(Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage(), classPairFile);
				}
				else {
					Logger.debug("missing TestCase class pair.");
				}
			}
			catch(Exception e) {
				Logger.error(e, this);
			}
		}


	}

	/**
	 * TestCase Dicon から ペアとなる TestCase クラスのファイルパスをソースフォルダからの相対パスとして取得します。
	 * @param diconFile Dicon ファイル
	 * @param sourceFolders ソースフォルダリスト
	 * @return ペアとなる TestCase クラスのファイルパス
	 */
	protected IPath getClassPairName(IFile diconFile, List<IPath> sourceFolders) throws Exception {
		IPath diconFileName = diconFile.getFullPath();
		for(IPath sourceFolder : sourceFolders) {
			if(sourceFolder.isPrefixOf(diconFileName)) {
				IPath path = diconFileName.removeFirstSegments(sourceFolder.segmentCount());
				return path.removeFileExtension().addFileExtension("java");
			}
		}
		return null;
	}

	/**
	 * TestCase クラスペアのファイルを取得します。
	 * @param classPair TestCase クラスペアファイル名
	 * @param project プロジェクト
	 * @param sourceFolders ソースフォルダリスト
	 * @return TestCase ペアファイル。存在しない場合は null
	 */
	protected IFile getClassPairFile(IPath classPair, IProject project, List<IPath> sourceFolders) {
		IFile file = null;
		IWorkspaceRoot root = project.getWorkspace().getRoot();
		for(IPath sourceFolder : sourceFolders) {
			file = root.getFile(sourceFolder.append(classPair));
			if(file.exists()) {
				return file;
			}
		}
		return null;
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void init(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		
	}
}
