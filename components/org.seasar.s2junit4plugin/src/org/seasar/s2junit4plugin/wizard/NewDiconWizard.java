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
package org.seasar.s2junit4plugin.wizard;

import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.seasar.s2junit4plugin.Messages;


/**
 * TestCase Dicon 生成 Wizard
 */
public class NewDiconWizard extends BasicNewResourceWizard {

	/**
	 * Dicon 生成 {@link IWizardPage}
	 */
	private NewDiconWizardPage page;

	/**
	 * Dicon を生成する Java パッケージ
	 */
	private IPackageFragment packageFragment;

	/**
	 * Dicon ファイル名
	 */
	private String diconFileName;
	
	private IPackageFragmentRoot packageFragmentRoot;

	/**
	 * <code>NewDiconWizard</code> のインスタンスを生成します。
	 * @param packageFragment パッケージ
	 * @param diconFileName Dicon ファイル名
	 */
	public NewDiconWizard(IPackageFragment packageFragment, String diconFileName) {
		this(packageFragment, null, diconFileName);
	}
	
	public NewDiconWizard(IPackageFragment packageFragment, IPackageFragmentRoot packageFragmentRoot, String diconFileName) {
		this.packageFragment = packageFragment;
		this.packageFragmentRoot = packageFragmentRoot;
		this.diconFileName = diconFileName;
	}

	/**
	 * @see org.eclipse.ui.wizards.newresource.BasicNewResourceWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		super.init(workbench, currentSelection);
		setWindowTitle(Messages.getString("dicon.wizard.NewDiconWizard.1")); //$NON-NLS-1$
		setNeedsProgressMonitor(true);
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		page = new NewDiconWizardPage(packageFragment, packageFragmentRoot, diconFileName);
		addPage(page);
		page.init(getSelection());
	}

	/**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return page.createDiconFile();
	}

}
