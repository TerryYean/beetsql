/**  
 * 文件名:    Table.java  
 * 描述:      
 * 作者:      suxj
 * 版本:      1.0  
 * 创建时间:  2015年7月30日 下午4:15:06  
 *  
 * 修改历史:  
 * 日期                          作者           版本         描述  
 * ------------------------------------------------------------------  
 * 2015年7月30日        suxj     1.0     1.0 Version  
 */ 
package org.beetl.sql.core.mapping;

/**  
 * @ClassName: Table   
 * @Description: TODO  
 * @author: suxj  
 * @date:2015年7月30日 下午4:15:06     
 */
public class Table {
	
	private int columnCount = 0;
    private String[] columnNames;
    private int[] columnTypes;
	
	public Table(){}
	
	public Table(int columnCount){
		this.columnCount = columnCount;
		this.columnNames = new String[columnCount];
        this.columnTypes = new int[columnCount];
	}

	public int getColumnCount() {
		return columnCount;
	}

	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	public String[] getColumnNames() {
		return columnNames;
	}

	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}

	public int[] getColumnTypes() {
		return columnTypes;
	}

	public void setColumnTypes(int[] columnTypes) {
		this.columnTypes = columnTypes;
	}
	
	public String getColumnName(int index) {
        if (index <= this.columnCount) {
            return this.columnNames[index];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
	
    public void setColumnName(String columnName, int index) {
        if (index <= this.columnCount) {
            this.columnNames[index] = columnName;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
    
    public void setColumnType(int columnType, int index) {
        if (index <= this.columnCount) {
            this.columnTypes[index] = columnType;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }
	
	
}
