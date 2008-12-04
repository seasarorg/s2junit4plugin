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
 * This is a program source code derived from the Quick JUnit Plugin for Eclipse.
 * An original copyright:Copyright © 2003-2008 Masaru Ishii,The Quick JUnit Plugin Project.
 */
package org.seasar.s2junit4plugin.action;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

public class HelpSupport {

    public static void setHelp(final Shell control, final String contextId) {
        final IWorkbench workbench = PlatformUI.getWorkbench();
        workbench.getHelpSystem().setHelp(control, contextId);
    }
}
