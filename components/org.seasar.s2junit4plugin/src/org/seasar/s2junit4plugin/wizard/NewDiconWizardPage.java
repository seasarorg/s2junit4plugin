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
	
	private static final String ID_PLUGIN_UI = "org.seasar.s2junit4plugin";
	
	public static final String SYSTEM_ID_DICON_20 = "http://www.seasar.org/dtd/components.dtd";
	public static final String PUBLIC_ID_DICON_20 = "-//SEASAR//DTD S2Container//EN";
	public static final String DTD_DICON_20 = "/components.dtd";
	public static final String DTD_DISPLAY_20 = "2.0";

	public static final String SYSTEM_ID_DICON_21 = "http://www.seasar.org/dtd/components21.dtd";
	public static final String PUBLIC_ID_DICON_21 = "-//SEASAR2.1//DTD S2Container//EN";
	public static final String DTD_DICON_21 = "/components21.dtd";
	public static final String DTD_DISPLAY_21 = "2.1";

	public static final String SYSTEM_ID_DICON_23 = "http://www.seasar.org/dtd/components23.dtd";
	public static final String PUBLIC_ID_DICON_23 = "-//SEASAR//DTD S2Container 2.3//EN";
	public static final String DTD_DICON_23 = "/components23.dtd";
	public static final String DTD_DISPLAY_23 = "2.3";

	public static final String SYSTEM_ID_DICON_24 = "http://www.seasar.org/dtd/components24.dtd";
	public static final String PUBLIC_ID_DICON_24 = "-//SEASAR//DTD S2Container 2.4//EN";
	public static final String DTD_DICON_24 = "/components24.dtd";
	public static final String DTD_DISPLAY_24 = "2.4";

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
		super(false, "newDiconPage");
		this.packageFragment = packageFragment;
		this.packageFragmentRoot = packageFragmentRoot;
		this.diconFileName = diconFileName;

		setTitle(Messages.getString("dicon.wizard.NewDiconWizardPage.1"));
		setDescription(Messages.getString("dicon.wizard.NewDiconWizardPage.2"));
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
		setPackageFragment(packageFragment, false);
		if(packageFragmentRoot != null) {
			setPackageFragmentRoot(packageFragmentRoot, true);
		}

		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.3"));
		nameLabel.setEnabled(false);

		Text nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
		nameText.setText(diconFileName);
		GridData text1LData1 = new GridData();
		text1LData1.horizontalAlignment = GridData.FILL;
		text1LData1.grabExcessHorizontalSpace = true;
		nameText.setLayoutData(text1LData1);
		nameText.setEditable(false);
		nameText.setEnabled(false);

		Label nullLabel1 = new Label(composite, SWT.NONE);
		GridData nullLabelLData1 = new GridData();
		nullLabelLData1.horizontalSpan = 2;
		nullLabelLData1.horizontalAlignment = GridData.FILL;
		nullLabel1.setLayoutData(nullLabelLData1);

		Label encodingLabel = new Label(composite, SWT.NONE);
		encodingLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.4"));

		encodingCombo = new Combo(composite, SWT.NONE);
		encodingCombo.addModifyListener(this);
		initEncodingCombo(encodingCombo);

		Label nullLabel2 = new Label(composite, SWT.NONE);
		GridData nullLabelLData2 = new GridData();
		nullLabelLData2.horizontalSpan = 2;
		nullLabelLData2.horizontalAlignment = GridData.FILL;
		nullLabel2.setLayoutData(nullLabelLData2);

		Label versionLabel = new Label(composite, SWT.NONE);
		versionLabel.setText(Messages.getString("dicon.wizard.NewDiconWizardPage.5"));

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
		combo.setText("UTF-8");
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
	 * Encoding 設定のステータス情報を生成します。
	 * @return Encoding 設定のステータス情報
	 */
	private IStatus createEncodingStatus() {
		int severity = IStatus.ERROR;
		String message = "";
		if (encodingCombo != null) {
			String enc = encodingCombo.getText();
			if (StringUtil.noneValue(enc)) {
				message = Messages.getString("dicon.wizard.NewDiconWizardPage.7");
			} else {
				try {
					String test = "enc test string";
					test.getBytes(enc);
					severity = IStatus.OK;
				} catch (UnsupportedEncodingException e) {
					message = Messages.getString("dicon.wizard.NewDiconWizardPage.8");
				}
			}
		}
		return createStatus(severity, message);
	}

	/**
	 * WizardPage のステータスを更新します。
	 */
	private void doStatusUpdate() {
		IStatus encodingStatus = createEncodingStatus();
		IStatus[] status = new IStatus[] {
				fContainerStatus, encodingStatus
		};
		updateStatus(status);
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
		String enc = encodingCombo.getText();
		String publicId = getPublicId(versionCombo.getText());
		String systemId = getSystemId(versionCombo.getText());
		String dicon = Messages.getString("dicon.wizard.NewDiconWizardPage.10",
				new Object[] {
					enc
				})
				+ Messages.getString("dicon.wizard.NewDiconWizardPage.11",
						new Object[] {
								publicId, systemId
						})
				+ Messages.getString("dicon.wizard.NewDiconWizardPage.12");
		try {
			InputStream contents = new ByteArrayInputStream(dicon.getBytes(enc));
			IPackageFragmentRoot sourceFolder = getPackageFragmentRoot();
			IPackageFragment packageFragment = sourceFolder.createPackageFragment(this.packageFragment.getElementName(), false, null);
			IFile file = FileUtil.createFile(packageFragment, diconFileName, contents);
			if (file != null) {
				file.setCharset(enc, null);
				IWorkbench workbench = Activator.getDefault().getWorkbench();
				IDE.openEditor(workbench.getActiveWorkbenchWindow().getActivePage(), file);
//				WorkbenchUtils.openDiconEditor(file);
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
