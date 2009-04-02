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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.IPreferenceStore;


public class NamingRules {
    private IPreferenceStore store;
    private final String STORE_ID = "NamingRules";

    public NamingRules(IPreferenceStore store) {
        this.store = store;
    }

    public List get() {
        String value = store.getString(STORE_ID);
        if (value == null || value.length() == 0)
            return getDefault();
        return stringToList(value);
    }

    public String[] getEnableValues() {
        List namingRules = get();
        List result = new ArrayList();
        for (int i = 0; i < namingRules.size(); ++i) {
            NamingRule rule = (NamingRule) namingRules.get(i);
            if (rule.isEnabled()) {
                result.add(rule.getValue());
            }
        }
        return (String[]) result.toArray(new String[result.size()]);
    }


    public void set(List namingRules) {
        store.setValue(STORE_ID, listToString(namingRules));
    }

    private String listToString(List namingRules) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < namingRules.size(); ++i) {
            if (i != 0)
                buf.append(',');
            NamingRule rule = (NamingRule) namingRules.get(i);
            buf.append(rule.getValue() + ":" + rule.isEnabled());
        }
        return buf.toString();
    }

    private List stringToList(String string) {
        List result = new ArrayList();
        StringTokenizer st = new StringTokenizer(string, ",");
        while(st.hasMoreTokens()) {
            String column = st.nextToken();
            int index = column.indexOf(':');
            if (index != -1) {
                String value = column.substring(0, index);
                Boolean enabled = Boolean.valueOf(column.substring(index + 1));
                result.add(new NamingRule(value, enabled.booleanValue()));      
            }
        }
        return result;
    }

    public List getDefault() {
        List result = new ArrayList();
        result.add(new NamingRule("${package}.${type}Test", true));
//        result.add(new NamingRule("${package}.${type}PDETest", false));
        return result;
    }
}