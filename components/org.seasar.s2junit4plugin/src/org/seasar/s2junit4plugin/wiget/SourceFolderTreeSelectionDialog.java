package org.seasar.s2junit4plugin.wiget;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;


/**
 * ソースフォルダ、テストソースフォルダを選択するダイアログです。
 * 
 * 
 * @author goooooooooosuke
 * 
 */
public class SourceFolderTreeSelectionDialog extends ElementTreeSelectionDialog {
	
	private IJavaProject project;
	
	/**
	 * コンストラクタ
	 * 
	 * @param parent
	 * @param project
	 * @param isJavaSourceFolder
	 */
	public SourceFolderTreeSelectionDialog(Shell parent, IJavaProject project, String title, String message) {
		super(parent, new WorkbenchLabelProvider(), new BaseWorkbenchContentProvider() {
			@Override
			public boolean hasChildren(Object element) {
				return false;
			}
		});
		this.project = project;
		setTitle(title);
		setMessage(message);
		setInput(project);
		setInitialSelection(project);
		setAllowMultiple(false);
		addFilter(new SourceFolderViewerFilter());
	}
	
	/**
	 * 選択されたソースフォルダのパスを戻します。
	 * 
	 * @return 選択されたソースフォルダのパス
	 */
	public String getSelectedSourceFolder() {
		String selectedSourceFolder = "/"; // 初期値は"/"とする
		Object[] results = getResult();
		if (results != null && results.length > 0) {
			if (results[0] instanceof IPackageFragmentRoot) {
				IPackageFragmentRoot packageFragmentRoot = (IPackageFragmentRoot) results[0];
				String folderName = packageFragmentRoot.getPath().toString();
				String projectPath = project.getPath().toString();
				if (folderName.length() <= projectPath.length()) {
					selectedSourceFolder = folderName;
				} else {
					selectedSourceFolder = folderName.substring(projectPath.length());
				}
			}
		}
		return selectedSourceFolder;
	}
	
	/**
	 * ソースフォルダだけを表示させるためのフィルタクラスです。
	 * 
	 * @author goooooooooosuke
	 *
	 */
	private class SourceFolderViewerFilter extends ViewerFilter {
		
		/*
		 * (非 Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return element instanceof IPackageFragmentRoot && !((IPackageFragmentRoot) element).isArchive();
		}
	}
	
}
