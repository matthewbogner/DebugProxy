package org.debugproxy;

import javax.swing.table.AbstractTableModel;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author: mbogner
 * @date: 2/17/11
 */
public class RewriteRuleTableModel extends AbstractTableModel {

    private Properties _ruleProps;
    private List<RewriteRule> _rules;
    private static final String PROPERTIES_FILENAME = "debugProxyRules.properties";

    public RewriteRuleTableModel() {
        _ruleProps = new Properties();
        //try retrieve data from file
        try {
            _ruleProps.load(new FileInputStream(PROPERTIES_FILENAME));
        } catch (IOException e) {
            //file likely doesn't exist.
        }

        _rules = new ArrayList<RewriteRule>();

        for(Map.Entry<Object, Object> property: _ruleProps.entrySet()) {
            try {
                String url = URLDecoder.decode((String)property.getKey(), "UTF-8");
                String fileName = URLDecoder.decode((String)property.getValue(), "UTF-8");
                _rules.add(new RewriteRule(url, fileName));
            } catch (UnsupportedEncodingException e) {
                //Shouldn't ever happen
                e.printStackTrace();
            }

        }
    }

    public RewriteRuleTableModel(List < RewriteRule > rules) {
        _rules = rules;
    }

    public List<RewriteRule> getAllRules() {
        return _rules;
    }

    public void addRule(RewriteRule rule) {
        _rules.add(rule);
        try {
            _ruleProps.setProperty(URLEncoder.encode(rule.getUrl(), "UTF-8"), URLEncoder.encode(rule.getFileName(), "UTF-8"));
            _ruleProps.store(new FileOutputStream(PROPERTIES_FILENAME), null);
        } catch (UnsupportedEncodingException e) {
            //Shouldn't ever happen
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        fireTableDataChanged();
    }

    public void removeRule(int index) {
        if (_rules.size() > index) {
            String url = _rules.get(index).getUrl();
            _rules.remove(index);

            try {
                _ruleProps.remove(URLEncoder.encode(url, "UTF-8"));
                _ruleProps.store(new FileOutputStream(PROPERTIES_FILENAME), null);
            } catch (UnsupportedEncodingException e) {
                //Shouldn't ever happen
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            fireTableDataChanged();
        }
    }

    /**
     * Returns the number of rows in the model. A
     * <code>JTable</code> uses this method to determine how many rows it
     * should display.  This method should be quick, as it
     * is called frequently during rendering.
     *
     * @return the number of rows in the model
     * @see #getColumnCount
     */
    @Override
    public int getRowCount() {
        if (_rules != null) {
            return _rules.size();
        }
        return 0;
    }

    /**
     * Returns the number of columns in the model. A
     * <code>JTable</code> uses this method to determine how many columns it
     * should create and display by default.
     *
     * @return the number of columns in the model
     * @see #getRowCount
     */
    @Override
    public int getColumnCount() {
        return RewriteRule.getColumnNames().length;
    }

    /**
     * Returns the name of the column at <code>columnIndex</code>.  This is used
     * to initialize the table's column header name.  Note: this name does
     * not need to be unique; two columns in a table can have the same name.
     *
     * @return the name of the column
     * @param    columnIndex    the index of the column
     */
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < RewriteRule.getColumnNames().length) {
            return RewriteRule.getColumnNames()[columnIndex];
        }
        return null;
    }

    /**
     * Returns the most specific superclass for all the cell values
     * in the column.  This is used by the <code>JTable</code> to set up a
     * default renderer and editor for the column.
     *
     * @param columnIndex the index of the column
     * @return the common ancestor class of the object values in the model.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param    rowIndex    the row whose value to be queried
     * @param    columnIndex    the column whose value to be queried
     * @return true if the cell is editable
     * @see #setValueAt
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * Returns the value for the cell at <code>columnIndex</code> and
     * <code>rowIndex</code>.
     *
     * @param    rowIndex    the row whose value is to be queried
     * @param    columnIndex the column whose value is to be queried
     * @return the value Object at the specified cell
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < _rules.size() && columnIndex < RewriteRule.getColumnNames().length) {
            return _rules.get(rowIndex).getValueForColumn(columnIndex);
        }
        return null;
    }

    /**
     * Sets the value in the cell at <code>columnIndex</code> and
     * <code>rowIndex</code> to <code>aValue</code>.
     *
     * @param    aValue         the new value
     * @param    rowIndex     the row whose value is to be changed
     * @param    columnIndex the column whose value is to be changed
     * @see #getValueAt
     * @see #isCellEditable
     */
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        //no-op
        //Individual cells are intentionally un-editable
    }
}
