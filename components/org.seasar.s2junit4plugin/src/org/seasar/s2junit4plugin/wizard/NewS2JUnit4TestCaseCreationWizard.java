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
 * This is a program source code derived from the Eclipse Software.
 * An original copyright notice is as follows.
 *******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************
 */
package org.seasar.s2junit4plugin.wizard;

import org.eclipse.core.resources.IResource;

import org.eclipse.jdt.core.IType;

import org.eclipse.jdt.junit.wizards.NewTestCaseWizardPageOne;
import org.eclipse.jdt.junit.wizards.NewTestCaseWizardPageTwo;

import org.eclipse.jdt.internal.junit.ui.JUnitPlugin;
import org.eclipse.jdt.internal.junit.wizards.JUnitWizard;
import org.eclipse.jdt.internal.junit.wizards.WizardMessages;

/**
 * A wizard for creating test cases.
 */
public class NewS2JUnit4TestCaseCreationWizard extends JUnitWizard {

	private NewS2JUnit4TestCaseWizardPageOne fPage1;
	private NewTestCaseWizardPageTwo fPage2;

	public NewS2JUnit4TestCaseCreationWizard() {
		super();
		setWindowTitle(WizardMessages.Wizard_title_new_testcase); 
		initDialogSettings();
	}

	protected void initializeDefaultPageImageDescriptor() {
		setDefaultPageImageDescriptor(JUnitPlugin.getImageDescriptor("wizban/newtest_wiz.png")); //$NON-NLS-1$
	}

	/*
	 * @see Wizard#createPages
	 */	
	public void addPages() {
		super.addPages();
		fPage2= new NewTestCaseWizardPageTwo();
		fPage1= new NewS2JUnit4TestCaseWizardPageOne(fPage2);
		addPage(fPage1);
		fPage1.init(getSelection());
		addPage(fPage2);
	}	
	
	/*
	 * @see Wizard#performFinish
	 */		
	public boolean performFinish() {
		if (finishPage(fPage1.getRunnable())) {
			IType newClass= fPage1.getCreatedType();
		
			IResource resource= newClass.getCompilationUnit().getResource();
			if (resource != null) {
				selectAndReveal(resource);
				openResource(resource);
			}
			return true;
		}
		return false;		
	}
}
