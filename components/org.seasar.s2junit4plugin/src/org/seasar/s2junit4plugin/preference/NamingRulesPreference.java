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
package org.seasar.s2junit4plugin.preference;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.seasar.s2junit4plugin.Messages;
import junit.extensions.eclipse.quick.NamingRule;
import junit.extensions.eclipse.quick.NamingRules;;


public class NamingRulesPreference extends Composite {
    private Shell shell;
    private List namingRulesValue;

    private CheckboxTableViewer tableViewer;
    private Button removeButton;
    private Button editButton;
    private Button moveUpButton;
    private Button moveDownButton;
    private int widthHint;
    private NamingRules namingRules;

    public NamingRulesPreference(IPreferenceStore store, Composite tabFolder, int widthHint) {
    	super(tabFolder, SWT.NULL);
    	namingRules = new NamingRules(store);
    	namingRulesValue = namingRules.get();
        GridLayout layout= new GridLayout();
        layout.numColumns= 1;
        layout.marginWidth= 0;
        setLayout(layout);
        GridData data= new GridData(GridData.FILL_BOTH);
        setLayoutData(data);
    	this.widthHint = widthHint;
    	create(namingRulesValue, this);
    }
    
    private void create(List namingRulesValue, Composite parent) {
        this.namingRulesValue = namingRulesValue;
        shell = parent.getShell();
        Composite container= new Composite(parent, SWT.NONE);
        GridLayout layout= new GridLayout();
        layout.numColumns= 2;
        layout.marginHeight= 0;
        layout.marginWidth= 0;
        container.setLayout(layout);
        GridData gd= new GridData(GridData.FILL_BOTH);
        container.setLayoutData(gd);
        createTable(container);
        createButtons(container);
        updateView();
    }

    private void createTable(Composite container) {
        Label label= new Label(container, SWT.NONE);
        label.setText(Messages.getString("NamingRulesPreference.label")); //$NON-NLS-1$
        GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
        gd.horizontalSpan = 2;
        label.setLayoutData(gd);

        Table table = new Table(container, SWT.CHECK | SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);

        gd= new GridData(GridData.FILL_HORIZONTAL);
        table.setLayoutData(gd);

        TableLayout tableLayout= new TableLayout();
        ColumnLayoutData[] columnLayoutData= new ColumnLayoutData[1];
        columnLayoutData[0]= new ColumnWeightData(100);
        tableLayout.addColumnData(columnLayoutData[0]);
        table.setLayout(tableLayout);
        new TableColumn(table, SWT.NONE);
        tableViewer = new CheckboxTableViewer(table);
        tableViewer.setLabelProvider(new TableLabelProvider());
        tableViewer.setContentProvider(new TableContentProvider());
        tableViewer.setInput(this);

        gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
        tableViewer.getTable().setLayoutData(gd);
        tableViewer.addCheckStateListener(new ICheckStateListener() {
            public void checkStateChanged(CheckStateChangedEvent event) {
                NamingRule namingRule = (NamingRule) event.getElement();
                namingRule.setEnabled(event.getChecked());
                updateView();
            }
        });
        tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
            public void selectionChanged(SelectionChangedEvent event) {
                // unable if call update
                updateButtons();
            }
        });
        tableViewer.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                editNamingRule();
            }
        });
    }
    private static class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
        public String getColumnText(Object o, int column) {
            return column == 0 ? ((NamingRule) o).getValue() : ""; //$NON-NLS-1$
        }
        public String getText(Object element) {
            return ((NamingRule) element).getValue();
        }
        public Image getColumnImage(Object element, int columnIndex) {
            return null;
        }
    }
    private class TableContentProvider implements IStructuredContentProvider {
        public Object[] getElements(Object inputElement) {
            return namingRulesValue.toArray();
        }
        public void dispose() {
        }
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }
    }
    private void createButtons(Composite container) {
        Composite buttonContainer= new Composite(container, SWT.NONE);
        GridData gd= new GridData(GridData.FILL_VERTICAL);
        buttonContainer.setLayoutData(gd);
        GridLayout buttonLayout= new GridLayout();
        buttonLayout.numColumns= 1;
        buttonLayout.marginHeight= 0;
        buttonLayout.marginWidth= 0;
        buttonContainer.setLayout(buttonLayout);

        Listener listener;

        listener = new Listener() {
            public void handleEvent(Event e) {
                addNamingRule();
            }};
        createButton("addButton", buttonContainer, listener, true); //$NON-NLS-1$

        listener = new Listener() {
            public void handleEvent(Event e) {
                removeNamingRules();
            }};
        removeButton = createButton("removeButton", buttonContainer, listener, false); //$NON-NLS-1$
        removeButton.setEnabled(false);

        listener = new Listener() {
            public void handleEvent(Event e) {
                editNamingRule();
            }};
        editButton = createButton("editButton", buttonContainer, listener, false); //$NON-NLS-1$
        editButton.setEnabled(false);

        listener = new Listener() {
            public void handleEvent(Event e) {
                moveNamingRule(true);
            }};
        moveUpButton = createButton("moveUpButton", buttonContainer, listener, false); //$NON-NLS-1$
        moveUpButton.setEnabled(false);

        listener = new Listener() {
            public void handleEvent(Event e) {
                moveNamingRule(false);
            }};
        moveDownButton = createButton("moveDownButton", buttonContainer, listener, false); //$NON-NLS-1$
        moveDownButton.setEnabled(false);
     }
    private void updateView() {
        tableViewer.refresh();
        for (int i = 0; i < namingRulesValue.size(); ++i) {
            NamingRule namingRule = (NamingRule) namingRulesValue.get(i);
            tableViewer.setChecked(namingRule, namingRule.isEnabled());
        }
        updateButtons();
    }

    private void updateButtons() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        removeButton.setEnabled(!selection.isEmpty());
        editButton.setEnabled(selection.size() == 1);
        int rowCount = tableViewer.getTable().getItemCount();
        boolean canMove = selection.size() == 1 &&  rowCount > 1;
        if (!canMove) {
            moveUpButton.setEnabled(false);
            moveDownButton.setEnabled(false);
        } else {
           int selectedIndex = namingRulesValue.indexOf(selection.getFirstElement());
           moveUpButton.setEnabled(0 < selectedIndex);
           moveDownButton.setEnabled(selectedIndex < rowCount - 1);
        }
    }
    private void editNamingRule() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        if (selection.isEmpty())
            return;
        NamingRule rule = (NamingRule) selection.getFirstElement();
        InputDialog dialog = createEditDialog("editNamingRule", rule.getValue()); //$NON-NLS-1$
        if (dialog.open() == Window.OK) {
            String value = dialog.getValue();
            if (value.trim().length() != 0) {
                rule.setValue(value);
                updateView();
            }
        }
    }
    private InputDialog createEditDialog(String messageId, String initValue) {
        String title = Messages.getString("NamingRulesPreference." + messageId + ".dialog.title"); //$NON-NLS-1$ //$NON-NLS-2$
        String message = Messages.getString("NamingRulesPreference." + messageId + ".dialog.message"); //$NON-NLS-1$ //$NON-NLS-2$
        return  new InputDialog(shell, title, message, initValue, new NamingRuleValidator());
    }
    private static class NamingRuleValidator implements IInputValidator {
        public String isValid(String newText) {
            newText = newText.trim();
            if (newText.length() == 0)
                return Messages.getString("NamingRulesPreference.namingRuleValidator.empty"); //$NON-NLS-1$

            newText = newText.replaceAll("\\$\\{package\\}", "package"); //$NON-NLS-1$ //$NON-NLS-2$
            newText = newText.replaceAll("\\$\\{type\\}", "type"); //$NON-NLS-1$ //$NON-NLS-2$

            StringTokenizer st = new StringTokenizer(newText, ".", true); //$NON-NLS-1$
            boolean dot = false;
            while(st.hasMoreTokens()) {
                String token = st.nextToken();
                if (dot) {
                    if (!token.equals(".")) { //$NON-NLS-1$
                        return Messages.getString("NamingRulesPreference.namingRuleValidator.error"); //$NON-NLS-1$
                    }
                    dot = false;
                } else {
                    if (!isJavaIdentifier(token))
                        return Messages.getString("NamingRulesPreference.namingRuleValidator.tokenError", token); //$NON-NLS-1$
                    dot = true;
                }
            }
            return null;
        }
        private boolean isJavaIdentifier(String token) {
            if (!Character.isJavaIdentifierStart(token.charAt(0)))
                return false;
            for (int i = 1; i < token.length(); ++i) {
                if (!Character.isJavaIdentifierPart(token.charAt(i)))
                return false;
            }
            return true;
        }
    }
    private void addNamingRule() {
        InputDialog dialog = createEditDialog("addNamingRule", ""); //$NON-NLS-1$ //$NON-NLS-2$
        if (dialog.open() == Window.OK) {
            String value = dialog.getValue();
            if (value.trim().length() != 0) {
                NamingRule rule = new NamingRule(value, true);
                namingRulesValue.add(rule);
                updateView();
            }
        }
    }
    private void removeNamingRules() {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        if (selection.isEmpty())
            return;
        for (Iterator i = selection.iterator(); i.hasNext(); ) {
             namingRulesValue.remove(i.next());
        }
        updateView();
    }
    private void moveNamingRule(boolean up) {
        IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
        if (selection.isEmpty() || selection.size() > 1)
            return;
        Object selected = selection.getFirstElement();
        int oldIndex = namingRulesValue.indexOf(selected);
        int newIndex = up ? oldIndex - 1: oldIndex + 1;
        if (newIndex < 0 || namingRulesValue.size() <= newIndex)
            return;
        namingRulesValue.remove(oldIndex);
        namingRulesValue.add(newIndex, selected);
        updateView();
    }
    private Button createButton(String buttonId, Composite buttonContainer, Listener listener, boolean isTop) {
        Button button= new Button(buttonContainer, SWT.PUSH);
        button.setText(Messages.getString("NamingRulesPreference." + buttonId + ".label")); //$NON-NLS-1$ //$NON-NLS-2$
        button.setToolTipText(Messages.getString("NamingRulesPreference." + buttonId + ".tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
        GridData gd;
        if (isTop)
            gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        else
            gd = getButtonGridData(button);
        button.setLayoutData(gd);
        button.addListener(SWT.Selection, listener);
        return button;
    }
    
    private GridData getButtonGridData(Button button) {
        GridData gd= new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
        gd.widthHint= Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
        return gd;
    }
    
    public void store() {
    	namingRules.set(namingRulesValue);
    }
    
    public void loadDefault() {
        this.namingRulesValue = namingRules.getDefault();
        updateView();
    }
}
