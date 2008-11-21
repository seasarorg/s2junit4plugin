
package org.seasar.s2junit4plugin.util;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.seasar.s2junit4plugin.util.AdaptableUtil;
import org.seasar.s2junit4plugin.util.WorkbenchUtil;


public class ResouceUtil {

	public static IResource getCurrentSelectedResouce() {
		IResource result = null;
		IWorkbenchWindow window = WorkbenchUtil.getWorkbenchWindow();
		if (window != null) {
			IWorkbenchPage page = window.getActivePage();
			if (page != null) {
				// getActiveEditorで取れる参照は、フォーカスがどこにあってもアクティブなエディタの参照が取れてしまう為。
				IWorkbenchPart part = page.getActivePart();
				if (part instanceof IEditorPart) {
					IEditorPart editor = (IEditorPart) part;
					result = AdaptableUtil.toResource(editor.getEditorInput());
				}
			}
			if (result == null) {
				ISelection selection = window.getSelectionService()
						.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ss = (IStructuredSelection) selection;
					Object o = ss.getFirstElement();
					result = AdaptableUtil.toResource(o);
				}
			}
		}
		return result;
	}

}
