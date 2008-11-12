package org.seasar.s2junit4plugin.action;

import org.eclipse.core.runtime.CoreException;

public class JUnitRunAction extends JUnitLaunchAction {
    
    public JUnitRunAction() throws CoreException {
        super(ExtensionSupport.createJUnitLaunchShortcut(), "run"); //$NON-NLS-1$
    }
}
