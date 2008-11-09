package org.seasar.s2junit4plugin.action;

import org.eclipse.core.runtime.CoreException;

public class JUnitDebugAction extends JUnitLaunchAction {
    
	public JUnitDebugAction() throws CoreException {
		super(ExtensionSupport.createJUnitLaunchShortcut(), "debug"); //$NON-NLS-1$
    }
}
