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
package org.seasar.s2junit4plugin.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;


public class WorkbenchUtil  {
	
	private static final  String[] ECLIPSE_ENCODINGS = new String[] {
			"ISO-8859-1", "UTF-8", "UTF-16", "UTF-16BE", "UTF-16LE", "US-ASCII",
			"Shift_JIS", "Windows-31J"
	};
	
	public static String[] getAllWorkbenchEncodings() {
		List list = new ArrayList();
		for (int i = 0; i < ECLIPSE_ENCODINGS.length; i++) {
			list.add(ECLIPSE_ENCODINGS[i]);
		}
		String defaultEnc = System.getProperty("file.encoding", "UTF-8");
		if (!list.contains(defaultEnc)) {
			list.add(defaultEnc);
		}
		String eclipseEnc = getWorkbenchEncoding();
		if (StringUtil.existValue(eclipseEnc) && !list.contains(eclipseEnc)) {
			list.add(eclipseEnc);
		}
		Collections.sort(list);
		return (String[]) list.toArray(new String[list.size()]);
	}

	public static String getWorkbenchEncoding() {
		String enc = ResourcesPlugin.getPlugin().getPluginPreferences().getString(
				ResourcesPlugin.PREF_ENCODING);
		if (StringUtil.noneValue(enc)) {
			enc = System.getProperty("file.encoding", "UTF-8");
		}
		return enc;
	}
	

	public static IWorkbenchWindow getWorkbenchWindow() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow result = workbench.getActiveWorkbenchWindow();
		if (result == null && 0 < workbench.getWorkbenchWindowCount()) {
			IWorkbenchWindow[] ws = workbench.getWorkbenchWindows();
			result = ws[0];
		}
		return result;
	}



}
