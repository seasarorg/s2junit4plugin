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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.ide.IDE;
import org.seasar.s2junit4plugin.Activator;
import org.seasar.s2junit4plugin.Logger;
import org.seasar.s2junit4plugin.Messages;
import org.seasar.s2junit4plugin.util.FileUtil;
import org.seasar.s2junit4plugin.util.StringUtil;
import org.seasar.s2junit4plugin.util.WorkbenchUtil;


/**
 * TestCase Dicon 生成 WizardPage
 */
public class NewDiconWizardPage extends NewTypeWizardPage implements ModifyListener {
	
	private static final String ID_PLUGIN_UI = "org.seasar.s2junit4plugin"; //$NON-NLS-1$
	
	public static final String SYSTEM_ID_DICON_20 = "http://www.seasar.org/dtd/components.dtd"; //$NON-NLS-1$
	public static final String PUBLIC_ID_DICON_20 = "-//SEASAR//DTD S2Container//EN"; //$NON-NLS-1$
	public static final String DTD_DICON_20 = "/components.dtd"; //$NON-NLS-1$
	public static final String DTD_DISPLAY_20 = "2.0"; //$NON-NLS-1$

	public static final String SYSTEM_ID_DICON_21 = "http://www.seasar.org/dtd/components21.dtd"; //$NON-NLS-1$
	public static final String PUBLIC_ID_DICON_21 = "-//SEASAR2.1//DTD S2Container//EN"; //$NON-NLS-1$
	public static final String DTD_DICON_21 = "/components21.dtd"; //$NON-NLS-1$
	public static final String DTD_DISPLAY_21 = "2.1"; //$NON-NLS-1$

	public static final String SYSTEM_ID_DICON_23 = "http://www.seasar.org/dtd/components23.dtd"; //$NON-NLS-1$
	public static final String PUBLIC_ID_DICON_23 = "-//SEASAR//DTD S2Container 2.3//EN"; //$NON-NLS-1$
	public static final String DTD_DICON_23 = "/components23.dtd"; //$NON-NLS-1$
	public static final String DTD_DISPLAY_23 = "2.3"; //$NON-NLS-1$

	public static final String SYSTEM_ID_DICON_24 = "http://www.seasar.org/dtd/components24.dtd"; //$NON-NLS-1$
	public static final String PUBLIC_ID_DICON_24 = "-//SEASAR//DTD S2Container 2.4//EN"; //$NON-NLS-1$
	public static final String DTD_DICON_24 = "/components24.dtd"; //$NON-NLS-1$
	public static final String DTD_DISPLAY_24 = "2.4"; //$NON-NLS-1$

	/**
	 * Dicon のパッケージ
	 */
	private IPackageFragment packageFragment;
	
	private IPackageFragmentRoot packageFragmentRoot;

	/**
	 * Dicon ファイル名
	 */
	private String diconFileName;

	/**
	 *　Diconファイル名設定 Text
	 */
	private Text diconFileNameText;

	/**
	 * 文字エンコーディング設定 Combo
	 */
	private Combo encodingCombo;

	/**
	 * Dicon バージョン設定 Combo
	 */
	private Combo versionCombo;


	/**
	 * <code>NewDiconWizardPage</code> のインスタンスを生成します。
	 * @param packageFragment パッケージ名
	 * @param diconFileName Dicon ファイル名
	 */
	public NewDiconWizardPage(IPackageFragment packageFragment, IPackageFragmentRoot packageFragmentRoot, String diconFileName) {
		super(false, "newDiconPage"); //$NON-NLS-1$
		this.packageFragment = packageFragment;
		this.packageFragmentRoot = packageFragmentRoot;
		this.diconFileName = diconFileName;

		setTitle(Messages.getString("dicon.wizard.NewDiconWizardPage.1")); //$NON-NLS-1$
		setDescription(Messages.getString("dicon.wizard.NewDiconWizardPage.2")); //$NON-NLS-1$
		setPageComplete(true);
	}

	/**
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);

		Composite composite = new Composite(parent, SWT.NONE);
		final int nColumns = 4;
		GridLayout layout = new GridLayout();
		layout.numColumns = nColumns;
		composite.setLayout(layout);

		createContainerControls(composite, nColumns);
		createPackageControls(composite, nColumns);
		setPackageFragment(packageFragment, true);
		if(packageFragmentRoot != null) {
			setPackageFragmentRoot(packageFragmentRoot, true);
		}

		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.3")); //$NON-NLS-1$

		diconFileNameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		diconFileNameText.setText(diconFileName);
		GridData text1LData1 = new GridData();
		text1LData1.horizontalAlignment = GridData.FILL;
		text1LData1.grabExcessHorizontalSpace = true;
		diconFileNameText.setLayoutData(text1LData1);
		diconFileNameText.addModifyListener(this);

		Label nullLabel1 = new Label(composite, SWT.NONE);
		GridData nullLabelLData1 = new GridData();
		nullLabelLData1.horizontalSpan = 2;
		nullLabelLData1.horizontalAlignment = GridData.FILL;
		nullLabel1.setLayoutData(nullLabelLData1);

		Label encodingLabel = new Label(composite, SWT.NONE);
		encodingLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.4")); //$NON-NLS-1$

		encodingCombo = new Combo(composite, SWT.NONE);
		encodingCombo.addModifyListener(this);
		initEncodingCombo(encodingCombo);

		Label nullLabel2 = new Label(composite, SWT.NONE);
		GridData nullLabelLData2 = new GridData();
		nullLabelLData2.horizontalSpan = 2;
		nullLabelLData2.horizontalAlignment = GridData.FILL;
		nullLabel2.setLayoutData(nullLabelLData2);

		Label versionLabel = new Label(composite, SWT.NONE);
		versionLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.5")); //$NON-NLS-1$

		versionCombo = new Combo(composite, SWT.NONE);
		initVersionCombo(versionCombo);
		versionCombo.addModifyListener(this);

		setControl(composite);
		Dialog.applyDialogFont(composite);
	}

	/**
	 * 文字エンコーディング設定 Combo を構築します。
	 * @param combo 文字エンコーディング設定 Combo
	 */
	private void initEncodingCombo(Combo combo) {
		combo.setText("UTF-8"); //$NON-NLS-1$
		if (combo.getItemCount() == 0) {
			String[] encodings = WorkbenchUtil.getAllWorkbenchEncodings();
			for (int i = 0; i < encodings.length; i++) {
				combo.add(encodings[i]);
			}
		}
	}

	/**
	 * Dicon バージョン設定 Combo を構築します。
	 * @param combo Dicon バージョン設定 Cobo
	 */
	private void initVersionCombo(Combo combo) {
		combo.setText(DTD_DISPLAY_24);
		if (combo.getItemCount() == 0) {
			combo.add(DTD_DISPLAY_20);
			combo.add(DTD_DISPLAY_21);
			combo.add(DTD_DISPLAY_23);
			combo.add(DTD_DISPLAY_24);
		}
	}

	/**
	 * この WizardPage を初期化します。
	 * @param selection structured selection
	 */
	public void init(IStructuredSelection selection) {
		IJavaElement jelem = getInitialJavaElement(selection);
		initContainerPage(jelem);
		initTypePage(jelem);
	}

	/**
	 * メッセージ出力用の {@link IStatus} を生成します。
	 * @param severity 重要度
	 * @param message メッセージ
	 * @return ステータス情報
	 */
	private IStatus createStatus(int severity, String message) {
		return new Status(severity, ID_PLUGIN_UI, IStatus.OK, message, null);
	}

	/**
	 * Diconファイル名設定のステータス情報を生成します。
	 * @return Diconファイル名設定のステータス情報
	 */
	private IStatus createDiconFileNameStatus() {
		int severity = IStatus.OK;
		String message = ""; //$NON-NLS-1$
		if (diconFileNameText != null) {
			String value = diconFileNameText.getText();
			if (StringUtil.noneValue(value)) {
				message = Messages.getString("NewDiconWizardPage.dionFileNameEmpty"); //$NON-NLS-1$
				severity = IStatus.ERROR;
			}
		}
		return createStatus(severity, message);
	}

	/**
	 * Encoding 設定のステータス情報を生成します。
	 * @return Encoding 設定のステータス情報
	 */
	private IStatus createEncodingStatus() {
		int severity = IStatus.ERROR;
		String message = ""; //$NON-NLS-1$
		if (encodingCombo != null) {
			String enc = encodingCombo.getText();
			if (StringUtil.noneValue(enc)) {
				message = Messages.getString("dicon.wizard.NewDiconWizardPage.7"); //$NON-NLS-1$
			} else {
				try {
					String test = "enc test string"; //$NON-NLS-1$
					test.getBytes(enc);
					severity = IStatus.OK;
				} catch (UnsupportedEncodingException e) {
					message = Messages.getString("dicon.wizard.NewDiconWizardPage.8"); //$NON-NLS-1$
				}
			}
		}
		return createStatus(severity, message);
	}

	/**
	 * WizardPage のステータスを更新します。
	 */
	private void doStatusUpdate() {
		IStatus[] statuses = new IStatus[] {
				fContainerStatus, createDiconFileNameStatus(), createEncodingStatus()
		};
		updateStatus(statuses);
	}

	/**
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText(ModifyEvent e) {
		doStatusUpdate();
	}

	/**
	 * @see org.eclipse.jdt.ui.wizards.NewTypeWizardPage#handleFieldChanged(java.lang.String)
	 */
	@Override
	protected void handleFieldChanged(String fieldName) {
		super.handleFieldChanged(fieldName);
		doStatusUpdate();
	}

	/**
	 * @see org.eclipse.jdt.ui.wizards.NewElementWizardPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if(visible) {
			versionCombo.setFocus();
		}
	}

	/**
	 * 選択された Dicon DTD の Public ID を取得します。
	 * @param selected 選択された DTD 名
	 * @return DTD の Public ID
	 */
	private String getPublicId(String selected) {
		if(DTD_DISPLAY_20.equals(selected)) {
			return PUBLIC_ID_DICON_20;
		}
		if(DTD_DISPLAY_21.equals(selected)) {
			return PUBLIC_ID_DICON_21;
		}
		if(DTD_DISPLAY_24.equals(selected)) {
			return PUBLIC_ID_DICON_24;
		}
		return PUBLIC_ID_DICON_23;
	}

	/**
	 * 選択された Dicon DTD の System ID を取得します。
	 * @param selected 選択された DTD 名
	 * @return DTD の System ID
	 */
	private String getSystemId(String selected) {
		if(DTD_DISPLAY_20.equals(selected)) {
			return SYSTEM_ID_DICON_20;
		}
		if(DTD_DISPLAY_21.equals(selected)) {
			return SYSTEM_ID_DICON_21;
		}
		if(DTD_DISPLAY_24.equals(selected)) {
			return SYSTEM_ID_DICON_24;
		}
		return SYSTEM_ID_DICON_23;
	}

	/**
	 * Dicon ファイルを生成します。
	 * @return 生成に成功した場合 true，失敗した場合 false
	 */
	public boolean createDiconFile() {
		String fileName = diconFileNameText.getText();
		String enc = encodingCombo.getText();
		String publicId = getPublicId(versionCombo.getText());
		String systemId = getSystemId(versionCombo.getText());
		String dicon = Messages.getString("dicon.wizard.NewDiconWizardPage.10", //$NON-NLS-1$
				new Object[] {
					enc
				})
				+ Messages.getString("dicon.wizard.NewDiconWizardPage.11", //$NON-NLS-1$
						new Object[] {
								publicId, systemId
						})
				+ Messages.getString("dicon.wizard.NewDiconWizardPage.12"); //$NON-NLS-1$
		try {
			InputStream contents = new ByteArrayInputStream(dicon.getBytes(enc));
			IPackageFragmentRoot sourceFolder = getPackageFragmentRoot();
			IPackageFragment packageFragment = sourceFolder.createPackageFragment(getPackageFragment().getElementName(), false, null);
			IFile file = FileUtil.createFile(packageFragment, fileName, contents);
			if (file != null) {
				file.setCharset(enc, null);
				IWorkbench workbench = Activator.getDefault().getWorkbench();
				IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
				return true;
			}
		} catch (CoreException e) {
			Logger.error(e, this);
		} catch (UnsupportedEncodingException e) {
			Logger.error(e, this);
		}
		return false;
	}
}
